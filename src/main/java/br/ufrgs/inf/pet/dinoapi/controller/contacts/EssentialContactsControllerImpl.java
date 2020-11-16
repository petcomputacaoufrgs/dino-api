package br.ufrgs.inf.pet.dinoapi.controller.contacts;

import br.ufrgs.inf.pet.dinoapi.model.contacts.EssentialContactSaveModel;
import br.ufrgs.inf.pet.dinoapi.service.contact.EssentialContactServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/public/contacts/essencial/")
public class EssentialContactsControllerImpl implements EssentialContactsController {

    private final EssentialContactServiceImpl essentialContactServiceImpl;

    @Autowired
    public EssentialContactsControllerImpl(EssentialContactServiceImpl essentialContactServiceImpl) {
        this.essentialContactServiceImpl = essentialContactServiceImpl;
    }

    @PostMapping("all/")
    public ResponseEntity<?> saveEssentialContacts(@Valid @RequestBody List<EssentialContactSaveModel> models) {
        return essentialContactServiceImpl.saveEssentialContacts(models);
    }
}
