package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

public interface ContactService {

    ResponseEntity<ArrayList<ContactModel>> getAllContacts();
    ResponseEntity<?> saveContact(ContactSaveModel model);
    /*ResponseEntity<ContactModel> saveContacts(List<ContactSaveModel> contactsSaveModel);

    ResponseEntity<ContactModel> savePhone(PhoneModel phoneModel);
    ResponseEntity<ContactModel> savePhones(PhoneModel phoneModel);

    ResponseEntity<?> delete(ContactDeleteModel contactDeleteModel);
    ResponseEntity<?> delete(List<ContactDeleteModel> contactsDeleteModel);*/


}

