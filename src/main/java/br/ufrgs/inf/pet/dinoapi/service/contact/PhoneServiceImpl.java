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
import br.ufrgs.inf.pet.dinoapi.service.contact.async.AsyncPhoneService;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.SynchronizableQueueMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PhoneServiceImpl extends SynchronizableServiceImpl<Phone, Long, PhoneDataModel, PhoneRepository> {
    private final ContactServiceImpl contactService;
    private final EssentialContactRepository essentialContactRepository;
    private final AsyncPhoneService asyncPhoneService;

    @Autowired
    public PhoneServiceImpl(PhoneRepository repository, OAuthServiceImpl authService,
                            SynchronizableQueueMessageService<Long, PhoneDataModel> synchronizableQueueMessageService,
                            ClockServiceImpl clockService, LogAPIErrorServiceImpl logAPIErrorService,
                            ContactServiceImpl contactService, EssentialContactRepository essentialContactRepository,
                            AsyncPhoneService asyncPhoneService) {
        super(repository, authService, clockService, synchronizableQueueMessageService, logAPIErrorService);
        this.contactService = contactService;
        this.essentialContactRepository = essentialContactRepository;
        this.asyncPhoneService = asyncPhoneService;
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
            if (essentialContact != null) {
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

            if (contactId != null) {
                if (essentialContactId != null)
                    throw new ConvertModelToEntityException(ContactsConstants.PHONE_WITH_CONTACT_AND_ECONTACT);
                searchContact(entity, contactId, auth);

                final Long originalEssentialPhoneId = model.getOriginalEssentialPhoneId();
                if (originalEssentialPhoneId != null) {
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
    public Optional<Phone> findEntityByIdThatUserCanRead(Long id, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        final Optional<Phone> phone = this.repository.findByIdAndUserId(id, auth.getUser().getId());

        if (phone.isPresent()) {
            return phone;
        }

        return this.repository.findEssentialById(id);
    }

    @Override
    public Optional<Phone> findEntityByIdThatUserCanEdit(Long id, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findByIdAndUserId(id, auth.getUser().getId());
    }

    @Override
    public List<Phone> findEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        final List<Phone> userPhones = this.repository.findAllByUserId(auth.getUser().getId());
        final List<Phone> essentialPhones = this.repository.findAllEssentialPhones();

        userPhones.addAll(essentialPhones);

        return userPhones;
    }

    @Override
    public List<Phone> findEntitiesByIdThatUserCanEdit(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByIdAndUserId(ids, auth.getUser().getId());
    }

    @Override
    public List<Phone> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        final List<Phone> userPhones = this.repository.findAllByIdAndUserIdExcludingIds(ids, auth.getUser().getId());
        final List<Phone> essentialPhones = this.repository.findAllEssentialPhonesExcludingIds(ids);

        userPhones.addAll(essentialPhones);

        return userPhones;
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.PHONE;
    }

    @Override
    protected void onDataCreated(PhoneDataModel model) {
        asyncPhoneService.createPhoneOnGoogleAPI(model, (phoneDataModels, auth) -> {
            try {
                return this.internalSaveAll(phoneDataModels, auth);
            } catch (Exception e) {
                this.logAPIError(e);
            }
            return new ArrayList<>();
        });
    }

    @Override
    protected void onDataUpdated(PhoneDataModel model, Phone entity) {
        asyncPhoneService.updatePhoneOnGoogleAPI(entity, (phoneDataModel, auth) -> {
            try {
                return this.internalSave(phoneDataModel, auth);
            } catch (Exception e) {
                this.logAPIError(e);
            }
            return null;
        });
    }

    @Override
    protected void onDataDeleted(Phone entity) {
        final List<Phone> ePhones = this.repository.findAllByOriginalEssentialPhone(entity.getId());

        ePhones.forEach(phone -> phone.setOriginalEssentialPhone(null));
        this.repository.saveAll(ePhones);

        asyncPhoneService.deletePhonesOnGoogleAPI(ePhones, (model, auth) -> {
            try {
                this.internalDelete(model, auth);
            } catch (Exception e) {
                this.logAPIError(e);
            }
            return null;
        });
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

    public List<String> findAllPhoneNumbersByContactId(Long contactId) {
        return repository.findAllPhoneNumbersByContactId(contactId);
    }

    private void searchContact(Phone entity, Long id, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        final Optional<Contact> contactSearch = contactService.findEntityByIdThatUserCanRead(id, auth);

        if (contactSearch.isPresent()) {
            entity.setContact(contactSearch.get());
        } else throw new ConvertModelToEntityException(ContactsConstants.PHONE_INVALID_CONTACT);
    }

    private void searchEssentialContact(Phone entity, Long id) throws ConvertModelToEntityException {
        final Optional<EssentialContact> essentialContactSearch = essentialContactRepository.findById(id);

        if (essentialContactSearch.isPresent()) {
            entity.setEssentialContact(essentialContactSearch.get());
        } else throw new ConvertModelToEntityException(ContactsConstants.PHONE_INVALID_ECONTACT);
    }
}
