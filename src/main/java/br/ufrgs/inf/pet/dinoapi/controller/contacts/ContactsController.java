package br.ufrgs.inf.pet.dinoapi.controller.contacts;

import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ContactsController {

    ResponseEntity<?> saveContact(ContactSaveModel ContactSaveModel);

    ResponseEntity<?> getContacts();

}
