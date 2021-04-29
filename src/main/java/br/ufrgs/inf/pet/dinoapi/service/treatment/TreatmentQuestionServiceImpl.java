package br.ufrgs.inf.pet.dinoapi.service.treatment;

import br.ufrgs.inf.pet.dinoapi.constants.FaqConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.TreatmentQuestion;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.treatment.TreatmentQuestionDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.treatment.TreatmentQuestionRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.SynchronizableStaffQueueMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TreatmentQuestionServiceImpl extends SynchronizableServiceImpl<TreatmentQuestion, Long, TreatmentQuestionDataModel, TreatmentQuestionRepository> {

    private final TreatmentServiceImpl treatmentService;

    @Autowired
    public TreatmentQuestionServiceImpl(TreatmentQuestionRepository repository, AuthServiceImpl authService,
                                        TreatmentServiceImpl treatmentService, ClockServiceImpl clockService,
                                        LogAPIErrorServiceImpl logAPIErrorService,
                                        SynchronizableStaffQueueMessageService<Long, TreatmentQuestionDataModel> synchronizableStaffQueueMessageService) {
        super(repository, authService, clockService, synchronizableStaffQueueMessageService, logAPIErrorService);
        this.treatmentService = treatmentService;
    }

    @Override
    public TreatmentQuestionDataModel convertEntityToModel(TreatmentQuestion entity) {
        final TreatmentQuestionDataModel model = new TreatmentQuestionDataModel();
        model.setQuestion(entity.getQuestion());
        model.setTreatmentId(entity.getTreatment().getId());

        return model;
    }

    @Override
    public TreatmentQuestion convertModelToEntity(TreatmentQuestionDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
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
    public void updateEntity(TreatmentQuestion entity, TreatmentQuestionDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
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
        return authService.isStaffOrAdmin()
                ? this.findById(id)
                : this.findByIdAndUser(id, auth);
    }

    @Override
    public Optional<TreatmentQuestion> findEntityByIdThatUserCanEdit(Long id, Auth auth) throws AuthNullException {
        return findEntityByIdThatUserCanRead(id, auth);
    }

    @Override
    public List<TreatmentQuestion> findEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return authService.isStaffOrAdmin()
                ? this.findAll()
                : this.repository.findAllByUserId(auth.getUser().getId());
    }

    @Override
    public List<TreatmentQuestion> findEntitiesByIdThatUserCanEdit(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return authService.isStaffOrAdmin()
                ? this.repository.findAllByIds(ids)
                : this.repository.findAllByIdsAndUserId(ids, auth.getUser().getId());
    }

    @Override
    public List<TreatmentQuestion> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return authService.isStaffOrAdmin()
                ? this.repository.findAllExcludingIds(ids)
                : this.repository.findAllByUserIdExcludingIds(auth.getUser().getId(), ids);
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.TREATMENT_QUESTION;
    }

    private Optional<TreatmentQuestion> findById(Long id)  {
        return this.repository.findById(id);
    }

    private List<TreatmentQuestion> findAll()  {
        return (List<TreatmentQuestion>) this.repository.findAll();
    }

    private Optional<TreatmentQuestion> findByIdAndUser(Long id, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findByIdAndUserId(id, auth.getUser().getId());
    }
}
