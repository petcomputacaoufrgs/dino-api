package br.ufrgs.inf.pet.dinoapi.controller.faq;

import br.ufrgs.inf.pet.dinoapi.model.faq.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface FaqController {

    ResponseEntity<FaqModel> get(FaqIdModel model);

    ResponseEntity<List<FaqModel>> getAll();

    ResponseEntity<FaqModel> save(FaqSaveRequestModel faqSaveRequestModel);

    ResponseEntity<List<FaqModel>> saveAll(FaqListSaveRequestModel model);

    ResponseEntity<List<FaqOptionModel>> getFaqOptions();

    ResponseEntity<FaqModel> editFaq(FaqModel model);

    ResponseEntity<FaqVersionModel> getFaqUserVersion();

    ResponseEntity<FaqModel> getFaqUser();

    ResponseEntity<Long> saveFaqUser(FaqIdModel model);

}
