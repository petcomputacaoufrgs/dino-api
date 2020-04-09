package br.ufrgs.inf.pet.dinoapi.controller.glossary;

import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossarySaveModel;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryUpdateModel;
import br.ufrgs.inf.pet.dinoapi.service.glossary.GlossaryServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.glossary_version.GlossaryVersionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Implementação de: {@link GlossaryController}
 *
 * @author joao.silva
 */
@RestController
@RequestMapping("/glossary/")
public class GlossaryControllerImpl implements GlossaryController {

    @Autowired
    GlossaryServiceImpl glossaryItemService;

    @Autowired
    GlossaryVersionServiceImpl glossaryVersionService;

    @Override
    @PostMapping()
    public ResponseEntity<?> save(@RequestBody GlossarySaveModel glossarySaveModel) {
        return glossaryItemService.save(glossarySaveModel);
    }

    @Override
    @PutMapping()
    public ResponseEntity<?> update(@RequestBody GlossaryUpdateModel glossaryUpdateModel) {
        return glossaryItemService.update(glossaryUpdateModel);
    }

    @Override
    @GetMapping()
    public ResponseEntity<?> get() {
        return glossaryItemService.getGlossary();
    }

    @Override
    @GetMapping("version/")
    public ResponseEntity<?> getVersion() {
        return glossaryVersionService.getGlossaryVersion();
    }


}
