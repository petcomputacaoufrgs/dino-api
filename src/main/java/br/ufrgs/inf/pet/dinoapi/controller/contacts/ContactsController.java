package br.ufrgs.inf.pet.dinoapi.controller.contacts;


import org.springframework.http.ResponseEntity;
import br.ufrgs.inf.pet.dinoapi.model.contacts.*;

import java.util.List;

public interface ContactsController {

    /**
     * Salva uma lista de contatos recebendo uma lista de models {@link ContactSaveModel}
     *
     * @return Objeto {@link SaveResponseModelAll}
     */
    ResponseEntity<SaveResponseModelAll> saveContacts(List<ContactSaveModel> models);

    /**
     * Edita uma lista de contatos recebendo uma lista de models {@link ContactModel}
     *
     * @return Número da versão nova dos contatos
     */
    ResponseEntity<?> editContacts(List<ContactModel> models);

    /**
     * Deleta uma lista de contatos recebendo uma lista de models {@link ContactDeleteModel}
     *
     * @return Número da versão nova dos contatos
     */
    ResponseEntity<?> deleteContacts(List<ContactDeleteModel> models);

    /**
     * Retorna os contatos do usuário
     *
     * @return Lista do tipo {@link ContactModel}
     */
    ResponseEntity<List<ContactModel>> getUserContacts();

    /**
     * Retorna a versão dos contatos do usuário
     *
     * @return Número da versão nova dos contatos
     */
    ResponseEntity<Long> getVersion();

    /**
     * Salva um contato através da model {@link ContactSaveModel}
     *
     * @return Objeto {@link SaveResponseModel}
     */
    ResponseEntity<SaveResponseModel> saveContact(ContactSaveModel model);

    /**
     * Remove um contato através da model {@link ContactDeleteModel}
     *
     * @return Número da versão nova dos contatos
     */
    ResponseEntity<?> deleteContact(ContactDeleteModel model);

    /**
     * Edita um contato através da model {@link ContactModel}
     *
     * @return Número da versão nova dos contatos
     */
    ResponseEntity<?> editContact(ContactModel model);
+  
     /**
     * Salva o declinio em acesso aos contatos do Google
     */
    ResponseEntity<?> declineGoogleContacts();
}
