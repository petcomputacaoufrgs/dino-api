package br.ufrgs.inf.pet.dinoapi.communication.google.people;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.GoogleContact;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.google.people.GooglePeopleModel;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface GooglePeopleCommunication {
    /**
     * Get a contact on Google People API
     * @param user dino user that owns the contact
     * @param resourceName resourceName of saved Google Contact
     * @return GooglePeopleModel or null
     */
    GooglePeopleModel getContact(User user, String resourceName) throws IOException, InterruptedException, URISyntaxException;

    /**
     * Save a contact on Google People API
     * @param user dino user that owns the contact
     * @param name contact name
     * @param description contact description
     * @param phoneNumbers list of contact's phone numbers
     * @return saved GooglePeopleModel or null
     */
    GooglePeopleModel createContact(User user, String name, String description, List<String> phoneNumbers);

    /**
     * Save a contact on Google People API
     * @param user dino user that owns the contact
     * @param name contact name
     * @param description contact description
     * @param phoneNumbers list of contact's phone numbers
     * @param resourceName resourceName of saved Google Contact
     * @return saved GooglePeopleModel or null
     */
    GooglePeopleModel updateContact(User user, String name, String description, List<String> phoneNumbers, String resourceName);

    /**
     * Delete a contact on Google People API
     * @param user dino user that owns the contact
     * @param googleContact saved GoogleContact
     * @return
     */
    boolean deleteContact(User user, GoogleContact googleContact);
}
