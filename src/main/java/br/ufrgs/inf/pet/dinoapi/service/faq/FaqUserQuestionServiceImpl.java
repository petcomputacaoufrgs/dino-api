package br.ufrgs.inf.pet.dinoapi.service.faq;

import br.ufrgs.inf.pet.dinoapi.constants.FaqConstants;
import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqUserQuestion;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqUserQuestionDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqUserQuestionRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.synchronizable.SynchronizableQueueMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FaqUserQuestionServiceImpl extends SynchronizableServiceImpl<FaqUserQuestion, Long, Integer, FaqUserQuestionDataModel, FaqUserQuestionRepository>  {

    private final FaqServiceImpl faqService;

    @Autowired
    public FaqUserQuestionServiceImpl(FaqUserQuestionRepository repository, AuthServiceImpl authService, FaqServiceImpl faqService,
                                      SynchronizableQueueMessageServiceImpl<Long, Integer, FaqUserQuestionDataModel> synchronizableQueueMessageService) {
        super(repository, authService, synchronizableQueueMessageService);
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
    public FaqUserQuestion convertModelToEntity(FaqUserQuestionDataModel model) throws ConvertModelToEntityException {
        final User user = this.getUser();
        final Optional<Faq> faq = faqService.getEntityByIdAndUser(model.getFaqId(), user);

        if (!faq.isPresent()) {
            throw new ConvertModelToEntityException(FaqConstants.INVALID_FAQ);
        }

        final FaqUserQuestion entity = new FaqUserQuestion();

        entity.setQuestion(model.getQuestion());
        entity.setFaq(faq.get());
        entity.setUser(user);

        return entity;
    }

    @Override
    public void updateEntity(FaqUserQuestion entity, FaqUserQuestionDataModel model) throws ConvertModelToEntityException {
        final User user = this.getUser();
        if (!entity.getFaq().getId().equals(model.getFaqId())) {
            final Optional<Faq> faq = faqService.getEntityByIdAndUser(model.getFaqId(), user);

            if (!faq.isPresent()) {
                throw new ConvertModelToEntityException(FaqConstants.INVALID_FAQ);
            }

            entity.setFaq(faq.get());
        }

        entity.setQuestion(model.getQuestion());
    }

    @Override
    public Optional<FaqUserQuestion> getEntityByIdAndUser(Long id, User user) {
        return this.repository.findByIdAndUserId(id, user.getId());
    }

    @Override
    public List<FaqUserQuestion> getEntitiesByUserId(User user) {
        return this.repository.findAllByUserId(user.getId());
    }

    @Override
    public List<FaqUserQuestion> getEntitiesByIdsAndUserId(List<Long> ids, User user) {
        return this.repository.findAllByIdsAndUserId(ids, user.getId());
    }

    @Override
    public List<FaqUserQuestion> getEntitiesByUserIdExceptIds(User user, List<Long> ids) {
        return this.repository.findAllByUserIdExceptIds(user.getId(), ids);
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
