package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialPhone;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import br.ufrgs.inf.pet.dinoapi.enumerable.PermissionEnum;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.contacts.EssentialPhoneDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.EssentialPhoneRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.async.AsyncEssentialPhoneService;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.SynchronizableStaffQueueMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EssentialPhoneServiceImpl
        extends SynchronizableServiceImpl<EssentialPhone, Long, EssentialPhoneDataModel, EssentialPhoneRepository> {

    private final EssentialContactServiceImpl essentialContactService;
    private final AsyncEssentialPhoneService asyncEssentialPhoneService;
    private final PhoneServiceImpl phoneService;

    @Autowired
    public EssentialPhoneServiceImpl(EssentialPhoneRepository repository, AuthServiceImpl authService,
                                     ClockServiceImpl clock, EssentialContactServiceImpl essentialContactService,
                                     SynchronizableStaffQueueMessageService<Long, EssentialPhoneDataModel> synchronizableStaffQueueMessageService,
                                     LogAPIErrorServiceImpl logAPIErrorService, AsyncEssentialPhoneService asyncEssentialPhoneService,
                                     PhoneServiceImpl phoneService) {
        super(repository, authService, clock, synchronizableStaffQueueMessageService, logAPIErrorService);
        this.essentialContactService = essentialContactService;
        this.asyncEssentialPhoneService = asyncEssentialPhoneService;
        this.phoneService = phoneService;
    }

    @Override
    public List<PermissionEnum> getNecessaryPermissionsToEdit() {
        final List<PermissionEnum> authorities = new ArrayList<>();
        authorities.add(PermissionEnum.ADMIN);
        authorities.add(PermissionEnum.STAFF);
        return authorities;
    }

    @Override
    public EssentialPhoneDataModel convertEntityToModel(EssentialPhone entity) {
        final EssentialContact essentialContact = entity.getEssentialContact();
        final EssentialPhoneDataModel model = new EssentialPhoneDataModel();
        model.setNumber(entity.getNumber());
        model.setType(entity.getType());
        model.setEssentialContactId(essentialContact.getId());
        return model;
    }

    @Override
    public EssentialPhone convertModelToEntity(EssentialPhoneDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        if (auth != null) {
            final Optional<EssentialContact> essentialContactSearch =
                    this.essentialContactService.findEntityByIdThatUserCanRead(model.getEssentialContactId(), auth);
            if (essentialContactSearch.isPresent()) {
                final EssentialContact essentialContact = essentialContactSearch.get();
                final EssentialPhone entity = new EssentialPhone();
                entity.setNumber(model.getNumber());
                entity.setType(model.getType());
                entity.setEssentialContact(essentialContact);

                return entity;
            }
            throw new ConvertModelToEntityException(ContactsConstants.ESSENTIAL_CONTACT_NOT_FOUND);
        }

        throw new AuthNullException();
    }

    @Override
    public void updateEntity(EssentialPhone entity, EssentialPhoneDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        if (auth != null) {
            entity.setNumber(model.getNumber());
            entity.setType(model.getType());
        } else {
            throw new AuthNullException();
        }
    }

    @Override
    public Optional<EssentialPhone> findEntityByIdThatUserCanRead(Long id, Auth auth) throws AuthNullException {
        return this.repository.findById(id);
    }

    @Override
    public Optional<EssentialPhone> findEntityByIdThatUserCanEdit(Long id, Auth auth) throws AuthNullException {
        return this.repository.findById(id);
    }

    @Override
    public List<EssentialPhone> findEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        return this.repository.findAll();
    }

    @Override
    public List<EssentialPhone> findEntitiesByIdThatUserCanEdit(List<Long> ids, Auth auth) throws AuthNullException {
        return this.repository.findAllById(ids);
    }

    @Override
    public List<EssentialPhone> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) throws AuthNullException {
        return this.repository.findAllExcludingIds(ids);
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.ESSENTIAL_PHONE;
    }


    @Override
    protected void afterDataCreated(EssentialPhone entity, Auth auth) {
        asyncEssentialPhoneService.createUsersPhones(entity);
    }

    @Override
    protected void afterDataUpdated(EssentialPhone entity, Auth auth) {
        asyncEssentialPhoneService.updateUsersPhones(entity);
    }

    @Override
    protected void beforeDataDeleted(EssentialPhone entity, Auth auth) {
        List<Phone> phones = phoneService.findAllByEssentialPhone(entity);

        phones.forEach(phone -> phone.setEssentialPhone(null));

        phones = phoneService.saveDirectly(phones);

        asyncEssentialPhoneService.deleteUsersPhones(phones);
    }
}
