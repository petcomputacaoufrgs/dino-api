package br.ufrgs.inf.pet.dinoapi.controller.glossary;

import br.ufrgs.inf.pet.dinoapi.entity.GlossaryItem;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossarySaveRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryUpdateRequestModel;
import br.ufrgs.inf.pet.dinoapi.service.glossary.GlossaryServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.glossary.GlossaryVersionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class GlossaryControllerImpl implements GlossaryController {

    private final GlossaryServiceImpl glossaryItemService;

    private final GlossaryVersionServiceImpl glossaryVersionService;

    @Autowired
    public GlossaryControllerImpl(GlossaryServiceImpl glossaryItemService, GlossaryVersionServiceImpl glossaryVersionService) {
        this.glossaryItemService = glossaryItemService;
        this.glossaryVersionService = glossaryVersionService;
    }

    @Override
    @PostMapping("public/glossary/")
    public ResponseEntity<GlossaryResponseModel> save(@Valid @RequestBody GlossarySaveRequestModel glossarySaveRequestModel) {
        return glossaryItemService.save(glossarySaveRequestModel);
    }

    @Override
    @PutMapping("public/glossary/")
    public ResponseEntity<?> update(@Valid @RequestBody GlossaryUpdateRequestModel glossaryUpdateRequestModel) {
        return glossaryItemService.update(glossaryUpdateRequestModel);
    }

    @Override
    @GetMapping("public/glossary/")
    public ResponseEntity<List<GlossaryItem>> get() {
        return glossaryItemService.get();
    }

    @Override
    @GetMapping("public/glossary/version/")
    public ResponseEntity<Long> getVersion() {
        return glossaryVersionService.getGlossaryVersion();
    }

}
