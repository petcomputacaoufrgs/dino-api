package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.*;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import br.ufrgs.inf.pet.dinoapi.repository.contact.ContactRepository;
import br.ufrgs.inf.pet.dinoapi.repository.contact.FakeContactService;
import br.ufrgs.inf.pet.dinoapi.repository.contact.PhoneRepository;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.*;

@Service
public class ContactServiceImpl implements ContactService {

        @Autowired
        //ContactRepository contactRepository;
        FakeContactService contactRepository;
        @Autowired
        PhoneRepository phoneRepository;
        @Autowired
        UserServiceImpl userServiceImpl;

        @Override
        public ResponseEntity<ArrayList<ContactModel>> getAllContacts() {

            ArrayList<ContactModel> response = new ArrayList<>();

            //User user = userServiceImpl.getCurrentUser();

            //List<Contact> contacts = user.getContacts();

            List<Contact> contacts = contactRepository.getAllContacts();

            for (Contact contact : contacts) {
                System.out.println(contact.toString());
                ContactModel responseItem = new ContactModel();
                responseItem.setByContact(contact);
                response.add(responseItem);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        public ResponseEntity<?> saveContact(ContactSaveModel model) {
            /*
            if (model.getPhones().size() == 0) {
                return new ResponseEntity<>("Contato deve conter um ou mais números", HttpStatus.BAD_REQUEST);
            }
            else if (model.getName().equals("")) {
                return new ResponseEntity<>("Contato deve ter um nome", HttpStatus.BAD_REQUEST);
            }


            User user = userServiceImpl.getCurrentUser();

            List<Contact> contactSearch = contactRepository.findAllByNameAndUserId(model.getName(), user.getId());

            if (contactSearch.size() > 0) {
                return new ResponseEntity<>("Contato com nome já existente", HttpStatus.BAD_REQUEST);
            }
             */

            Contact contact = new Contact();
            contact.setByContactSaveModel(model);

            /*
            ArrayList<Phone> phones = new ArrayList<Phone>(contact.getPhones());
            phones = (ArrayList<Phone>) phoneRepository.saveAll(phones);
            contact.setPhones(phones);
            */
            Contact response = contactRepository.saveContact(contact);
            System.out.println(contact.toString());

            //ContactModel response = new ContactModel();
            //response.setByContact(contact);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

}
