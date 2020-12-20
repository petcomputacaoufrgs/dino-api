package br.ufrgs.inf.pet.dinoapi.service.faq;

import br.ufrgs.inf.pet.dinoapi.constants.FaqConstants;
import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.faq.*;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.treatment.TreatmentServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.topic.synchronizable.SynchronizableTopicMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FaqServiceImpl extends SynchronizableServiceImpl<Faq, Long, Integer, FaqDataModel, FaqRepository> {

    private final TreatmentServiceImpl treatmentService;

    @Autowired
    public FaqServiceImpl(FaqRepository repository, AuthServiceImpl authService, TreatmentServiceImpl treatmentService,
                          SynchronizableTopicMessageServiceImpl<Long, Integer, FaqDataModel> synchronizableTopicMessageService) {
        super(repository, authService, synchronizableTopicMessageService);
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
    public Faq convertModelToEntity(FaqDataModel model, User user) throws ConvertModelToEntityException {
        final Optional<Treatment> treatment = treatmentService.getEntityByIdAndUser(model.getTreatmentId(), user);

        if (treatment.isPresent()) {
            final Faq entity = new Faq();
            entity.setTitle(model.getTitle());
            entity.setTreatment(treatment.get());

            return entity;
        }

        throw new ConvertModelToEntityException(FaqConstants.INVALID_TREATMENT);

    }

    @Override
    public void updateEntity(Faq entity, FaqDataModel model, User user) throws ConvertModelToEntityException {
        if (!entity.getTreatment().getId().equals(model.getTreatmentId())) {
            final Optional<Treatment> treatment = treatmentService.getEntityByIdAndUser(model.getTreatmentId(), user);

            if (treatment.isPresent()) {
                entity.setTreatment(treatment.get());
            } else {
                throw new ConvertModelToEntityException(FaqConstants.INVALID_TREATMENT);
            }
        }

        entity.setTitle(model.getTitle());
    }

    @Override
    public Optional<Faq> getEntityByIdAndUser(Long id, User user) {
        return this.repository.findById(id);
    }

    @Override
    public List<Faq> getEntitiesByUserId(User user) {
        return this.repository.findAll();
    }

    @Override
    public List<Faq> getEntitiesByIdsAndUserId(List<Long> ids, User user) {
        return this.repository.findAllByIds(ids);
    }

    @Override
    public List<Faq> getEntitiesByUserIdExceptIds(User user, List<Long> ids) {
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
}
