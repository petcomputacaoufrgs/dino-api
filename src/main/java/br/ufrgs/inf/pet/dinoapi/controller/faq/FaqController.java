package br.ufrgs.inf.pet.dinoapi.controller.faq;

import br.ufrgs.inf.pet.dinoapi.model.faq.FaqIdModel;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqModel;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqOptionModel;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqSaveRequestModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface FaqController {

    ResponseEntity<FaqModel> save(FaqSaveRequestModel faqSaveRequestModel);

    ResponseEntity<List<FaqModel>> saveAll(@Valid @RequestBody List<FaqSaveRequestModel> models);

    ResponseEntity<List<FaqOptionModel>> getFaqOptions();

    ResponseEntity<FaqModel> editFaq(@Valid @RequestBody FaqModel model);

    ResponseEntity<Long> getUserFaqId();

    ResponseEntity<FaqModel> getFaqUser();

    ResponseEntity<Long> saveFaqUser(@Valid @RequestBody FaqIdModel model);

}
