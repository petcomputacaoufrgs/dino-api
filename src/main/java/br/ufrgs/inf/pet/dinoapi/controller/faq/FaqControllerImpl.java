package br.ufrgs.inf.pet.dinoapi.controller.faq;

import br.ufrgs.inf.pet.dinoapi.model.faq.*;
import br.ufrgs.inf.pet.dinoapi.service.faq.FaqServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.faq.FaqVersionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@Controller
public class FaqControllerImpl implements FaqController{

    private final FaqServiceImpl faqServiceImpl;

    private final FaqVersionServiceImpl faqVersionServiceimpl;

    @Autowired
    public FaqControllerImpl(FaqServiceImpl faqServiceImpl,
                             FaqVersionServiceImpl faqVersionServiceimpl) {
        this.faqServiceImpl = faqServiceImpl;
        this.faqVersionServiceimpl = faqVersionServiceimpl;
    }

    @PostMapping("public/faq/")
    public ResponseEntity<FaqModel> save(@Valid @RequestBody FaqSaveRequestModel model) {
        return faqServiceImpl.save(model);
    }

    @PostMapping("public/faq/all/")
    public ResponseEntity<List<FaqModel>> saveAll(@Valid @RequestBody List<FaqSaveRequestModel> models) {
        return faqServiceImpl.saveAll(models);
    }

    @GetMapping("faq/")
    public ResponseEntity<FaqModel> getFaqUser() {
        return faqServiceImpl.getFaqUser();
    }

    @PostMapping("faq/")
    public ResponseEntity<Long> saveFaqUser(@Valid @RequestBody FaqIdModel model) {
        return faqServiceImpl.saveFaqUser(model);
    }

    @GetMapping("public/faq/all/")
    public ResponseEntity<FaqAllModel> getAll() {
        return faqServiceImpl.getAll();
    }

    @GetMapping("public/faq/options/")
    public ResponseEntity<FaqOptionsModel> getFaqOptions() {
        return faqServiceImpl.getFaqOptions();
    }


    @GetMapping("public/faq/options/version/")
    public ResponseEntity<Long> getFaqOptionsVersion() {
        return faqServiceImpl.getFaqOptionsVersion();
    }


}
