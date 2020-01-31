package br.ufrgs.inf.pet.dinoapi.controller.glossary;

import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryModel;
import br.ufrgs.inf.pet.dinoapi.service.glossary_item.GlossaryItemServiceImpl;
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
    GlossaryItemServiceImpl glossaryItemService;

    @Autowired
    GlossaryVersionServiceImpl glossaryVersionService;

    @Override
    @PostMapping("update/")
    public ResponseEntity<?> save(@RequestBody GlossaryModel glossaryModel) {
        return glossaryItemService.save(glossaryModel);
    }

    @Override
    @GetMapping("version/")
    public ResponseEntity<?> getGlossaryVersion() {
        return glossaryVersionService.getGlossaryVersion();
    }

    @Override
    @GetMapping()
    public ResponseEntity<?> getGlossary() {
        return glossaryItemService.getGlossary();
    }
}
