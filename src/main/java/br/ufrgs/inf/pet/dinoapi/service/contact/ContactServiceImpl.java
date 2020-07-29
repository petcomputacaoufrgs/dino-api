package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.NoteVersion;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.*;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import br.ufrgs.inf.pet.dinoapi.repository.contact.*;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class ContactServiceImpl implements ContactService {

        @Autowired
        ContactRepository contactRepository;
        @Autowired
        ContactVersionServiceImpl contactVersionServiceImpl;
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

        public ResponseEntity<List<ContactModel>> getUserContacts() {

            User user = userServiceImpl.getCurrentUser();

            List<Contact> contacts = user.getContacts();

            List<ContactModel> response = contacts.stream().map(ContactModel::new).collect(Collectors.toList());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        public Contact saveContactDB(ContactSaveModel model, User user) {

            //não estou botando data nas versões

            Contact contact = new Contact(model, user);

            contact = contactRepository.save(contact);

            contact.setPhones(phoneServiceImpl.savePhonesDB(model.getPhones(), contact));

            return contact;
        }


        public ResponseEntity<ContactResponseModel> saveContact(ContactSaveModel model) {

            User user = userServiceImpl.getCurrentUser();

            ContactModel responseModel = new ContactModel(saveContactDB(model, user));

            contactVersionServiceImpl.updateVersion(user);

            ContactResponseModel response = new ContactResponseModel(user.getContactVersion().getVersion(), responseModel);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        public ResponseEntity<ContactResponseModel> saveContacts(List<ContactSaveModel> models) {

            User user = userServiceImpl.getCurrentUser();

            contactVersionServiceImpl.updateVersion(user);

            List<Contact> contacts = models.stream()
                    .map(modelContact -> saveContactDB(modelContact, user))
                    .collect(Collectors.toList());

            //ATUALIZAR OS ID
            List<ContactModel> responseModels = contacts.stream()
                    .map(ContactModel::new)
                    .collect(Collectors.toList());

            ContactResponseModel response = new ContactResponseModel(user.getContactVersion().getVersion(), responseModels);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }


    public ResponseEntity<?> deleteContact(ContactDeleteModel model) {

        if (model == null || model.getId() == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        User user = userServiceImpl.getCurrentUser();

        Optional<Contact> contactToDeleteSearch = contactRepository.findByIdAndUserId(model.getId(), user.getId());

        if(contactToDeleteSearch.isPresent()) {
            Contact contactToDelete = contactToDeleteSearch.get();

            //phoneRepository.deleteAll(contactToDelete.getPhones());

            //botar o user id
            //contactRepository.deleteByIdAndUserId(contactToDelete.getId(), user.getId());
            contactRepository.delete(contactToDelete);

            contactVersionServiceImpl.updateVersion(user);
        }

        return new ResponseEntity<>(user.getContactVersion().getVersion(), HttpStatus.OK);
    }

    public ResponseEntity<?> deleteContacts(List<ContactDeleteModel> models) {
        // ver depois com o JP
        User user = userServiceImpl.getCurrentUser();

        List<Long> validIds = models.stream()
                .filter(Objects::nonNull)
                .map(ContactDeleteModel::getId)
                .collect(Collectors.toList());

        int deletedItems = 0;

        if (validIds.size() > 0) {

            /*
            Optional<List<Contact>> contactsToDeleteSearch = contactRepository.findAllByIdAndUserId(validIds, user.getId());

            if (contactsToDeleteSearch.isPresent()) {

                List<Contact> contactsToDelete = contactsToDeleteSearch.get();

                contactsToDelete.forEach(c -> phoneRepository.deleteAll(c.getPhones()));

             */
            //ver se esse user id é o da requisição

                //deletedItems = contactRepository.deleteAll(validIds);
                deletedItems = contactRepository.deleteAllByIdAndUserId(validIds, user.getId());


                if (deletedItems > 0) {
                    contactVersionServiceImpl.updateVersion(user);
                }
            //}
        }

        return new ResponseEntity<>(user.getContactVersion().getVersion(), HttpStatus.OK);
    }

    public ResponseEntity<?> editContacts(List<ContactModel> models) {

        User user = userServiceImpl.getCurrentUser();

        List<ContactModel> responseFailed = new ArrayList<>();

        models.forEach(model -> {

            Optional<Contact> contactSearch = contactRepository.findByIdAndUserId(model.getId(), user.getId());

            if (contactSearch.isPresent()) {

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
            else responseFailed.add(model);
            });

        if(models.size() > responseFailed.size())
            contactVersionServiceImpl.updateVersion(user);

        if(responseFailed.size() > 0)
            return new ResponseEntity<>(responseFailed, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(user.getContactVersion().getVersion(), HttpStatus.OK);

    }


}
