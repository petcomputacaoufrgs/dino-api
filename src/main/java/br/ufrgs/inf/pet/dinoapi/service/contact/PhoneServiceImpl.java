package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.contacts.PhoneModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.PhoneRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.SynchronizableQueueMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PhoneServiceImpl extends SynchronizableServiceImpl<Phone, Long, Integer, PhoneModel, PhoneRepository> {

    private final ContactServiceImpl contactService;

    @Autowired
    public PhoneServiceImpl(PhoneRepository repository, ContactServiceImpl contactService, AuthServiceImpl authService,
                            SynchronizableQueueMessageServiceImpl<Long, Integer, PhoneModel> synchronizableQueueMessageService) {
        super(repository, authService, synchronizableQueueMessageService);
        this.contactService = contactService;
    }

    @Override
    public PhoneModel convertEntityToModel(Phone entity) {
        PhoneModel model = new PhoneModel();
        model.setNumber(entity.getNumber());
        model.setType(entity.getType());
        model.setContactId(entity.getContact().getId());
        return model;
    }

    @Override
    public Phone convertModelToEntity(PhoneModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        if (auth != null) {
            final User user = auth.getUser();
            final Optional<Contact> contactSearch = contactService.findContactByIdAndUser(model.getContactId(), user);

            if (contactSearch.isPresent()) {
                Phone phone = new Phone();
                phone.setNumber(model.getNumber());
                phone.setType(model.getType());
                phone.setContact(contactSearch.get());

                return phone;
            }

            throw new ConvertModelToEntityException(ContactsConstants.INVALID_CONTACT);
        }

        throw new AuthNullException();
    }

    @Override
    public void updateEntity(Phone entity, PhoneModel model, Auth auth) {
        entity.setNumber(model.getNumber());
        entity.setType(model.getType());
    }

    @Override
    public Optional<Phone> getEntityByIdAndUserAuth(Long id, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findByIdAndContactUserId(id, auth.getUser().getId());
    }

    @Override
    public List<Phone> getEntitiesByUserAuth(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByContactUserId(auth.getUser().getId());
    }

    @Override
    public List<Phone> getEntitiesByIdsAndUserAuth(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByIdAndContactUserId(ids, auth.getUser().getId());
    }

    @Override
    public List<Phone> getEntitiesByUserAuthExceptIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByContactUserIdExceptIds(auth.getUser().getId(), ids);
    }

    @Override
    public WebSocketDestinationsEnum getUpdateWebSocketDestination() {
        return WebSocketDestinationsEnum.PHONE_UPDATE;
    }

    @Override
    public WebSocketDestinationsEnum getDeleteWebSocketDestination() {
        return WebSocketDestinationsEnum.PHONE_DELETE;
    }
}
