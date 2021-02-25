package br.ufrgs.inf.pet.dinoapi.controller.faq;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.TreatmentQuestion;
import br.ufrgs.inf.pet.dinoapi.model.treatment.TreatmentQuestionDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqUserQuestionRepository;
import br.ufrgs.inf.pet.dinoapi.service.treatment.TreatmentQuestionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/faq_user_question/")
public class FaqUserQuestionControllerImpl extends SynchronizableControllerImpl<
        TreatmentQuestion, Long, TreatmentQuestionDataModel, FaqUserQuestionRepository, TreatmentQuestionServiceImpl> {
    @Autowired
    protected FaqUserQuestionControllerImpl(TreatmentQuestionServiceImpl service) {
        super(service);
    }
}
