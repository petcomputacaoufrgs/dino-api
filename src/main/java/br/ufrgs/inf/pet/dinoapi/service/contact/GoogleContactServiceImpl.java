package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.constants.GoogleContactConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.GoogleContact;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.contacts.GoogleContactDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.GoogleContactRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.OAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.async.AsyncGoogleContactImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.SynchronizableQueueMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GoogleContactServiceImpl extends SynchronizableServiceImpl<GoogleContact, Long, GoogleContactDataModel, GoogleContactRepository> {

    private final ContactServiceImpl contactService;

    private final AsyncGoogleContactImpl asyncGoogleContact;

    @Autowired
    public GoogleContactServiceImpl(GoogleContactRepository repository, OAuthServiceImpl authService, ContactServiceImpl contactService,
                                    SynchronizableQueueMessageServiceImpl<Long, GoogleContactDataModel> synchronizableQueueMessageService,
                                    ClockServiceImpl clockService, LogAPIErrorServiceImpl logAPIErrorService,
                                    AsyncGoogleContactImpl asyncGoogleContact) {
        super(repository, authService, clockService, synchronizableQueueMessageService, logAPIErrorService);
        this.contactService = contactService;
        this.asyncGoogleContact = asyncGoogleContact;
    }

    @Override
    public GoogleContactDataModel convertEntityToModel(GoogleContact entity) {
        final GoogleContactDataModel model = new GoogleContactDataModel();
        model.setResourceName(entity.getResourceName());
        model.setContactId(entity.getContact().getId());
        return model;
    }

    @Override
    public GoogleContact convertModelToEntity(GoogleContactDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        if (auth != null) {
            final User user = auth.getUser();
            final Optional<Contact> contactSearch = contactService.findContactByIdAndUser(model.getContactId(), user);

            if (contactSearch.isPresent()) {
                GoogleContact googleContact = new GoogleContact();
                googleContact.setContact(contactSearch.get());
                googleContact.setUser(user);
                if (model.getResourceName() != null) {
                    googleContact.setResourceName(model.getResourceName());
                }
                return googleContact;
            }

            throw new ConvertModelToEntityException(GoogleContactConstants.INVALID_CONTACT_ERROR);
        }

        throw new AuthNullException();
    }

    @Override
    public void updateEntity(GoogleContact entity, GoogleContactDataModel model, Auth auth) throws ConvertModelToEntityException {
        if (model.getResourceName() != null) {
            entity.setResourceName(model.getResourceName());
        }
    }

    @Override
    protected void onDataCreated(GoogleContactDataModel model) {
        asyncGoogleContact.createContactOnGoogleAPI(model, (phoneDataModel, auth) -> {
            try {
                return this.internalSave(phoneDataModel, auth, false);
            } catch (Exception e) {
                this.logAPIError(e);
            }
            return null;
        });
    }

    @Override
    protected void onDataUpdated(GoogleContactDataModel model, GoogleContact entity) {
        asyncGoogleContact.updateContactOnGoogleAPI(entity, (phoneDataModel, auth) -> {
            try {
                return this.internalSave(phoneDataModel, auth, false);
            } catch (Exception e) {
                this.logAPIError(e);
            }
            return null;
        });
    }

    @Override
    protected void onDataDeleted(GoogleContact entity) {
        asyncGoogleContact.deleteContactOnGoogleAPI(entity, (model, auth) -> {
            try {
                this.internalDelete(model, auth, false);
            } catch (Exception e) {
                this.logAPIError(e);
            }
            return null;
        });
    }

    @Override
    public Optional<GoogleContact> findEntityByIdThatUserCanRead(Long id, Auth auth) throws AuthNullException {
        return this.findByIdAndUser(id, auth);
    }

    @Override
    public Optional<GoogleContact> findEntityByIdThatUserCanEdit(Long id, Auth auth) throws AuthNullException {
        return this.findByIdAndUser(id, auth);
    }

    private Optional<GoogleContact> findByIdAndUser(Long id, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return repository.findByIdAndUserId(id, auth.getUser().getId());
    }

    @Override
    public List<GoogleContact> findEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return repository.findAllByUserId(auth.getUser().getId());
    }

    @Override
    public List<GoogleContact> findEntitiesByIdThatUserCanEdit(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return repository.findAllByIdAndUserId(ids, auth.getUser().getId());
    }

    @Override
    public List<GoogleContact> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return repository.findAllByUserIdExcludingIds(auth.getUser().getId(), ids);
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.GOOGLE_CONTACT;
    }

    public void saveByUser(GoogleContactDataModel googleContactDataModel, User user) throws AuthNullException, ConvertModelToEntityException {
        final Auth fakeAuth = new Auth();
        fakeAuth.setUser(user);

        this.internalSave(googleContactDataModel, fakeAuth, false);
    }

    public Optional<GoogleContact> findByContactId(Long contactId) {
        return repository.findByContactId(contactId);
    }
}
