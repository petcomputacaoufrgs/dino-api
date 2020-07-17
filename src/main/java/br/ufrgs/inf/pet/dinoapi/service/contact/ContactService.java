package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ContactService {

    ResponseEntity<List<ContactModel>> getAllContacts();
    ResponseEntity<List<ContactModel>> saveContacts(List<ContactSaveModel> contactsSaveModel);
    ResponseEntity<ContactModel> saveContact(ContactSaveModel model);
    ResponseEntity<?> deleteContact(ContactModel model);
    ResponseEntity<?> deleteContacts(List<ContactModel> models);


    Contact saveContactDB(ContactSaveModel model, User user);
    void deleteContactDB(ContactModel model, Long userId);

    //ResponseEntity<?> delete(ContactDeleteModel contactDeleteModel);
    //ResponseEntity<?> delete(List<ContactDeleteModel> contactsDeleteModel);*/


}

