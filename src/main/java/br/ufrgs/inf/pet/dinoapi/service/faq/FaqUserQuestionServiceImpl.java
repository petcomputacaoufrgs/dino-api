package br.ufrgs.inf.pet.dinoapi.service.faq;

import br.ufrgs.inf.pet.dinoapi.constants.FaqConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.faq.TreatmentQuestion;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqUserQuestionDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqUserQuestionRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.OAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.treatment.TreatmentServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.SynchronizableQueueMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FaqUserQuestionServiceImpl extends SynchronizableServiceImpl<TreatmentQuestion, Long, FaqUserQuestionDataModel, FaqUserQuestionRepository> {

    private final TreatmentServiceImpl treatmentService;

    @Autowired
    public FaqUserQuestionServiceImpl(FaqUserQuestionRepository repository, OAuthServiceImpl authService,
                                      TreatmentServiceImpl treatmentService, ClockServiceImpl clockService,
                                      LogAPIErrorServiceImpl logAPIErrorService,
                                      SynchronizableQueueMessageService<Long, FaqUserQuestionDataModel> synchronizableQueueMessageService) {
        super(repository, authService, clockService, synchronizableQueueMessageService, logAPIErrorService);
        this.treatmentService = treatmentService;
    }

    @Override
    public FaqUserQuestionDataModel convertEntityToModel(TreatmentQuestion entity) {
        final FaqUserQuestionDataModel model = new FaqUserQuestionDataModel();
        model.setQuestion(entity.getQuestion());
        model.setTreatmentId(entity.getTreatment().getId());

        return model;
    }

    @Override
    public TreatmentQuestion convertModelToEntity(FaqUserQuestionDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        if (auth != null) {
            final Optional<Treatment> faq = treatmentService.findEntityByIdThatUserCanRead(model.getTreatmentId(), auth);

            if (faq.isEmpty()) {
                throw new ConvertModelToEntityException(FaqConstants.INVALID_TREATMENT);
            }

            final TreatmentQuestion entity = new TreatmentQuestion();

            entity.setQuestion(model.getQuestion());
            entity.setTreatment(faq.get());
            entity.setUser(auth.getUser());

            return entity;
        }

        throw new AuthNullException();
    }

    @Override
    public void updateEntity(TreatmentQuestion entity, FaqUserQuestionDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        if (auth != null) {
            if (!entity.getTreatment().getId().equals(model.getTreatmentId())) {
                final Optional<Treatment> faq = treatmentService.findEntityByIdThatUserCanRead(model.getTreatmentId(), auth);

                if (faq.isEmpty()) {
                    throw new ConvertModelToEntityException(FaqConstants.INVALID_TREATMENT);
                }

                entity.setTreatment(faq.get());
            }

            entity.setQuestion(model.getQuestion());
        } else {
            throw new AuthNullException();
        }
    }

    @Override
    public Optional<TreatmentQuestion> findEntityByIdThatUserCanRead(Long id, Auth auth) throws AuthNullException {
        return this.findByIdAndUser(id, auth);
    }

    @Override
    public Optional<TreatmentQuestion> findEntityByIdThatUserCanEdit(Long id, Auth auth) throws AuthNullException {
        return this.findByIdAndUser(id, auth);
    }

    private Optional<TreatmentQuestion> findByIdAndUser(Long id, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findByIdAndUserId(id, auth.getUser().getId());
    }

    @Override
    public List<TreatmentQuestion> findEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByUserId(auth.getUser().getId());
    }

    @Override
    public List<TreatmentQuestion> findEntitiesByIdThatUserCanEdit(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByIdsAndUserId(ids, auth.getUser().getId());
    }

    @Override
    public List<TreatmentQuestion> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByUserIdExcludingIds(auth.getUser().getId(), ids);
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.FAQ_USER_QUESTION;
    }
}
