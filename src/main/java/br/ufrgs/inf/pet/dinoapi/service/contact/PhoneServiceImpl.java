package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.contacts.PhoneModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.PhoneRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.GenericMessageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhoneServiceImpl extends SynchronizableServiceImpl<Phone, Long, PhoneModel, PhoneRepository> {

    public PhoneServiceImpl(PhoneRepository repository, AuthServiceImpl authService, GenericMessageService genericMessageService) {
        super(repository, authService, genericMessageService);
    }

    @Override
    public PhoneModel convertEntityToModel(Phone entity) {
        PhoneModel model = new PhoneModel();
        model.setNumber(entity.getNumber());
        model.setType(entity.getType());
        return model;
    }

    @Override
    public Phone convertModelToEntity(PhoneModel model, User user) throws ConvertModelToEntityException {
        return null;
    }

    //@Override
    public Phone convertModelToEntity(PhoneModel model, Contact contact) throws ConvertModelToEntityException {
        Phone phone = new Phone();
        phone.setNumber(model.getNumber());
        phone.setType(model.getType());
        phone.setContact(contact);
        return phone;
    }

    @Override
    public void updateEntity(Phone entity, PhoneModel model) throws ConvertModelToEntityException {
        entity.setNumber(model.getNumber());
        entity.setType(model.getType());
    }

    @Override
    public Optional<Phone> getEntityByIdAndUser(Long id, User user) {
        return this.repository.findByIdAndUserId(id, user.getId());
    }

    @Override
    public List<Phone> getEntitiesByUserId(User user) {
        return this.repository.findAllByUserId(user.getId());
    }

    @Override
    public List<Phone> getEntitiesByIdsAndUserId(List<Long> ids, User user) {
        return this.repository.findAllByIdAndUserId(ids, user.getId());
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
