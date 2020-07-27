package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ContactService {

    ResponseEntity<List<ContactModel>> getAllContacts();

    ResponseEntity<ContactResponseModel> saveContacts(List<ContactSaveModel> contactsSaveModel);

    ResponseEntity<ContactResponseModel> saveContact(ContactSaveModel model);

    ResponseEntity<?> editContacts(List<ContactModel> models);

    ResponseEntity<?> deleteContact(ContactDeleteModel model);

    ResponseEntity<?> deleteContacts(List<ContactDeleteModel> models);

    ResponseEntity<List<ContactModel>> getUserContacts();

    Contact saveContactDB(ContactSaveModel model, User user);



}

