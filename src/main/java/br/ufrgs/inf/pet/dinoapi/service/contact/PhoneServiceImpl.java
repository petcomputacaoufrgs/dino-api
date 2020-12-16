package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.contacts.PhoneModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.PhoneRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.GenericQueueMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PhoneServiceImpl extends SynchronizableServiceImpl<Phone, Long, Integer, PhoneModel, PhoneRepository> {

    private final ContactServiceImpl contactService;

    @Autowired
    public PhoneServiceImpl(PhoneRepository repository, ContactServiceImpl contactService, AuthServiceImpl authService, GenericQueueMessageServiceImpl genericQueueMessageService) {
        super(repository, authService, genericQueueMessageService);
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
    public Phone convertModelToEntity(PhoneModel model, User user) throws ConvertModelToEntityException  {
        Optional<Contact> contactSearch = contactService.findContactByIdAndUser(model.getContactId(), user);

        if (contactSearch.isPresent()) {
            Phone phone = new Phone();
            phone.setNumber(model.getNumber());
            phone.setType(model.getType());
            phone.setContact(contactSearch.get());

            return phone;
        }

        throw new ConvertModelToEntityException(ContactsConstants.INVALID_CONTACT);
    }

    @Override
    public void updateEntity(Phone entity, PhoneModel model) {
        entity.setNumber(model.getNumber());
        entity.setType(model.getType());
    }

    @Override
    public Optional<Phone> getEntityByIdAndUser(Long id, User user) {
        return this.repository.findByIdAndContactUserId(id, user.getId());
    }

    @Override
    public List<Phone> getEntitiesByUserId(User user) {
        return this.repository.findAllByContactUserId(user.getId());
    }

    @Override
    public List<Phone> getEntitiesByIdsAndUserId(List<Long> ids, User user) {
        return this.repository.findAllByIdAndContactUserId(ids, user.getId());
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
