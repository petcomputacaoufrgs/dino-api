package br.ufrgs.inf.pet.dinoapi.service.contact.async;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.contacts.EssentialContactDataModel;

import java.util.List;

public interface AsyncEssentialContactService {
    /**
     * Create new contacts (Google API included) based in this essential contact for all users (with essential contacts permission)
     * @param model Essential contact data model
     * @throws AuthNullException In case of authentication error
     * @throws ConvertModelToEntityException In case of cast error between model and entity
     */
    void createContactsOnGoogleAPI(EssentialContactDataModel model) throws AuthNullException, ConvertModelToEntityException;

    /**
     * Update contacts (Google API included) based in this essential contact
     * @param model Essential contact data model
     * @throws AuthNullException In case of authentication error
     * @throws ConvertModelToEntityException In case of cast error between model and entity
     */
    void updateContactsOnGoogleAPI(EssentialContactDataModel model) throws AuthNullException, ConvertModelToEntityException;

    /**
     * Delete contacts (Google API included)
     * @param contacts List of contacts to delete
     * @throws AuthNullException In case of authentication error
     */
    void deleteContactsOnGoogleAPI(List<Contact> contacts) throws AuthNullException;
}
