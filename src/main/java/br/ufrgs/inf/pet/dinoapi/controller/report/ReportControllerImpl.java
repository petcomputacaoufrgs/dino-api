package br.ufrgs.inf.pet.dinoapi.controller.report;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.TreatmentQuestion;
import br.ufrgs.inf.pet.dinoapi.model.treatment.TreatmentQuestionDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.treatment.TreatmentQuestionRepository;
import br.ufrgs.inf.pet.dinoapi.service.treatment.TreatmentQuestionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.ufrgs.inf.pet.dinoapi.constants.PathConstants.REPORT;

@RestController
@RequestMapping(REPORT)
public class ReportControllerImpl extends SynchronizableControllerImpl<
        TreatmentQuestion, Long, TreatmentQuestionDataModel, TreatmentQuestionRepository, TreatmentQuestionServiceImpl> {
    @Autowired
    protected ReportControllerImpl(TreatmentQuestionServiceImpl service) {
        super(service);
    }
}