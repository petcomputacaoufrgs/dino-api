package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;
import br.ufrgs.inf.pet.dinoapi.enumerable.PermissionEnum;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.contacts.EssentialContactDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.ContactRepository;
import br.ufrgs.inf.pet.dinoapi.repository.contact.EssentialContactRepository;
import br.ufrgs.inf.pet.dinoapi.repository.treatment.TreatmentRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.async.AsyncEssentialContactService;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.topic.SynchronizableTopicMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EssentialContactServiceImpl extends
        SynchronizableServiceImpl<EssentialContact, Long, EssentialContactDataModel, EssentialContactRepository> {

    private final TreatmentRepository treatmentRepository;
    private final AsyncEssentialContactService asyncEssentialContactService;
    private final ContactRepository contactRepository;

    @Autowired
    public EssentialContactServiceImpl(TreatmentRepository treatmentRepository, EssentialContactRepository repository,
                                       AuthServiceImpl authService, ClockServiceImpl clock, LogAPIErrorServiceImpl logAPIErrorService,
                                       SynchronizableTopicMessageService<Long, EssentialContactDataModel> synchronizableTopicMessageService,
                                       AsyncEssentialContactService asyncEssentialContactService,
                                       ContactRepository contactRepository) {
        super(repository, authService, clock, synchronizableTopicMessageService, logAPIErrorService);
        this.treatmentRepository = treatmentRepository;
        this.asyncEssentialContactService = asyncEssentialContactService;
        this.contactRepository = contactRepository;
    }

    @Override
    public List<PermissionEnum> getNecessaryPermissionsToEdit() {
        final List<PermissionEnum> authorities = new ArrayList<>();
        authorities.add(PermissionEnum.ADMIN);
        authorities.add(PermissionEnum.STAFF);
        return authorities;
    }

    @Override
    public EssentialContactDataModel convertEntityToModel(EssentialContact entity) {
        final EssentialContactDataModel model = new EssentialContactDataModel();
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setColor(entity.getColor());
        if (entity.getTreatments() != null) {
            model.setTreatmentIds(entity.getTreatments().stream()
                    .map(SynchronizableEntity::getId).collect(Collectors.toList()));
        }
        return model;
    }

    @Override
    public EssentialContact convertModelToEntity(EssentialContactDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        final EssentialContact entity = new EssentialContact();
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setColor(model.getColor());
        searchTreatments(entity, model.getTreatmentIds());
        return entity;
    }

    @Override
    public void updateEntity(EssentialContact entity, EssentialContactDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setColor(model.getColor());
        searchTreatments(entity, model.getTreatmentIds());
    }

    @Override
    public Optional<EssentialContact> findEntityByIdThatUserCanRead(Long id, Auth auth) throws AuthNullException {
        return this.repository.findById(id);
    }

    @Override
    public Optional<EssentialContact> findEntityByIdThatUserCanEdit(Long id, Auth auth) throws AuthNullException {
        return this.repository.findById(id);
    }

    @Override
    public List<EssentialContact> findEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        return this.repository.findAll();
    }

    @Override
    public List<EssentialContact> findEntitiesByIdThatUserCanEdit(List<Long> ids, Auth auth) throws AuthNullException {
        return this.repository.findAllById(ids);
    }

    @Override
    public List<EssentialContact> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) throws AuthNullException {
        return this.repository.findAllExcludingIds(ids);
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.ESSENTIAL_CONTACT;
    }

    @Override
    protected void afterDataCreated(EssentialContact entity, Auth auth) {
        asyncEssentialContactService.createUsersContacts(entity);
    }

    @Override
    protected void afterDataUpdated(EssentialContact entity, Auth auth) {
        asyncEssentialContactService.updateUsersContacts(entity);
    }

    @Override
    protected void beforeDataDeleted(EssentialContact entity, Auth auth) {
        final List<Contact> contacts = contactRepository.findAllByEssentialContactId(entity.getId());

        contacts.forEach(contact -> contact.setEssentialContact(null));

        contactRepository.saveAll(contacts);

        asyncEssentialContactService.deleteContacts(contacts);
    }

    public Optional<EssentialContact> findById(Long id) {
        return this.repository.findById(id);
    }

    private void searchTreatments(EssentialContact entity, List<Long> treatmentIds) {
        if (treatmentIds != null && treatmentIds.size() != 0) {
            final List<Treatment> treatmentSearch = treatmentRepository.findAllByIds(treatmentIds);
            entity.setTreatments(treatmentSearch);
        }
    }
}
