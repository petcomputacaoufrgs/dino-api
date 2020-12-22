package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.contacts.ContactModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.ContactRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.synchronizable.SynchronizableQueueMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl extends SynchronizableServiceImpl<Contact, Long, Integer, ContactModel, ContactRepository> {

    @Autowired
    public ContactServiceImpl(ContactRepository repository, AuthServiceImpl authService,
                              SynchronizableQueueMessageServiceImpl<Long, Integer, ContactModel> synchronizableQueueMessageService) {
        super(repository, authService, synchronizableQueueMessageService);
    }

    @Override
    public ContactModel convertEntityToModel(Contact entity) {
        ContactModel model = new ContactModel();
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setColor(entity.getColor());
        return model;
    }

    @Override
    public Contact convertModelToEntity(ContactModel model) throws ConvertModelToEntityException {
        Contact contact = new Contact();
        contact.setName(model.getName());
        contact.setDescription(model.getDescription());
        contact.setColor(model.getColor());
        contact.setUser(this.getUser());
        return contact;
    }

    @Override
    public void updateEntity(Contact entity, ContactModel model) throws ConvertModelToEntityException {
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setColor(model.getColor());
    }

    @Override
    public Optional<Contact> getEntityByIdAndUser(Long id, User user) {
        return this.repository.findByIdAndUserId(id, user.getId());
    }

    @Override
    public List<Contact> getEntitiesByUserId(User user) {
        return this.repository.findAllByUserId(user.getId());
    }

    @Override
    public List<Contact> getEntitiesByIdsAndUserId(List<Long> ids, User user) {
        return this.repository.findAllByIdsAndUserId(ids, user.getId());
    }

    @Override
    public List<Contact> getEntitiesByUserIdExceptIds(User user, List<Long> ids) {
        return this.repository.findAllByUserIdExceptIds(user.getId(), ids);
    }

    @Override
    public WebSocketDestinationsEnum getUpdateWebSocketDestination() {
        return WebSocketDestinationsEnum.CONTACT_UPDATE;
    }

    @Override
    public WebSocketDestinationsEnum getDeleteWebSocketDestination() {
        return WebSocketDestinationsEnum.CONTACT_DELETE;
    }

    public Optional<Contact> findContactByIdAndUser(Long id, User user) {
        return this.repository.findByIdAndUserId(id, user.getId());
    }
}
