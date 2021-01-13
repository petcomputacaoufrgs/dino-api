package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.contacts.PhoneDataModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.EssentialContactRepository;
import br.ufrgs.inf.pet.dinoapi.repository.contact.PhoneRepository;
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
import java.util.stream.Collectors;

@Service
public class PhoneServiceImpl extends SynchronizableServiceImpl<Phone, Long, Integer, PhoneDataModel, PhoneRepository> {
    private final ContactServiceImpl contactService;
    private final EssentialContactRepository essentialContactRepository;

    @Autowired
    public PhoneServiceImpl(PhoneRepository repository, OAuthServiceImpl authService,
                            SynchronizableQueueMessageServiceImpl<Long, Integer, PhoneDataModel> synchronizableQueueMessageService,
                            ClockServiceImpl clockService, LogAPIErrorServiceImpl logAPIErrorService,
                            ContactServiceImpl contactService, EssentialContactRepository essentialContactRepository) {
        super(repository, authService, clockService, synchronizableQueueMessageService, logAPIErrorService);
        this.contactService = contactService;
        this.essentialContactRepository = essentialContactRepository;
    }

    @Override
    public PhoneDataModel convertEntityToModel(Phone entity) {
        final PhoneDataModel model = new PhoneDataModel();
        model.setNumber(entity.getNumber());
        model.setType(entity.getType());
        final Contact contact = entity.getContact();
        if (contact != null) {
            model.setContactId(contact.getId());

            final Phone originalEPhone = entity.getOriginalEssentialPhone();
            if (originalEPhone != null) {
                model.setOriginalEssentialPhoneId(originalEPhone.getId());
            }
        } else {
            final EssentialContact essentialContact = entity.getEssentialContact();
            if(essentialContact != null) {
                model.setEssentialContactId(entity.getEssentialContact().getId());
            }
        }
        return model;
    }

    @Override
    public Phone convertModelToEntity(PhoneDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        if (auth != null) {
            final Phone entity = new Phone();
            entity.setNumber(model.getNumber());
            entity.setType(model.getType());
            final Long contactId = model.getContactId();
            final Long essentialContactId = model.getEssentialContactId();

            if(contactId != null) {
                if(essentialContactId != null) throw new ConvertModelToEntityException(ContactsConstants.PHONE_WITH_CONTACT_AND_ECONTACT);
                searchContact(entity, contactId, auth);

                final Long originalEssentialPhoneId = model.getOriginalEssentialPhoneId();
                if(originalEssentialPhoneId != null) {
                    final Optional<Phone> originalEPhoneSearch = this.findById(originalEssentialPhoneId);

                    originalEPhoneSearch.ifPresent(entity::setOriginalEssentialPhone);
                }
            } else if (essentialContactId != null) {
                searchEssentialContact(entity, essentialContactId);
            } else throw new ConvertModelToEntityException(ContactsConstants.PHONE_WITHOUT_CONTACT_OR_ECONTACT);

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
    public List<Phone> getEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByUserId(auth.getUser().getId());
    }

    @Override
    public List<Phone> getEntitiesByIdsAndUserAuth(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByIdAndUserId(ids, auth.getUser().getId());
    }

    @Override
    public List<Phone> getEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByContactUserIdExceptIds(auth.getUser().getId(), ids);
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.PHONE;
    }

    @Override
    protected void onDataCreated(PhoneDataModel model) throws AuthNullException, ConvertModelToEntityException {
        final Long essentialContactId = model.getEssentialContactId();

        if (essentialContactId == null) return;

        final List<Contact> contacts = contactService.findAllByEssentialContactId(essentialContactId);
        final List<Phone> phones = repository.findAllByEssentialContactId(essentialContactId);

        for (Contact contact : contacts) {
            final User user = contact.getUser();

            if (user.getUserAppSettings().getIncludeEssentialContact()) {
                final Auth fakeAuth = new Auth();
                fakeAuth.setUser(user);

                final List<PhoneDataModel> phoneDataModels = phones.stream().map(phone -> {
                    final PhoneDataModel phoneDataModel = new PhoneDataModel();
                    phoneDataModel.setType(phone.getType());
                    phoneDataModel.setNumber(phone.getNumber());
                    phoneDataModel.setContactId(contact.getId());
                    phoneDataModel.setLastUpdate(clock.getUTCZonedDateTime());
                    phoneDataModel.setOriginalEssentialPhoneId(phone.getId());

                    return phoneDataModel;
                }).collect(Collectors.toList());

                this.internalSaveAll(phoneDataModels, fakeAuth);
            }
        }
    }

    @Override
    protected void onDataUpdated(PhoneDataModel model, Phone entity) throws AuthNullException, ConvertModelToEntityException {
        final EssentialContact essentialContact = entity.getEssentialContact();

        if (essentialContact == null) return;

        final List<Phone> phones = repository.findAllByOriginalEssentialPhone(entity);

        for (Phone phone : phones) {
            final Auth fakeAuth = new Auth();
            fakeAuth.setUser(phone.getContact().getUser());

            final PhoneDataModel phoneDataModel = new PhoneDataModel();
            phoneDataModel.setType(phone.getType());
            phoneDataModel.setNumber(phone.getNumber());
            phoneDataModel.setContactId(phone.getContact().getId());
            phoneDataModel.setLastUpdate(clock.getUTCZonedDateTime());
            phoneDataModel.setId(phone.getId());
            phoneDataModel.setOriginalEssentialPhoneId(entity.getId());
            this.internalSave(phoneDataModel, fakeAuth);
        }
    }

    @Override
    protected void onDataDeleted(Phone entity) throws AuthNullException {
        final List<Phone> phones = repository.findAllByOriginalEssentialPhone(entity);

        for (Phone phone : phones) {
            final Auth fakeAuth = new Auth();
            fakeAuth.setUser(phone.getContact().getUser());

            final SynchronizableDeleteModel<Long> model = new SynchronizableDeleteModel<>();
            model.setLastUpdate(clock.getUTCZonedDateTime());
            model.setId(phone.getId());

            this.internalDelete(model, fakeAuth);
        }
    }

    public void deleteAllByUser(List<SynchronizableDeleteModel<Long>> model, User user) throws AuthNullException {
        final Auth fakeAuth = new Auth();
        fakeAuth.setUser(user);

        this.internalDeleteAll(model, fakeAuth);
    }

    public Optional<Phone> findById(Long id) {
        return repository.findById(id);
    }

    public List<Phone> findAllByContactId(Long contactId) {
        return repository.findAllByContactId(contactId);
    }

    private void searchContact(Phone entity, Long id, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        final Optional<Contact> contactSearch = contactService.getEntityByIdAndUserAuth(id, auth);

        if(contactSearch.isPresent()) {
            entity.setContact(contactSearch.get());
        } else throw new ConvertModelToEntityException(ContactsConstants.PHONE_INVALID_CONTACT);
    }

    private void searchEssentialContact(Phone entity, Long id) throws ConvertModelToEntityException {
        final Optional<EssentialContact> essentialContactSearch = essentialContactRepository.findById(id);

        if(essentialContactSearch.isPresent()) {
            entity.setEssentialContact(essentialContactSearch.get());
        } else throw new ConvertModelToEntityException(ContactsConstants.PHONE_INVALID_ECONTACT);
    }
}
