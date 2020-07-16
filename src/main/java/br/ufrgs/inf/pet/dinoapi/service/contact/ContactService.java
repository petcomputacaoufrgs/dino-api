package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public interface ContactService {

    ResponseEntity<List<ContactModel>> getAllContacts();
    ResponseEntity<ContactModel> saveContact(ContactSaveModel model);
    /*ResponseEntity<ContactModel> saveContacts(List<ContactSaveModel> contactsSaveModel);

    ResponseEntity<ContactModel> savePhone(PhoneModel phoneModel);
    ResponseEntity<ContactModel> savePhones(PhoneModel phoneModel);

    ResponseEntity<?> delete(ContactDeleteModel contactDeleteModel);
    ResponseEntity<?> delete(List<ContactDeleteModel> contactsDeleteModel);*/


}

