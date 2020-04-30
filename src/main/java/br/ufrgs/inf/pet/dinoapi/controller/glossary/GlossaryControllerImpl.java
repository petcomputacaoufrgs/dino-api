package br.ufrgs.inf.pet.dinoapi.controller.glossary;

import br.ufrgs.inf.pet.dinoapi.entity.GlossaryItem;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossarySaveModel;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryUpdateModel;
import br.ufrgs.inf.pet.dinoapi.service.glossary.GlossaryServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.glossary.GlossaryVersionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/glossary/")
public class GlossaryControllerImpl implements GlossaryController {

    @Autowired
    GlossaryServiceImpl glossaryItemService;

    @Autowired
    GlossaryVersionServiceImpl glossaryVersionService;

    @Override
    @PostMapping("save/")
    public ResponseEntity<GlossaryResponseModel> save(@RequestBody GlossarySaveModel glossarySaveModel) {
        return glossaryItemService.save(glossarySaveModel);
    }

    @Override
    @PutMapping("update/")
    public ResponseEntity<?> update(@RequestBody GlossaryUpdateModel glossaryUpdateModel) {
        return glossaryItemService.update(glossaryUpdateModel);
    }

    @Override
    @GetMapping("get/")
    public ResponseEntity<List<GlossaryItem>> get() {
        return glossaryItemService.get();
    }

    @Override
    @GetMapping("version/")
    public ResponseEntity<Long> getVersion() {
        return glossaryVersionService.getGlossaryVersion();
    }

}
