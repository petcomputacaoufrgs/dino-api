package br.ufrgs.inf.pet.dinoapi.service.faq;

import br.ufrgs.inf.pet.dinoapi.model.faq.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FaqService {

    public ResponseEntity<FaqModel> get(FaqIdModel model);

    public ResponseEntity<FaqModel> save(FaqSaveRequestModel faqSaveRequestModel);

    public ResponseEntity<List<FaqModel>> saveAll(FaqListSaveRequestModel model);

    public ResponseEntity<Long> saveFaqUser(FaqIdModel model);

    public ResponseEntity<FaqModel> getFaqUser();

    public ResponseEntity<?> getFaqUserVersion();

    public ResponseEntity<FaqModel> editFaq(FaqModel model);

    public ResponseEntity<List<FaqOptionModel>> getFaqOptions();

}

