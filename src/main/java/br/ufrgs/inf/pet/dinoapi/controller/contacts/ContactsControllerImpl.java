package br.ufrgs.inf.pet.dinoapi.controller.contacts;

import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import br.ufrgs.inf.pet.dinoapi.service.contact.ContactServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.ContactVersionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Avisando que tirei as funções prévias que manipulavam não um array de objetos mas um único objeto. Se algum dia precisar, tá feito*/
@RestController
@RequestMapping("/contacts/")
public class ContactsControllerImpl implements ContactsController {

    @Autowired
    ContactServiceImpl contactServiceImpl;
    @Autowired
    ContactVersionServiceImpl contactVersionServiceImpl;

    @Override
    @GetMapping("version/")
    public ResponseEntity<Long> getVersion() {
        return contactVersionServiceImpl.getVersion();
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ContactModel>> getUserContacts() {
        return contactServiceImpl.getUserContacts();
    }

    @PostMapping("all/")
    public ResponseEntity<ContactResponseModel> saveContacts(@RequestBody List<ContactSaveModel> models) {
        return contactServiceImpl.saveContacts(models);
    }

    @PutMapping("all/")
    public ResponseEntity<?> editContacts(@RequestBody List<ContactModel> models) {
        return contactServiceImpl.editContacts(models);
    }

    @DeleteMapping("all/")
    public ResponseEntity<?> deleteContacts(@RequestBody List<ContactDeleteModel> models) {
        return contactServiceImpl.deleteContacts(models);
    }
}
