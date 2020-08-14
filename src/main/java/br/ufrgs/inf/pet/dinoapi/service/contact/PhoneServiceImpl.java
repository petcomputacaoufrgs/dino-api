package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import br.ufrgs.inf.pet.dinoapi.repository.contact.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PhoneServiceImpl implements PhoneService {

    @Autowired
    PhoneRepository phoneRepository;

    public List<Phone> savePhones(List<PhoneSaveModel> phoneModels, Contact contact) {

        List<Phone> phones = phoneModels.stream()
            .map(phoneModel -> new Phone(phoneModel, contact))
            .collect(Collectors.toList());

        phoneRepository.saveAll(phones);

        return phones;
    }

    public void editPhones(List<PhoneModel> phoneModels, Contact contact) {

        List<Phone> phonesToSave = new ArrayList<>();
        List<Phone> phonesToDelete = contact.getPhones();

        phoneModels.forEach(phoneModel -> {

            if (phoneModel.getId() == null) {
                phonesToSave.add(new Phone(phoneModel, contact));
            }
            else {

                Optional<Phone> phoneSearch = phonesToDelete.stream()
                        .filter(phone -> phone.getId().equals(phoneModel.getId()))
                        .findFirst();

                if (phoneSearch.isPresent()) {
                    Phone phoneDB = phoneSearch.get();

                    boolean changed = !phoneModel.getNumber().equals(phoneDB.getNumber());
                    if (changed) {
                        phoneDB.setNumber(phoneModel.getNumber());
                    }
                    if (phoneModel.getType() != phoneDB.getType()) {
                        phoneDB.setType(phoneModel.getType());
                        changed = true;
                    }
                    if (changed) {
                        phonesToSave.add(phoneDB);
                    }
                    phonesToDelete.remove(phoneDB);
                }
            }
        });
        phoneRepository.saveAll(phonesToSave);
        phoneRepository.deleteAllById(phonesToDelete.stream().map(Phone::getId).collect(Collectors.toList()));
    }
}
