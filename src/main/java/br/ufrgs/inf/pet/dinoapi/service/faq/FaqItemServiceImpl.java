package br.ufrgs.inf.pet.dinoapi.service.faq;

import br.ufrgs.inf.pet.dinoapi.constants.FaqConstants;
import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqItem;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqItemDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqItemRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.topic.synchronizable.SynchronizableTopicMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FaqItemServiceImpl extends SynchronizableServiceImpl<FaqItem, Long, Integer, FaqItemDataModel, FaqItemRepository> {

    private final FaqServiceImpl faqService;

    @Autowired
    public FaqItemServiceImpl(FaqItemRepository repository, AuthServiceImpl authService,  FaqServiceImpl faqService,
                              SynchronizableTopicMessageServiceImpl<Long, Integer, FaqItemDataModel> synchronizableTopicMessageService) {
        super(repository, authService, synchronizableTopicMessageService);
        this.faqService = faqService;
    }


    @Override
    public FaqItemDataModel convertEntityToModel(FaqItem entity) {
        final FaqItemDataModel model = new FaqItemDataModel();
        model.setAnswer(entity.getAnswer());
        model.setQuestion(entity.getQuestion());
        model.setFaqId(entity.getFaq().getId());

        return model;
    }

    @Override
    public FaqItem convertModelToEntity(FaqItemDataModel model) throws ConvertModelToEntityException {
        final Optional<Faq> faq = faqService.getEntityByIdAndUser(model.getFaqId(), this.getUser());

        if (faq.isPresent()) {
            final FaqItem entity = new FaqItem();
            entity.setQuestion(model.getQuestion());
            entity.setAnswer(model.getAnswer());
            entity.setFaq(faq.get());

            return entity;
        } else {
            throw new ConvertModelToEntityException(FaqConstants.INVALID_FAQ);
        }
    }

    @Override
    public void updateEntity(FaqItem entity, FaqItemDataModel model) throws ConvertModelToEntityException {
        if (!entity.getFaq().getId().equals(model.getFaqId())) {
            final Optional<Faq> faq = faqService.getEntityByIdAndUser(model.getFaqId(), this.getUser());

            if (faq.isPresent()) {
                entity.setFaq(faq.get());
            } else {
                throw new ConvertModelToEntityException(FaqConstants.INVALID_FAQ);
            }
        }

        entity.setAnswer(model.getAnswer());
        entity.setQuestion(model.getQuestion());
    }

    @Override
    public Optional<FaqItem> getEntityByIdAndUser(Long id, User user) {
        return this.repository.findById(id);
    }

    @Override
    public List<FaqItem> getEntitiesByUserId(User user) {
        return this.repository.findAll();
    }

    @Override
    public List<FaqItem> getEntitiesByIdsAndUserId(List<Long> ids, User user) {
        return this.repository.findAllByIds(ids);
    }

    @Override
    public List<FaqItem> getEntitiesByUserIdExceptIds(User user, List<Long> ids) {
        return this.repository.findAllExceptIds(ids);
    }

    @Override
    public WebSocketDestinationsEnum getUpdateWebSocketDestination() {
        return WebSocketDestinationsEnum.FAQ_ITEM_UPDATE;
    }

    @Override
    public WebSocketDestinationsEnum getDeleteWebSocketDestination() {
        return WebSocketDestinationsEnum.FAQ_ITEM_DELETE;
    }
}
