package br.ufrgs.inf.pet.dinoapi.controller.contacts;


import org.springframework.http.ResponseEntity;
import br.ufrgs.inf.pet.dinoapi.model.contacts.*;

import java.util.List;

public interface ContactsController {

    ResponseEntity<SaveResponseModelAll> saveContacts(List<ContactSaveModel> models);

    ResponseEntity<?> editContacts(List<ContactModel> models);

    ResponseEntity<?> deleteContacts(List<ContactDeleteModel> models);

    ResponseEntity<List<ContactModel>> getUserContacts();

    ResponseEntity<Long> getVersion();

    ResponseEntity<SaveResponseModel> saveContact(ContactSaveModel model);

    ResponseEntity<?> deleteContact(ContactDeleteModel model);

    ResponseEntity<?> editContact(ContactModel model);


}
