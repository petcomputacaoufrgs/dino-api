package br.ufrgs.inf.pet.dinoapi.controller.glossary;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.glossary.GlossaryItem;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryItemDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.glossary.GlossaryItemRepository;
import br.ufrgs.inf.pet.dinoapi.service.glossary.GlossaryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static br.ufrgs.inf.pet.dinoapi.constants.PathConstants.GLOSSARY;

@RestController
@RequestMapping(GLOSSARY)
public class GlossaryControllerImpl extends SynchronizableControllerImpl<
        GlossaryItem, Long, GlossaryItemDataModel, GlossaryItemRepository, GlossaryServiceImpl> {

    @Autowired
    public GlossaryControllerImpl(GlossaryServiceImpl glossaryItemService) {
        super(glossaryItemService);
    }
}
