package br.ufrgs.inf.pet.dinoapi.controller.contacts;


import org.springframework.http.ResponseEntity;
import br.ufrgs.inf.pet.dinoapi.model.contacts.*;

import java.util.List;

public interface ContactsController {

    /**
     * Requisita os tokens de acesso do Google utilizando um token de autenticação do usuário
     *
     * @param token Token de autorização Google
     * @return GoogleTokenResponse contendo todos os dados e tokens necessários para login
     * @author mayra.cademartori
     */
    ResponseEntity<SaveResponseModelAll> saveContacts(List<ContactSaveModel> models);

    ResponseEntity<?> editContacts(List<ContactModel> models);

    ResponseEntity<?> deleteContacts(List<ContactDeleteModel> models);

    ResponseEntity<List<ContactModel>> getUserContacts();

    ResponseEntity<Long> getVersion();

    ResponseEntity<SaveResponseModel> saveContact(ContactSaveModel model);

    ResponseEntity<?> deleteContact(ContactDeleteModel model);

    ResponseEntity<?> editContact(ContactModel model);


}
