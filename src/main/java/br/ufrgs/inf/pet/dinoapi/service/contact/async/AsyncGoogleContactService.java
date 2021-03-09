package br.ufrgs.inf.pet.dinoapi.service.contact.async;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.GoogleContact;
import br.ufrgs.inf.pet.dinoapi.model.contacts.GoogleContactDataModel;

import java.util.function.BiFunction;

public interface AsyncGoogleContactService {
    /**
     * Create Google Contact on Google API (with user permission)
     * @param model Google Contact data model
     * @param save Function to save result on database base in {@link GoogleContactDataModel}
     */
    void createContactOnGoogleAPI(GoogleContactDataModel model, BiFunction<GoogleContactDataModel, Auth, Void> save);

    /**
     * Update Google Contact on Google API (with user permission)
     * @param entity Google Contact entity
     * @param save Function to save result on database base in {@link GoogleContactDataModel}
     */
    void updateContactOnGoogleAPI(GoogleContact entity, BiFunction<GoogleContactDataModel, Auth, Void> save);

    /**
     * Delete Google Contact on Google API (with user permission)
     * @param entity Google Contact entity
     */
    void deleteContactOnGoogleAPI(GoogleContact entity);
}
