package br.ufrgs.inf.pet.dinoapi.service.faq;

import br.ufrgs.inf.pet.dinoapi.constants.FaqConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqItem;
import br.ufrgs.inf.pet.dinoapi.enumerable.PermissionEnum;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqItemDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqItemRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.treatment.TreatmentServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.SynchronizableTopicMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FaqItemServiceImpl extends SynchronizableServiceImpl<FaqItem, Long, FaqItemDataModel, FaqItemRepository> {

    private final TreatmentServiceImpl treatmentService;

    @Autowired
    public FaqItemServiceImpl(FaqItemRepository repository, AuthServiceImpl authService, TreatmentServiceImpl treatmentService,
                              ClockServiceImpl clockService, LogAPIErrorServiceImpl logAPIErrorService,
                              SynchronizableTopicMessageService<Long, FaqItemDataModel> synchronizableTopicMessageService) {
        super(repository, authService, clockService, synchronizableTopicMessageService, logAPIErrorService);
        this.treatmentService = treatmentService;
    }

    @Override
    public List<PermissionEnum> getNecessaryPermissionsToEdit() {
        final List<PermissionEnum> authorities = new ArrayList<>();
        authorities.add(PermissionEnum.ADMIN);
        authorities.add(PermissionEnum.STAFF);
        return authorities;
    }


    @Override
    public FaqItemDataModel convertEntityToModel(FaqItem entity) {
        final FaqItemDataModel model = new FaqItemDataModel();
        model.setAnswer(entity.getAnswer());
        model.setQuestion(entity.getQuestion());
        if(entity.getTreatment() != null) {
            model.setTreatmentId(entity.getTreatment().getId());
        }

        return model;
    }

    @Override
    public FaqItem convertModelToEntity(FaqItemDataModel model, Auth auth) throws ConvertModelToEntityException {

        final FaqItem entity = new FaqItem();
        entity.setQuestion(model.getQuestion());
        entity.setAnswer(model.getAnswer());
        if(model.getTreatmentId() != null) {
            entity.setTreatment(treatmentService.getEntityById(model.getTreatmentId()).orElseGet(() -> null));
        }

        return entity;
    }

    @Override
    public void updateEntity(FaqItem entity, FaqItemDataModel model, Auth auth) throws ConvertModelToEntityException {
        if (!entity.getTreatment().getId().equals(model.getTreatmentId())) { //TODO ver com joão...
            final Optional<Treatment> treatmentSearch = treatmentService.getEntityById(model.getTreatmentId());

            if (treatmentSearch.isPresent()) {
                entity.setTreatment(treatmentSearch.get());
            } else {
                throw new ConvertModelToEntityException(FaqConstants.INVALID_TREATMENT);
            }
        }

        entity.setAnswer(model.getAnswer());
        entity.setQuestion(model.getQuestion());
    }

    @Override
    public Optional<FaqItem> findEntityByIdThatUserCanRead(Long id, Auth auth) {
        return this.repository.findById(id);
    }

    @Override
    public Optional<FaqItem> findEntityByIdThatUserCanEdit(Long id, Auth auth) {
        return this.repository.findById(id);
    }

    @Override
    public List<FaqItem> findEntitiesThatUserCanRead(Auth auth) {
        return this.repository.findAll();
    }

    @Override
    public List<FaqItem> findEntitiesByIdThatUserCanEdit(List<Long> ids, Auth auth) {
        return this.repository.findAllByIds(ids);
    }

    @Override
    public List<FaqItem> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) {
        return this.repository.findAllExcludingIds(ids);
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.FAQ_ITEM;
    }
}
