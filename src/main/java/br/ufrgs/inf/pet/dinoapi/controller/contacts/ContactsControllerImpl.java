package br.ufrgs.inf.pet.dinoapi.controller.contacts;

import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import br.ufrgs.inf.pet.dinoapi.service.contact.ContactServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping("/contacts/")
    public class ContactsControllerImpl implements ContactsController {

        @Autowired
        ContactServiceImpl contactServiceImpl;

        @Override
        @GetMapping
        public ResponseEntity<List<ContactModel>> getAllContacts() {
            return contactServiceImpl.getAllContacts();
        }


        @PostMapping
        public ResponseEntity<ContactModel> saveContact(@RequestBody ContactSaveModel model) {
            return contactServiceImpl.saveContact(model);
        }

        @PostMapping("/all")
        public ResponseEntity<List<ContactModel>> saveContacts(@RequestBody List<ContactSaveModel> models) {
            return contactServiceImpl.saveContacts(models);
        }

        @DeleteMapping
        public ResponseEntity<?> delete(@RequestBody ContactModel model) {
            return contactServiceImpl.deleteContact(model);
        }

}
