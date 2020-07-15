package br.ufrgs.inf.pet.dinoapi.controller.contacts;

import org.springframework.http.ResponseEntity;
import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import br.ufrgs.inf.pet.dinoapi.service.contact.ContactServiceImpl;

public interface ContactsController {

    ResponseEntity<?> saveContact(ContactSaveModel model);

    ResponseEntity<?> getAllContacts();

}
