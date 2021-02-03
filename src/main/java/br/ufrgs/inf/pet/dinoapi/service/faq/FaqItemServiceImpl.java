package br.ufrgs.inf.pet.dinoapi.service.faq;

import br.ufrgs.inf.pet.dinoapi.constants.FaqConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqItem;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqItemDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqItemRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.OAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.treatment.TreatmentServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.topic.SynchronizableTopicMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FaqItemServiceImpl extends SynchronizableServiceImpl<FaqItem, Long, FaqItemDataModel, FaqItemRepository> {

    private final TreatmentServiceImpl treatmentService;

    @Autowired
    public FaqItemServiceImpl(FaqItemRepository repository, OAuthServiceImpl authService, TreatmentServiceImpl treatmentService,
                              ClockServiceImpl clockService, LogAPIErrorServiceImpl logAPIErrorService,
                              SynchronizableTopicMessageService<Long, FaqItemDataModel> synchronizableTopicMessageService) {
        super(repository, authService, clockService, synchronizableTopicMessageService, logAPIErrorService);
        this.treatmentService = treatmentService;
    }


    @Override
    public FaqItemDataModel convertEntityToModel(FaqItem entity) {
        final FaqItemDataModel model = new FaqItemDataModel();
        model.setAnswer(entity.getAnswer());
        model.setQuestion(entity.getQuestion());
        model.setTreatmentId(entity.getId());

        return model;
    }

    @Override
    public FaqItem convertModelToEntity(FaqItemDataModel model, Auth auth) throws ConvertModelToEntityException {
        final Optional<Treatment> faq = treatmentService.getEntityById(model.getTreatmentId());

        if (faq.isPresent()) {
            final FaqItem entity = new FaqItem();
            entity.setQuestion(model.getQuestion());
            entity.setAnswer(model.getAnswer());
            entity.setTreatment(faq.get());

            return entity;
        } else {
            throw new ConvertModelToEntityException(FaqConstants.INVALID_TREATMENT);
        }
    }

    @Override
    public void updateEntity(FaqItem entity, FaqItemDataModel model, Auth auth) throws ConvertModelToEntityException {
        if (!entity.getId().equals(model.getTreatmentId())) {
            final Optional<Treatment> faq = treatmentService.getEntityById(model.getTreatmentId());

            if (faq.isPresent()) {
                entity.setTreatment(faq.get());
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
