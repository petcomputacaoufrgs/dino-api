package br.ufrgs.inf.pet.dinoapi.service.faq;

import br.ufrgs.inf.pet.dinoapi.constants.FaqConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqUserQuestion;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqUserQuestionDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqUserQuestionRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.SynchronizableQueueMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FaqUserQuestionServiceImpl extends SynchronizableServiceImpl<FaqUserQuestion, Long, Integer, FaqUserQuestionDataModel, FaqUserQuestionRepository>  {

    private final FaqServiceImpl faqService;

    @Autowired
    public FaqUserQuestionServiceImpl(FaqUserQuestionRepository repository, AuthServiceImpl authService,
                                      FaqServiceImpl faqService, ClockServiceImpl clockService,
                                      LogAPIErrorServiceImpl logAPIErrorService,
                                      SynchronizableQueueMessageServiceImpl<Long, Integer, FaqUserQuestionDataModel> synchronizableQueueMessageService) {
        super(repository, authService, clockService, synchronizableQueueMessageService, logAPIErrorService);
        this.faqService = faqService;
    }

    @Override
    public FaqUserQuestionDataModel convertEntityToModel(FaqUserQuestion entity) {
        final FaqUserQuestionDataModel model = new FaqUserQuestionDataModel();
        model.setQuestion(entity.getQuestion());
        model.setFaqId(entity.getFaq().getId());

        return model;
    }

    @Override
    public FaqUserQuestion convertModelToEntity(FaqUserQuestionDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        if (auth != null) {
            final Optional<Faq> faq = faqService.getEntityByIdAndUserAuth(model.getFaqId(), auth);

            if (faq.isEmpty()) {
                throw new ConvertModelToEntityException(FaqConstants.INVALID_FAQ);
            }

            final FaqUserQuestion entity = new FaqUserQuestion();

            entity.setQuestion(model.getQuestion());
            entity.setFaq(faq.get());
            entity.setUser(auth.getUser());

            return entity;
        }

        throw new AuthNullException();
    }

    @Override
    public void updateEntity(FaqUserQuestion entity, FaqUserQuestionDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        if (auth != null) {
            if (!entity.getFaq().getId().equals(model.getFaqId())) {
                final Optional<Faq> faq = faqService.getEntityByIdAndUserAuth(model.getFaqId(), auth);

                if (faq.isEmpty()) {
                    throw new ConvertModelToEntityException(FaqConstants.INVALID_FAQ);
                }

                entity.setFaq(faq.get());
            }

            entity.setQuestion(model.getQuestion());
        } else {
            throw new AuthNullException();
        }
    }

    @Override
    public Optional<FaqUserQuestion> getEntityByIdAndUserAuth(Long id, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findByIdAndUserId(id, auth.getUser().getId());
    }

    @Override
    public List<FaqUserQuestion> getEntitiesByUserAuth(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByUserId(auth.getUser().getId());
    }

    @Override
    public List<FaqUserQuestion> getEntitiesByIdsAndUserAuth(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByIdsAndUserId(ids, auth.getUser().getId());
    }

    @Override
    public List<FaqUserQuestion> getEntitiesByUserAuthExceptIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByUserIdExceptIds(auth.getUser().getId(), ids);
    }

    @Override
    public WebSocketDestinationsEnum getUpdateWebSocketDestination() {
        return WebSocketDestinationsEnum.FAQ_USER_QUESTION_UPDATE;
    }

    @Override
    public WebSocketDestinationsEnum getDeleteWebSocketDestination() {
        return WebSocketDestinationsEnum.FAQ_USER_QUESTION_DELETE;
    }
}
