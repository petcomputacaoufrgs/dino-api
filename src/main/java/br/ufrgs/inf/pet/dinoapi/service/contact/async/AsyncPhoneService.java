package br.ufrgs.inf.pet.dinoapi.service.contact.async;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import br.ufrgs.inf.pet.dinoapi.model.contacts.PhoneDataModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteModel;

import java.util.List;
import java.util.function.BiFunction;

public interface AsyncPhoneService {
    /**
     * Based in a phone of an essential contact, create new phones (including Google API) for all contacts that derive from the same essential contact
     * @param model Phone data model
     * @param saveAll Function to save a list of phone data model
     */
    void createPhoneOnGoogleAPI(PhoneDataModel model, BiFunction<List<PhoneDataModel>, Auth, List<PhoneDataModel>> saveAll);

    /**
     * Based in a phone of an essential contact, update all phones that derive from this essential phone on Google API
     * @param entity Phone entity
     * @param save Function to save a phone data model
     */
    void updatePhoneOnGoogleAPI(Phone entity, BiFunction<PhoneDataModel, Auth, PhoneDataModel> save);

    /**
     * Base in a phone of an essential contact, delete all phones that derive from this essential phone on Google API
     * @param delete Function to delete a phone based in delete model
     * @param deletedEssentialPhones List of essential phones to delete
     */
    void deletePhonesOnGoogleAPI(List<Phone> deletedEssentialPhones, BiFunction<SynchronizableDeleteModel<Long>, Auth, Void> delete);
}
