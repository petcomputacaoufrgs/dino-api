package br.ufrgs.inf.pet.dinoapi.controller.faq;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqUserQuestion;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqUserQuestionDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqUserQuestionRepository;
import br.ufrgs.inf.pet.dinoapi.service.faq.FaqUserQuestionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/faq_user_question/")
public class FaqUserQuestionControllerImpl extends SynchronizableControllerImpl<
        FaqUserQuestion, Long, Integer, FaqUserQuestionDataModel, FaqUserQuestionRepository, FaqUserQuestionServiceImpl> {
    @Autowired
    protected FaqUserQuestionControllerImpl(FaqUserQuestionServiceImpl service) {
        super(service);
    }
}
