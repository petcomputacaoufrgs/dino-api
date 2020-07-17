package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import br.ufrgs.inf.pet.dinoapi.model.contacts.ContactModel;
import br.ufrgs.inf.pet.dinoapi.model.contacts.PhoneModel;
import br.ufrgs.inf.pet.dinoapi.model.contacts.PhoneSaveModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
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
