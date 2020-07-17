package br.ufrgs.inf.pet.dinoapi.controller.contacts;


import org.springframework.http.ResponseEntity;
import br.ufrgs.inf.pet.dinoapi.model.contacts.*;

import java.util.List;

public interface ContactsController {

    ResponseEntity<ContactModel> saveContact(ContactSaveModel model);

    ResponseEntity<List<ContactModel>> saveContacts(List<ContactSaveModel> model);

    ResponseEntity<List<ContactModel>> getAllContacts();

    ResponseEntity<?> delete(ContactModel model);
}
