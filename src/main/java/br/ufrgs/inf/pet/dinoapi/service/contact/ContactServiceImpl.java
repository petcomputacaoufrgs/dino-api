package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.GoogleContact;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.model.contacts.ContactDataModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.ContactRepository;
import br.ufrgs.inf.pet.dinoapi.repository.contact.PhoneRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.async.AsyncContactService;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.SynchronizableQueueMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl extends SynchronizableServiceImpl<Contact, Long, ContactDataModel, ContactRepository> {

    private final PhoneRepository phoneRepository;
    private final AsyncContactService asyncContactService;
    private final GoogleContactServiceImpl googleContactService;

    @Autowired
    public ContactServiceImpl(ContactRepository repository, AuthServiceImpl authService,
                              ClockServiceImpl clockService, LogAPIErrorServiceImpl logAPIErrorService, PhoneRepository phoneRepository,
                              SynchronizableQueueMessageService<Long, ContactDataModel> synchronizableQueueMessageService,
                              AsyncContactService asyncContactService, GoogleContactServiceImpl googleContactService) {
        super(repository, authService, clockService, synchronizableQueueMessageService, logAPIErrorService);
        this.phoneRepository = phoneRepository;
        this.asyncContactService = asyncContactService;
        this.googleContactService = googleContactService;
    }

    @Override
    public ContactDataModel convertEntityToModel(Contact entity) {
        final ContactDataModel model = new ContactDataModel();
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setColor(entity.getColor());

        return model;
    }

    @Override
    public Contact convertModelToEntity(ContactDataModel model, Auth auth) throws AuthNullException {
        if (auth != null) {
            final Contact entity = new Contact();
            entity.setName(model.getName());
            entity.setDescription(model.getDescription());
            entity.setColor(model.getColor());
            entity.setUser(auth.getUser());

            return entity;
        } else throw new AuthNullException();
    }

    @Override
    public void updateEntity(Contact entity, ContactDataModel model, Auth auth) throws AuthNullException {
        if (auth != null) {
            entity.setName(model.getName());
            entity.setDescription(model.getDescription());
            entity.setColor(model.getColor());
        } else throw new AuthNullException();
    }

    @Override
    public Optional<Contact> findEntityByIdThatUserCanRead(Long id, Auth auth) throws AuthNullException {
        return this.findByIdAndUser(id, auth);
    }

    @Override
    public Optional<Contact> findEntityByIdThatUserCanEdit(Long id, Auth auth) throws AuthNullException {
        return this.findByIdAndUser(id, auth);
    }

    private Optional<Contact> findByIdAndUser(Long id, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findByIdAndUserId(id, auth.getUser().getId());
    }

    @Override
    public List<Contact> findEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByUserId(auth.getUser().getId());
    }

    @Override
    public List<Contact> findEntitiesByIdThatUserCanEdit(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByIdsAndUserId(ids, auth.getUser().getId());
    }

    @Override
    public List<Contact> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByUserIdExcludingIds(auth.getUser().getId(), ids);
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.CONTACT;
    }

    @Override
    public boolean shouldDelete(Contact contact, SynchronizableDeleteModel<Long> model) {
        final Integer phoneCount = phoneRepository.countByContactId(contact.getId());

        return phoneCount == 0;
    }

    @Override
    protected void afterDataCreated(Contact entity, Auth auth) {
        asyncContactService.createContactOnGoogleAPI(entity, auth);
    }

    @Override
    protected void afterDataUpdated(Contact entity, Auth auth) {
        asyncContactService.updateContactOnGoogleAPI(entity, auth);
    }

    @Override
    protected void beforeDataDeleted(Contact entity, Auth auth) {
        final Optional<GoogleContact> googleContactSearch = this.googleContactService.findByContactId(entity.getId());

        googleContactSearch.ifPresent(googleContact -> {
            asyncContactService.deleteContactOnGoogleAPI(googleContact.getResourceName(), auth);
        });
    }
}
