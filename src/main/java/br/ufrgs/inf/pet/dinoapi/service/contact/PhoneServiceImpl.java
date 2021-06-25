package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialPhone;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.contacts.PhoneDataModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.EssentialPhoneRepository;
import br.ufrgs.inf.pet.dinoapi.repository.contact.PhoneRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.async.AsyncPhoneService;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.SynchronizableQueueMessageService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PhoneServiceImpl extends SynchronizableServiceImpl<Phone, Long, PhoneDataModel, PhoneRepository> {
    private final ContactServiceImpl contactService;
    private final AsyncPhoneService asyncPhoneService;
    private final EssentialPhoneRepository essentialPhoneRepository;

    @Autowired
    public PhoneServiceImpl(PhoneRepository repository, AuthServiceImpl authService,
                            SynchronizableQueueMessageService<Long, PhoneDataModel> synchronizableQueueMessageService,
                            ClockServiceImpl clockService, LogAPIErrorServiceImpl logAPIErrorService,
                            ContactServiceImpl contactService, AsyncPhoneService asyncPhoneService,
                            EssentialPhoneRepository essentialPhoneRepository) {
        super(repository, authService, clockService, synchronizableQueueMessageService, logAPIErrorService);
        this.contactService = contactService;
        this.asyncPhoneService = asyncPhoneService;
        this.essentialPhoneRepository = essentialPhoneRepository;
    }

    @Override
    public PhoneDataModel convertEntityToModel(Phone entity) {
        final PhoneDataModel model = new PhoneDataModel();
        model.setNumber(entity.getNumber());
        model.setType(entity.getType());
        model.setContactId(entity.getContact().getId());
        final EssentialPhone ePhone = entity.getEssentialPhone();
        if (ePhone != null) {
            model.setEssentialPhoneId(ePhone.getId());
        }

        return model;
    }

    @Override
    public Phone convertModelToEntity(PhoneDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        if (auth != null) {
            final Long contactId = model.getContactId();

            if (contactId == null) throw new ConvertModelToEntityException(ContactsConstants.PHONE_WITHOUT_CONTACT);

            final Phone entity = new Phone();
            entity.setNumber(model.getNumber());
            entity.setType(model.getType());

            final Long essentialContactId = model.getEssentialPhoneId();

            if (essentialContactId != null) {
                final Optional<EssentialPhone> ePhoneSearch = essentialPhoneRepository.findById(essentialContactId);
                ePhoneSearch.ifPresent(entity::setEssentialPhone);
            }

            searchContact(entity, contactId, auth);

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
        if (auth == null) throw new AuthNullException();

        return this.repository.findByIdAndUserId(id, auth.getUser().getId());
    }

    @Override
    public Optional<Phone> findEntityByIdThatUserCanEdit(Long id, Auth auth) throws AuthNullException {
        if (auth == null) throw new AuthNullException();

        return this.repository.findByIdAndUserId(id, auth.getUser().getId());
    }

    @Override
    public List<Phone> findEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        if (auth == null) throw new AuthNullException();

        return this.repository.findAllByUserId(auth.getUser().getId());
    }

    @Override
    public List<Phone> findEntitiesByIdThatUserCanEdit(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) throw new AuthNullException();

        return this.repository.findAllByIdAndUserId(ids, auth.getUser().getId());
    }

    @Override
    public List<Phone> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) throw new AuthNullException();

        return this.repository.findAllByIdAndUserIdExcludingIds(ids, auth.getUser().getId());
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.PHONE;
    }

    @Override
    protected void afterDataCreated(Phone entity, Auth auth) {
        asyncPhoneService.updateGoogleContactPhones(auth.getUser(), entity);
    }

    @Override
    protected void afterDataUpdated(Phone entity, Auth auth) {
        asyncPhoneService.updateGoogleContactPhones(auth.getUser(), entity);
    }

    @Override
    protected void afterDataDeleted(Phone entity, Auth auth) {
        asyncPhoneService.updateGoogleContactPhones(auth.getUser(), entity);
    }

    public List<Phone> findAllByEssentialPhone(EssentialPhone essentialPhone) {
        return this.repository.findAllByEssentialPhoneId(essentialPhone.getId());
    }

    public void saveByUser(PhoneDataModel contactDataModel, User user) throws AuthNullException, ConvertModelToEntityException {
        final Auth fakeAuth = this.getFakeAuth(user);

        this.internalSave(contactDataModel, fakeAuth);
    }

    public Phone saveDirectly(Phone phone) {
        return this.repository.save(phone);
    }

    public List<Phone> saveDirectly(List<Phone> phones) {
        return Lists.newArrayList(this.repository.saveAll(phones));
    }

    public void deleteByUser(SynchronizableDeleteModel<Long> model, User user) throws AuthNullException {
        final Auth fakeAuth = new Auth();
        fakeAuth.setUser(user);

        this.internalDelete(model, fakeAuth);
    }

    public void deleteAllByUser(List<SynchronizableDeleteModel<Long>> models, User user) throws AuthNullException {
        final Auth fakeAuth = new Auth();
        fakeAuth.setUser(user);

        this.internalDeleteAll(models, fakeAuth);
    }

    public List<Phone> findAllByContactId(Long contactId) {
        return repository.findAllByContactId(contactId);
    }


    private void searchContact(Phone entity, Long id, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        final Optional<Contact> contactSearch = contactService.findEntityByIdThatUserCanRead(id, auth);

        if (contactSearch.isPresent()) {
            entity.setContact(contactSearch.get());
        } else throw new ConvertModelToEntityException(ContactsConstants.PHONE_INVALID_CONTACT);
    }

    private Auth getFakeAuth(User user) {
        final Auth fakeAuth = new Auth();
        fakeAuth.setUser(user);

        return fakeAuth;
    }
}
