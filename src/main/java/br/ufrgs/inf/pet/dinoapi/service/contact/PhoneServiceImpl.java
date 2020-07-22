package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import br.ufrgs.inf.pet.dinoapi.repository.contact.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PhoneServiceImpl implements PhoneService {

    @Autowired
    PhoneRepository phoneRepository;

    public List<Phone> savePhonesDB(List<PhoneSaveModel> phoneModels, Contact contact) {

        List<Phone> phones = phoneModels.stream()
            .map(phoneModel -> {
                Phone phone = new Phone();
                phone.setType(phoneModel.getType());
                phone.setNumber(phoneModel.getNumber());
                phone.setContact(contact);
                return phone;
            })
            .collect(Collectors.toList());

        phoneRepository.saveAll(phones);

        return phones;
    }

    public List<Phone> addPhoneSaveModels(List<PhoneModel> phoneModels, Contact contact) {

        List<Phone> phones = phoneModels.stream()
                .map(phoneModel -> {
                    Phone phone = new Phone();
                    phone.setType(phoneModel.getType());
                    phone.setNumber(phoneModel.getNumber());
                    phone.setContact(contact);
                    return phone;
                })
                .collect(Collectors.toList());

        phoneRepository.saveAll(phones);

        return phones;
    }


    public void editPhonesDB(List<PhoneModel> phoneModels, Contact contact) {

        if (phoneModels.size() > 0) {

            List<Phone> phonesToSave = new ArrayList<>();

            phoneModels.forEach(phoneModel -> {

                if(phoneModel.getId() == null) {
                    Phone newPhone = new Phone();
                    newPhone.setType(phoneModel.getType());
                    newPhone.setNumber(phoneModel.getNumber());
                    newPhone.setContact(contact);
                    phonesToSave.add(newPhone);
                } else {
                    Optional<Phone> phoneSearch = phoneRepository.findByIdAndContactId(phoneModel.getId(), contact.getId());

                    if (phoneSearch.isPresent()) {
                        Phone phone = phoneSearch.get();

                        if (phone.getId().equals(phoneModel.getId())) {

                            boolean changed = !phoneModel.getNumber().equals(phone.getNumber());

                            if (changed)
                                phone.setNumber(phoneModel.getNumber());

                            if (phoneModel.getType() != phone.getType()) {
                                phone.setType(phoneModel.getType());
                                changed = true;
                            }
                            if (changed)
                                phonesToSave.add(phone);
                        }
                    }
                }
            });
            phoneRepository.saveAll(phonesToSave);
        }
    }


    public ResponseEntity<?> deletePhone(PhoneModel model) {
        phoneRepository.deleteById(model.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deletePhones(List<PhoneModel> models) {
        if (models.size() == 0) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        models.forEach(phoneModel -> phoneRepository.deleteById(phoneModel.getId()));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
