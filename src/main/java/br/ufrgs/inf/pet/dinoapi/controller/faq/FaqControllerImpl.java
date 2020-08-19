package br.ufrgs.inf.pet.dinoapi.controller.faq;

import br.ufrgs.inf.pet.dinoapi.model.faq.FaqAllModel;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqModel;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqOptionsModel;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqSaveRequestModel;
import br.ufrgs.inf.pet.dinoapi.service.faq.FaqServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.faq.FaqVersionServiceimpl;
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

    private final FaqVersionServiceimpl faqVersionServiceimpl;

    @Autowired
    public FaqControllerImpl(FaqServiceImpl faqServiceImpl,
                             FaqVersionServiceimpl faqVersionServiceimpl) {
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

    @GetMapping("public/faq/all/")
    public ResponseEntity<FaqAllModel> getAll() {
        return faqServiceImpl.getAll();
    }

    @GetMapping("public/faq/options")
    public ResponseEntity<FaqOptionsModel> getFaqOptions() {
        return faqServiceImpl.getFaqOptions();
    }

    /*
    @Override
    @PutMapping("public/glossary/update/")
    public ResponseEntity<?> update(@Valid @RequestBody GlossaryUpdateRequestModel glossaryUpdateRequestModel) {
        return glossaryItemService.update(glossaryUpdateRequestModel);
    }

    @Override
    @GetMapping("/glossary/version/")
    public ResponseEntity<Long> getVersion() {
        return glossaryVersionService.getGlossaryVersion();
    }

     */
}
