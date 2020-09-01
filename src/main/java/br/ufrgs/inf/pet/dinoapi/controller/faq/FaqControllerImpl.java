package br.ufrgs.inf.pet.dinoapi.controller.faq;

import br.ufrgs.inf.pet.dinoapi.model.faq.*;
import br.ufrgs.inf.pet.dinoapi.service.faq.FaqServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
public class FaqControllerImpl implements FaqController{

    private final FaqServiceImpl faqServiceImpl;

    @Autowired
    public FaqControllerImpl(FaqServiceImpl faqServiceImpl) {
        this.faqServiceImpl = faqServiceImpl;
    }

    @Override
    @GetMapping("public/faq/")
    public ResponseEntity<FaqModel> get(@Valid @RequestBody FaqIdModel model) {
        return faqServiceImpl.get(model);
    }

    @Override
    @GetMapping("public/faq/all/")
    public ResponseEntity<List<FaqModel>> getAll() {
        return faqServiceImpl.getAll();
    }

    @Override
    @PostMapping("public/faq/")
    public ResponseEntity<FaqModel> save(@Valid @RequestBody FaqSaveRequestModel model) {
        return faqServiceImpl.save(model);
    }

    @Override
    @PostMapping("public/faq/all/")
    public ResponseEntity<List<FaqModel>> saveAll(@Valid @RequestBody FaqListSaveRequestModel model) {
        return faqServiceImpl.saveAll(model);
    }

    @Override
    @GetMapping("public/faq/options/")
    public ResponseEntity<List<FaqOptionModel>> getFaqOptions() {
        return faqServiceImpl.getFaqOptions();
    }

    @Override
    @PutMapping("public/faq/")
    public ResponseEntity<FaqModel> editFaq(@Valid @RequestBody FaqModel model) {
        return faqServiceImpl.editFaq(model);
    }

    @Override
    @GetMapping("faq/version/")
    public ResponseEntity<FaqVersionModel> getFaqUserVersion() {
        return faqServiceImpl.getFaqUserVersion();
    }

    @Override
    @GetMapping("faq/")
    public ResponseEntity<FaqModel> getFaqUser() {
        return faqServiceImpl.getFaqUser();
    }

    @Override
    @PostMapping("faq/")
    public ResponseEntity<Long> saveFaqUser(@Valid @RequestBody FaqIdModel model) {
        return faqServiceImpl.saveFaqUser(model);
    }
}
