package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public interface ContactService {

    ResponseEntity<ArrayList<ContactResponseModel>> getContacts();
    ResponseEntity<?> saveContact(ContactSaveModel contactSaveModel);
    /*ResponseEntity<ContactResponseModel> saveContacts(List<ContactSaveModel> contactsSaveModel);

    ResponseEntity<ContactResponseModel> savePhone(PhoneModel phoneModel);
    ResponseEntity<ContactResponseModel> savePhones(PhoneModel phoneModel);

    ResponseEntity<?> delete(ContactDeleteModel contactDeleteModel);
    ResponseEntity<?> delete(List<ContactDeleteModel> contactsDeleteModel);*/


}

