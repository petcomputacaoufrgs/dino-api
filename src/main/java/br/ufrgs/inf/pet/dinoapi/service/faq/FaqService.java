package br.ufrgs.inf.pet.dinoapi.service.faq;

import br.ufrgs.inf.pet.dinoapi.model.faq.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FaqService {

    ResponseEntity<FaqModel> get(FaqIdModel model);

    ResponseEntity<List<FaqModel>> getAll();

    ResponseEntity<FaqModel> save(FaqSaveRequestModel faqSaveRequestModel);

    ResponseEntity<List<FaqModel>> saveAll(FaqListSaveRequestModel model);

    ResponseEntity<Long> saveFaqUser(FaqIdModel model);

    ResponseEntity<FaqModel> getFaqUser();

    ResponseEntity<?> getFaqUserVersion();

    ResponseEntity<FaqModel> editFaq(FaqModel model);

    ResponseEntity<List<FaqOptionModel>> getFaqOptions();

}

