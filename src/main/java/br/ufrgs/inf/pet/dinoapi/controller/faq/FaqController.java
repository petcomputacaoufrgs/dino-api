package br.ufrgs.inf.pet.dinoapi.controller.faq;

import br.ufrgs.inf.pet.dinoapi.model.faq.FaqModel;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqSaveRequestModel;
import org.springframework.http.ResponseEntity;

public interface FaqController {

    ResponseEntity<FaqModel> save(FaqSaveRequestModel faqSaveRequestModel);

}
