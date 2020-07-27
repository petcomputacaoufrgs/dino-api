package br.ufrgs.inf.pet.dinoapi.controller.contacts;


import org.springframework.http.ResponseEntity;
import br.ufrgs.inf.pet.dinoapi.model.contacts.*;

import java.util.List;

public interface ContactsController {

    ResponseEntity<ContactResponseModel> saveContact(ContactSaveModel model);

    ResponseEntity<ContactResponseModel> saveContacts(List<ContactSaveModel> models);

    ResponseEntity<?> editContacts(List<ContactModel> models);

    ResponseEntity<?> deleteContact(ContactDeleteModel model);

    ResponseEntity<?> deleteContacts(List<ContactDeleteModel> models);

    ResponseEntity<?> deletePhones(List<PhoneModel> models);

    ResponseEntity<List<ContactModel>> getAllContacts();

    ResponseEntity<List<ContactModel>> getUserContacts();

    ResponseEntity<Long> getVersion();

}
