package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.NoteTag;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.*;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import br.ufrgs.inf.pet.dinoapi.model.notes.NoteModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.ContactRepository;
import br.ufrgs.inf.pet.dinoapi.repository.contact.FakeContactService;
import br.ufrgs.inf.pet.dinoapi.repository.contact.PhoneRepository;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ContactServiceImpl implements ContactService {

        @Autowired
        ContactRepository contactRepository;
        //FakeContactService contactRepository;
        @Autowired
        PhoneRepository phoneRepository;
        @Autowired
        UserServiceImpl userServiceImpl;

        public ResponseEntity<List<ContactModel>> getAllContacts() {

            List<Contact> contacts = Lists.newArrayList(contactRepository.findAll());

            List<ContactModel> response = contacts.stream().map(ContactModel::new).collect(Collectors.toList());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        public ResponseEntity<ContactModel> saveContact(ContactSaveModel model) {


            //User user = userServiceImpl.getCurrentUser();
            Contact contact = new Contact();
            contact.setFrontId(model.getFrontId());
            contact.setName(model.getName());
            contact.setPhones(model.getPhones().stream()
                    .map(modelPhone -> {
                        Phone phone = new Phone();
                        phone.setType(modelPhone.getType());
                        phone.setNumber(modelPhone.getNumber());
                        return phone;
                    })
                    .collect(Collectors.toList()));
            contact.setDescription(model.getDescription());
            contact.setColor(model.getColor());
            //contact.setUser(user);

            contact = contactRepository.save(contact);

            ContactModel response = new ContactModel(contact);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

}
