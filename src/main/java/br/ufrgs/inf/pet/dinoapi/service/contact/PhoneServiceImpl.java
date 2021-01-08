package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.contacts.PhoneDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.ContactRepository;
import br.ufrgs.inf.pet.dinoapi.repository.contact.EssentialContactRepository;
import br.ufrgs.inf.pet.dinoapi.repository.contact.PhoneRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
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
public class PhoneServiceImpl extends SynchronizableServiceImpl<Phone, Long, Integer, PhoneDataModel, PhoneRepository> {

    private final ContactRepository contactRepository;
    private final EssentialContactRepository essentialContactRepository;

    @Autowired
    public PhoneServiceImpl(PhoneRepository repository, AuthServiceImpl authService,
                            SynchronizableQueueMessageServiceImpl<Long, Integer, PhoneDataModel> synchronizableQueueMessageService,
                            ClockServiceImpl clockService, LogAPIErrorServiceImpl logAPIErrorService,
                            ContactRepository contactRepository, EssentialContactRepository essentialContactRepository) {
        super(repository, authService, clockService, synchronizableQueueMessageService, logAPIErrorService);

        this.contactRepository = contactRepository;
        this.essentialContactRepository = essentialContactRepository;
    }

    @Override
    public PhoneDataModel convertEntityToModel(Phone entity) {
        PhoneDataModel model = new PhoneDataModel();
        model.setNumber(entity.getNumber());
        model.setType(entity.getType());
        Contact contact = entity.getContact();
        if (contact != null) {
            model.setContactId(contact.getId());
        } else {
            EssentialContact essentialContact = entity.getEssentialContact();
            if(essentialContact != null) {
                model.setEssentialContactId(entity.getEssentialContact().getId());
            }
        }
        return model;
    }

    @Override
    public Phone convertModelToEntity(PhoneDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        if (auth != null) {
            Phone entity = new Phone();
            entity.setNumber(model.getNumber());
            entity.setType(model.getType());
            Long contactId = model.getContactId();
            Long essentialContactId = model.getEssentialContactId();

            if(contactId != null) {

                if(essentialContactId != null) throw new ConvertModelToEntityException("Pode não");

                searchContact(entity, contactId);

            } else if (essentialContactId != null) {
                searchEssentialContact(entity, essentialContactId);

            } else throw new ConvertModelToEntityException("Nem Contato nem Contato Essencial encontrados");

            return entity;
        }

        throw new AuthNullException();
    }

    @Override
    public void updateEntity(Phone entity, PhoneDataModel model, Auth auth) {
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
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.PHONE;
    }

    private void searchContact(Phone entity, Long id) throws ConvertModelToEntityException {
        final Optional<Contact> contactSearch = contactRepository.findById(id);

        if(contactSearch.isPresent()) {
            entity.setContact(contactSearch.get());
        } else throw new ConvertModelToEntityException("Contato não encontrado");
    }

    private void searchEssentialContact(Phone entity, Long id) throws ConvertModelToEntityException {
        final Optional<EssentialContact> essentialContactSearch = essentialContactRepository.findById(id);

        if(essentialContactSearch.isPresent()) {
            entity.setEssentialContact(essentialContactSearch.get());
        } else throw new ConvertModelToEntityException("Contato Essencial não encontrado");
    }


}
