package br.ufrgs.inf.pet.dinoapi.service.treatment;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;
import br.ufrgs.inf.pet.dinoapi.enumerable.PermissionEnum;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.treatment.TreatmentDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.treatment.TreatmentRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.OAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.topic.SynchronizableTopicMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TreatmentServiceImpl extends SynchronizableServiceImpl<Treatment, Long, TreatmentDataModel, TreatmentRepository> {

    @Autowired
    public TreatmentServiceImpl(TreatmentRepository repository, OAuthServiceImpl authService,
                                ClockServiceImpl clockService, LogAPIErrorServiceImpl logAPIErrorService,
                                SynchronizableTopicMessageService<Long, TreatmentDataModel> synchronizableTopicMessageService) {
        super(repository, authService, clockService, synchronizableTopicMessageService, logAPIErrorService);
    }

    @Override
    public List<PermissionEnum> getNecessaryPermissionsToEdit() {
        final List<PermissionEnum> authorities = new ArrayList<>();
        authorities.add(PermissionEnum.ADMIN);
        authorities.add(PermissionEnum.STAFF);
        return authorities;
    }

    @Override
    public TreatmentDataModel convertEntityToModel(Treatment entity) {
        final TreatmentDataModel model = new TreatmentDataModel();
        model.setName(entity.getName());
        return model;
    }

    @Override
    public Treatment convertModelToEntity(TreatmentDataModel model, Auth auth) throws ConvertModelToEntityException {
        final Treatment entity = new Treatment();
        entity.setName(model.getName());

        return entity;
    }

    @Override
    public void updateEntity(Treatment entity, TreatmentDataModel model, Auth auth) throws ConvertModelToEntityException {
        entity.setName(model.getName());
    }

    @Override
    public Optional<Treatment> findEntityByIdThatUserCanRead(Long id, Auth auth) {
        return this.repository.findById(id);
    }

    @Override
    public Optional<Treatment> findEntityByIdThatUserCanEdit(Long id, Auth auth) {
        return this.repository.findById(id);
    }

    @Override
    public List<Treatment> findEntitiesThatUserCanRead(Auth auth) {
        return this.repository.findAll();
    }

    @Override
    public List<Treatment> findEntitiesByIdThatUserCanEdit(List<Long> ids, Auth auth) {
        return this.repository.findByIds(ids);
    }

    @Override
    public List<Treatment> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) {
        return this.repository.findAllExcludingIds(ids);
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.TREATMENT;
    }

    public Optional<Treatment> getEntityById(Long id) {
        return this.repository.findById(id);
    }

    public List<Treatment> getEntitiesByIds(List<Long> ids) {
        return this.repository.findAllByIds(ids);
    }

    public List<Treatment> getEntitiesByEssentialContact(EssentialContact essentialContact) {
        return this.repository.findAllByEssentialContact(essentialContact);
    }
}
