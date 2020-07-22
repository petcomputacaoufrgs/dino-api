package br.ufrgs.inf.pet.dinoapi.controller.contacts;


import org.springframework.http.ResponseEntity;
import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ContactsController {

    ResponseEntity<ContactModel> saveContact(ContactSaveModel model);

    ResponseEntity<List<ContactModel>> saveContacts(List<ContactSaveModel> models);

    ResponseEntity<List<ContactModel>> getAllContacts();

    ResponseEntity<?> editContacts(List<ContactModel> models);

    ResponseEntity<?> deleteContact(ContactModel model);

    ResponseEntity<?> deleteContacts(List<ContactModel> models);

    ResponseEntity<?> deletePhones(List<PhoneModel> models);
}
