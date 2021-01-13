package br.ufrgs.inf.pet.dinoapi.communication.google.people;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.GoogleContact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.contacts.ContactDataModel;
import br.ufrgs.inf.pet.dinoapi.model.google.people.GooglePeopleModel;

import java.util.List;

public interface GooglePeopleCommunication {
    /**
     * Get a contact on Google People API
     *  @param user dino user that owns the contact
     * @param googleContact saved GoogleContact
     * @return GooglePeopleModel or null
     */
    GooglePeopleModel getContact(User user, GoogleContact googleContact);

    /**
     * Save a contact on Google People API
     *  @param user dino user that owns the contact
     * @param contactDataModel data model of contact to save
     * @return saved GooglePeopleModel or null
     */
    GooglePeopleModel createContact(User user, ContactDataModel contactDataModel);

    /**
     * Save a contact on Google People API
     *  @param user dino user that owns the contact
     * @param contactDataModel data model of contact to save
     * @param googleContact saved GoogleContact
     * @return saved GooglePeopleModel or null
     */
    GooglePeopleModel updateContact(User user, ContactDataModel contactDataModel, List<Phone> phones, GoogleContact googleContact);

    /**
     * Delete a contact on Google People API
     * @param user dino user that owns the contact
     * @param contactDataModel data model of contact to save
     * @param googleContact saved GoogleContact
     */
    void deleteContact(User user, ContactDataModel contactDataModel, List<Phone> phones, GoogleContact googleContact);
}
