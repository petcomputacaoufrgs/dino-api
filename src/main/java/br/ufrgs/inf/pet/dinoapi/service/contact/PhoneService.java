package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import br.ufrgs.inf.pet.dinoapi.model.contacts.PhoneModel;
import br.ufrgs.inf.pet.dinoapi.model.contacts.PhoneSaveModel;

import java.util.List;

public interface PhoneService {

    List<Phone> savePhones(List<PhoneSaveModel> phoneModels, Contact contact);

    void editPhones(List<PhoneModel> phoneModels, Contact contact);

}
