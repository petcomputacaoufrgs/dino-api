package br.ufrgs.inf.pet.dinoapi.controller.faq;

import br.ufrgs.inf.pet.dinoapi.model.faq.FaqModel;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqSaveRequestModel;
import br.ufrgs.inf.pet.dinoapi.service.faq.FaqServiceImpl;
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

    @Autowired
    public FaqControllerImpl(FaqServiceImpl faqServiceImpl) {
        this.faqServiceImpl = faqServiceImpl;
    }

    @PostMapping("public/faq/save/")
    public ResponseEntity<FaqModel> save(@Valid @RequestBody FaqSaveRequestModel model) {
        return faqServiceImpl.save(model);
    }

    @PostMapping("public/faq/save/all")
    public ResponseEntity<List<FaqModel>> saveAll(@Valid @RequestBody List<FaqSaveRequestModel> models) {
        return faqServiceImpl.saveAll(models);
    }
    
    @GetMapping("public/faq/get/all")
    public ResponseEntity<List<FaqModel>> get() {
        return faqServiceImpl.getAll();
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
