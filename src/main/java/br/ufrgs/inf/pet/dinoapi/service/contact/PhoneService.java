package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import br.ufrgs.inf.pet.dinoapi.model.contacts.ContactSaveModel;
import br.ufrgs.inf.pet.dinoapi.model.contacts.PhoneSaveModel;

import java.util.List;

public interface PhoneService {

    List<Phone> savePhonesDB(List<PhoneSaveModel> phoneModels, Contact contact);

}
