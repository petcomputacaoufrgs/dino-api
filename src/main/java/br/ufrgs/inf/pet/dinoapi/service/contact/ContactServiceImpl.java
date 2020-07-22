package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.Note;
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
        @Autowired
        PhoneServiceImpl phoneServiceImpl;

        public ResponseEntity<List<ContactModel>> getAllContacts() {

            List<Contact> contacts = Lists.newArrayList(contactRepository.findAll());

            List<ContactModel> response = contacts.stream().map(ContactModel::new).collect(Collectors.toList());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        public ResponseEntity<ContactModel> saveContact(ContactSaveModel model) {

            //User user = userServiceImpl.getCurrentUser();
            User user = userServiceImpl.findUserByEmail("mayra.cademartori@gmail.com");
            Contact contact = new Contact(model, user);

            contact = contactRepository.save(contact);

            contact.setPhones(phoneServiceImpl.savePhonesDB(model.getPhones(), contact));

            ContactModel response = new ContactModel(contact);

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
            Contact contact = new Contact(model, user);

            contact = contactRepository.save(contact);

            contact.setPhones(phoneServiceImpl.savePhonesDB(model.getPhones(), contact));

            return contact;
        }


    public ResponseEntity<?> deleteContact(ContactModel model) {

        if (model == null || model.getId() == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        //User user = userServiceImpl.getCurrentUser();
        User user = userServiceImpl.findUserByEmail("mayra.cademartori@gmail.com");

        deleteContactDB(model, user.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteContacts(List<ContactModel> models) {

        if (models.size() == 0) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        //User user = userServiceImpl.getCurrentUser();
        User user = userServiceImpl.findUserByEmail("mayra.cademartori@gmail.com");

        models.forEach(model -> deleteContactDB(model, user.getId()));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public void deleteContactDB(ContactModel model, Long userId) {
        model.getPhones()
                .forEach(phoneModel ->
                        phoneRepository.deleteByIdAndContactId(phoneModel.getId(),model.getId()));
        contactRepository.deleteByIdAndUserId(model.getId(), userId);
    }

    public ResponseEntity<?> editContacts(List<ContactModel> models) {

        if (models.size() == 0) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        //User user = userServiceImpl.getCurrentUser();
        User user = userServiceImpl.findUserByEmail("mayra.cademartori@gmail.com");

        models.forEach(model -> {

            Optional<Contact> contactSearch = contactRepository.findByIdAndUserId(model.getId(), user.getId());

            if (contactSearch.isPresent()) {
                //return new ResponseEntity<>("Contato não encontrado", HttpStatus.BAD_REQUEST);


            Contact contact = contactSearch.get();

            boolean changed = ! model.getName().equals(contact.getName());
            if (changed)
                contact.setName(model.getName());

            if(! model.getDescription().equals(contact.getDescription())){
                changed = true;
                contact.setDescription(model.getDescription());
            }
            if(! model.getColor().equals(contact.getColor())){
                changed = true;
                contact.setColor(model.getColor());
            }
            if(! model.getFrontId().equals(contact.getFrontId())){
                changed = true;
                contact.setFrontId(model.getFrontId());
            }
            if(changed)
                contactRepository.save(contact);

            phoneServiceImpl.editPhonesDB(model.getPhones(), contact);
            }
        });


        return new ResponseEntity<>(HttpStatus.OK);

    }


}
