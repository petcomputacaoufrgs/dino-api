package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import br.ufrgs.inf.pet.dinoapi.repository.contact.PhoneRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PhoneServiceImpl implements PhoneService {

    private PhoneRepository phoneRepository;

    @Autowired
    public PhoneServiceImpl(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    public List<Phone> savePhones(List<PhoneSaveModel> phoneModels, Contact contact) {
        final List<Phone> phones = phoneModels.stream()
            .map(phoneModel -> new Phone(phoneModel, contact))
            .collect(Collectors.toList());

        final Iterable<Phone> savedPhones = phoneRepository.saveAll(phones);

        return Lists.newArrayList(savedPhones);
    }

    public void editPhones(List<PhoneModel> phoneModels, Contact contact) {
        final List<Phone> phonesToSave = new ArrayList<>();
        final List<Phone> phonesToDelete = phoneRepository.getPhonesByContactId(contact.getId());

        phoneModels.forEach(phoneModel -> {
            if (phoneModel.getId() == null) {
                phonesToSave.add(new Phone(phoneModel, contact));
            } else {
                final Optional<Phone> phoneSearch = phonesToDelete.stream()
                        .filter(phone -> phone.getId().equals(phoneModel.getId()))
                        .findFirst();

                if (phoneSearch.isPresent()) {
                    final Phone phoneDB = phoneSearch.get();

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
