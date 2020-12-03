package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ContactService {

    ResponseEntity<SaveResponseModelAll> saveContacts(List<ContactSaveModel> contactsSaveModel);

    ResponseEntity<?> editContacts(List<ContactModel> models);

    ResponseEntity<Long> deleteContacts(List<ContactDeleteModel> models);

    ResponseEntity<List<ContactModel>> getUserContacts();

    ResponseEntity<?> declineGoogleContacts();

}

