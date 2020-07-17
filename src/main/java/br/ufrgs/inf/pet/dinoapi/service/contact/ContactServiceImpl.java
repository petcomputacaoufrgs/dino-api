package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.*;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import br.ufrgs.inf.pet.dinoapi.repository.contact.ContactRepository;
import br.ufrgs.inf.pet.dinoapi.repository.contact.PhoneRepository;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ContactServiceImpl implements ContactService {

        @Autowired
        ContactRepository contactRepository;
        @Autowired
        PhoneRepository phoneRepository;
        @Autowired
        UserServiceImpl userServiceImpl;

        public ResponseEntity<List<ContactModel>> getAllContacts() {

            List<Contact> contacts = Lists.newArrayList(contactRepository.findAll());

            List<ContactModel> response = contacts.stream().map(ContactModel::new).collect(Collectors.toList());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        public ResponseEntity<List<ContactModel>> saveContacts(List<ContactSaveModel> models) {

            //User user = userServiceImpl.getCurrentUser();
            User user = userServiceImpl.findUserByEmail("mayra.cademartori@gmail.com");

            List<Contact> contacts = models.stream()
                    .map(modelContact -> saveContactDB(modelContact, user))
                    .collect(Collectors.toList());

            List<ContactModel> response = contacts.stream()
                    .map(ContactModel::new)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }


        public Contact saveContactDB(ContactSaveModel model, User user) {
            Contact contact = new Contact();
            contact.setFrontId(model.getFrontId());
            contact.setName(model.getName());
            contact.setDescription(model.getDescription());
            contact.setColor(model.getColor());
            contact.setUser(user);

            contact = contactRepository.save(contact);

            contact.setPhones(savePhonesDB(model, contact));

            return contact;
        }

        public ResponseEntity<ContactModel> saveContact(ContactSaveModel model) {

            //User user = userServiceImpl.getCurrentUser();
            User user = userServiceImpl.findUserByEmail("mayra.cademartori@gmail.com");
            Contact contact = new Contact();
            contact.setFrontId(model.getFrontId());
            contact.setName(model.getName());
            contact.setDescription(model.getDescription());
            contact.setColor(model.getColor());
            contact.setUser(user);

            contact = contactRepository.save(contact); //ver isso do tempCont

            contact.setPhones(savePhonesDB(model, contact));

            ContactModel response = new ContactModel(contact);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        public List<Phone> savePhonesDB(ContactSaveModel model, Contact contact) {

            List<Phone> phones = model.getPhones()
                    .stream()
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

    public ResponseEntity<?> deleteContact(ContactModel model) {

        if (model == null || model.getId() == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        //User user = userServiceImpl.getCurrentUser();
        User user = userServiceImpl.findUserByEmail("mayra.cademartori@gmail.com");

        model.getPhones().forEach(phoneModel ->
                phoneRepository.deleteByIdAndContactId(phoneModel.getId(), model.getId()));
        contactRepository.deleteByIdAndUserId(model.getId(), user.getId());


        return new ResponseEntity<>(HttpStatus.OK);
    }

}
