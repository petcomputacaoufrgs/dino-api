package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.contacts.ContactDataModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.ContactRepository;
import br.ufrgs.inf.pet.dinoapi.repository.contact.EssentialContactRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.OAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.SynchronizableQueueMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl extends SynchronizableServiceImpl<Contact, Long, Integer, ContactDataModel, ContactRepository> {

    private final EssentialContactRepository essentialContactRepository;

    @Autowired
    public ContactServiceImpl(ContactRepository repository, OAuthServiceImpl authService, EssentialContactRepository essentialContactRepository,
                              ClockServiceImpl clockService, LogAPIErrorServiceImpl logAPIErrorService,
                              SynchronizableQueueMessageServiceImpl<Long, Integer, ContactDataModel> synchronizableQueueMessageService) {
        super(repository, authService, clockService, synchronizableQueueMessageService, logAPIErrorService);
        this.essentialContactRepository = essentialContactRepository;
    }

    @Override
    public ContactDataModel convertEntityToModel(Contact entity) {
        final ContactDataModel model = new ContactDataModel();
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setColor(entity.getColor());

        final EssentialContact essentialContact = entity.getEssentialContact();

        if (essentialContact != null) {
            model.setEssentialContactId(essentialContact.getId());
        }

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

            final Long essentialContactId = model.getEssentialContactId();

            if (essentialContactId != null) {
                final Optional<EssentialContact> essentialContactSearch = essentialContactRepository.findById(essentialContactId);

                essentialContactSearch.ifPresent(entity::setEssentialContact);
            }

            return entity;
        } else {
            throw new AuthNullException();
        }
    }

    @Override
    public void updateEntity(Contact entity, ContactDataModel model, Auth auth) throws AuthNullException {
        if (auth != null) {
            entity.setName(model.getName());
            entity.setDescription(model.getDescription());
            entity.setColor(model.getColor());
        } else {
            throw new AuthNullException();
        }
    }

    @Override
    public Optional<Contact> getEntityByIdAndUserAuth(Long id, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findByIdAndUserId(id, auth.getUser().getId());
    }

    @Override
    public List<Contact> getEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByUserId(auth.getUser().getId());
    }

    @Override
    public List<Contact> getEntitiesByIdsAndUserAuth(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByIdsAndUserId(ids, auth.getUser().getId());
    }

    @Override
    public List<Contact> getEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByUserIdExceptIds(auth.getUser().getId(), ids);
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.CONTACT;
    }

    public Optional<Contact> findContactByIdAndUser(Long id, User user) {
        return this.repository.findByIdAndUserId(id, user.getId());
    }

    public ContactDataModel saveByUser(ContactDataModel contactDataModel, User user) throws AuthNullException, ConvertModelToEntityException {
        final Auth fakeAuth = this.getFakeAuth(user);

        return this.internalSave(contactDataModel, fakeAuth);
    }

    public void deleteByUser(SynchronizableDeleteModel<Long> model, User user) throws AuthNullException {
        final Auth fakeAuth = this.getFakeAuth(user);

        this.internalDelete(model, fakeAuth);
    }

    public List<Contact> findAllByEssentialContactId(Long essentialContactId) {
        return this.repository.findAllByEssentialContactId(essentialContactId);
    }

    private Auth getFakeAuth(User user) {
        final Auth fakeAuth = new Auth();
        fakeAuth.setUser(user);

        return fakeAuth;
    }

}
