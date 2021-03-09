package br.ufrgs.inf.pet.dinoapi.service.contact.async;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.contacts.GoogleContactDataModel;
import br.ufrgs.inf.pet.dinoapi.model.google.people.GooglePeopleModel;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.function.BiFunction;

public interface AsyncSaveGoogleContact {
    /**
     * Save contact on Google API
     * @param user Contact's user
     * @param id Google contact's id
     * @param contactId Contact's id
     * @param googlePeopleModel Google people model according to People API
     * @param save Function to save google contact based in data model
     */
    void saveGoogleContact(User user, Long id, Long contactId, GooglePeopleModel googlePeopleModel, BiFunction<GoogleContactDataModel, Auth, Void> save);
}
