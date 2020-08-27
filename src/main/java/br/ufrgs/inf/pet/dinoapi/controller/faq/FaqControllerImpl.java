package br.ufrgs.inf.pet.dinoapi.controller.faq;

import br.ufrgs.inf.pet.dinoapi.model.faq.*;
import br.ufrgs.inf.pet.dinoapi.service.faq.FaqServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@Controller
public class FaqControllerImpl implements FaqController{

    private final FaqServiceImpl faqServiceImpl;

    @Autowired
    public FaqControllerImpl(FaqServiceImpl faqServiceImpl) {
        this.faqServiceImpl = faqServiceImpl;
    }

    @PostMapping("public/faq/")
    public ResponseEntity<FaqModel> save(@Valid @RequestBody FaqSaveRequestModel model) {
        return faqServiceImpl.save(model);
    }

    @PostMapping("public/faq/all/")
    public ResponseEntity<List<FaqModel>> saveAll(@Valid @RequestBody List<FaqSaveRequestModel> models) {
        return faqServiceImpl.saveAll(models);
    }

    @GetMapping("public/faq/options/")
    public ResponseEntity<List<FaqOptionModel>> getFaqOptions() {
        return faqServiceImpl.getFaqOptions();
    }

    @PutMapping("public/faq/")
    public ResponseEntity<FaqModel> editFaq(@Valid @RequestBody FaqModel model) {
        return faqServiceImpl.editFaq(model);
    }

    @GetMapping("faq/version/")
    public ResponseEntity<FaqSyncModel> getFaqUserVersion() {
        return faqServiceImpl.getFaqUserVersion();
    }

    @GetMapping("faq/")
    public ResponseEntity<FaqModel> getFaqUser() {
        return faqServiceImpl.getFaqUser();
    }

    @PostMapping("faq/")
    public ResponseEntity<Long> saveFaqUser(@Valid @RequestBody FaqIdModel model) {
        return faqServiceImpl.saveFaqUser(model);
    }
}
