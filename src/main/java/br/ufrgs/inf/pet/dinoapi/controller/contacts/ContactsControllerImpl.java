package br.ufrgs.inf.pet.dinoapi.controller.contacts;

import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import br.ufrgs.inf.pet.dinoapi.service.contact.ContactServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.ContactVersionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/contacts/")
public class ContactsControllerImpl implements ContactsController {

    private final ContactServiceImpl contactServiceImpl;
    private final ContactVersionServiceImpl contactVersionServiceImpl;

    @Autowired
    public ContactsControllerImpl(ContactServiceImpl contactServiceImpl, ContactVersionServiceImpl contactVersionServiceImpl) {
        this.contactServiceImpl = contactServiceImpl;
        this.contactVersionServiceImpl = contactVersionServiceImpl;
    }

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

    @PostMapping
    public ResponseEntity<SaveResponseModel> saveContact(@Valid @RequestBody ContactSaveModel model) {
        return contactServiceImpl.saveContact(model);
    }

    @PostMapping("all/")
    public ResponseEntity<SaveResponseModelAll> saveContacts(@Valid @RequestBody List<ContactSaveModel> models) {
        return contactServiceImpl.saveContacts(models);
    }

    @PutMapping
    public ResponseEntity<?> editContact(@Valid @RequestBody ContactModel model) {
        return contactServiceImpl.editContact(model);
    }

    @PutMapping("all/")
    public ResponseEntity<?> editContacts(@Valid @RequestBody List<ContactModel> models) {
        return contactServiceImpl.editContacts(models);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteContact(@Valid @RequestBody ContactIdModel model) {
        return contactServiceImpl.deleteContact(model);
    }

    @DeleteMapping("all/")
    public ResponseEntity<?> deleteContacts(@Valid @RequestBody List<ContactIdModel> models) {
        return contactServiceImpl.deleteContacts(models);
    }

    @PutMapping("google/decline_contacts/")
    public ResponseEntity<?> declineGoogleContacts() {
        return contactServiceImpl.declineGoogleContacts();
    }
}
