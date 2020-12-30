package br.ufrgs.inf.pet.dinoapi.service.faq;

import br.ufrgs.inf.pet.dinoapi.constants.AuthConstants;
import br.ufrgs.inf.pet.dinoapi.constants.FaqConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.faq.*;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.treatment.TreatmentServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.topic.SynchronizableTopicMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FaqServiceImpl extends SynchronizableServiceImpl<Faq, Long, Integer, FaqDataModel, FaqRepository> {

    private final TreatmentServiceImpl treatmentService;

    @Autowired
    public FaqServiceImpl(FaqRepository repository, AuthServiceImpl authService, TreatmentServiceImpl treatmentService,
                          ClockServiceImpl clockService, LogAPIErrorServiceImpl logAPIErrorService,
                          SynchronizableTopicMessageServiceImpl<Long, Integer, FaqDataModel> synchronizableTopicMessageService) {
        super(repository, authService, clockService, synchronizableTopicMessageService, logAPIErrorService);
        this.treatmentService = treatmentService;
    }

    @Override
    public FaqDataModel convertEntityToModel(Faq entity) {
        final FaqDataModel model = new FaqDataModel();
        model.setTitle(entity.getTitle());
        model.setTreatmentId(entity.getTreatment().getId());

        return model;
    }

    @Override
    public Faq convertModelToEntity(FaqDataModel model, Auth auth) throws ConvertModelToEntityException {
        final Optional<Treatment> treatment = treatmentService.getEntityById(model.getTreatmentId());

        if (treatment.isPresent()) {
            final Faq entity = new Faq();
            entity.setTitle(model.getTitle());
            entity.setTreatment(treatment.get());

            return entity;
        }

        throw new ConvertModelToEntityException(FaqConstants.INVALID_TREATMENT);
    }

    @Override
    public void updateEntity(Faq entity, FaqDataModel model, Auth auth) throws ConvertModelToEntityException {
        if (!entity.getTreatment().getId().equals(model.getTreatmentId())) {
            final Optional<Treatment> treatment = treatmentService.getEntityById(model.getTreatmentId());

            if (treatment.isPresent()) {
                entity.setTreatment(treatment.get());
            } else {
                throw new ConvertModelToEntityException(FaqConstants.INVALID_TREATMENT);
            }
        }

        entity.setTitle(model.getTitle());
    }

    @Override
    public Optional<Faq> getEntityByIdAndUserAuth(Long id, Auth auth) {
        return this.repository.findById(id);
    }

    @Override
    public List<Faq> getEntitiesByUserAuth(Auth auth) {
        return this.repository.findAll();
    }

    @Override
    public List<Faq> getEntitiesByIdsAndUserAuth(List<Long> ids, Auth auth) {
        return this.repository.findAllByIds(ids);
    }

    @Override
    public List<Faq> getEntitiesByUserAuthExceptIds(Auth auth, List<Long> ids) {
        return this.repository.findAllExceptIds(ids);
    }

    @Override
    public WebSocketDestinationsEnum getUpdateWebSocketDestination() {
        return WebSocketDestinationsEnum.FAQ_UPDATE;
    }

    @Override
    public WebSocketDestinationsEnum getDeleteWebSocketDestination() {
        return WebSocketDestinationsEnum.FAQ_DELETE;
    }

    public Optional<Faq> getEntityById(Long id) {
        return this.repository.findById(id);
    }
}
