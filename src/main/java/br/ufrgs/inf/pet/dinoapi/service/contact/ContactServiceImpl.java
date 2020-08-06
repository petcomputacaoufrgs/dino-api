package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import br.ufrgs.inf.pet.dinoapi.repository.contact.ContactRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final ContactVersionServiceImpl contactVersionServiceImpl;
    private final AuthServiceImpl authService;
    private final PhoneServiceImpl phoneServiceImpl;

    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository, ContactVersionServiceImpl contactVersionServiceImpl, AuthServiceImpl authService, PhoneServiceImpl phoneServiceImpl) {
        this.contactRepository = contactRepository;
        this.contactVersionServiceImpl = contactVersionServiceImpl;
        this.phoneServiceImpl = phoneServiceImpl;
        this.authService = authService;
    }


    public ResponseEntity<List<ContactModel>> getUserContacts() {

            User user = authService.getCurrentUser();

            List<Contact> contacts = user.getContacts();

            List<ContactModel> response = contacts.stream().map(ContactModel::new).collect(Collectors.toList());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }


        public ResponseEntity<SaveResponseModel> saveContact(ContactSaveModel model) {

            //fazer segurança

            User user = authService.getCurrentUser();

            Contact contact = contactRepository.save(new Contact(model, user));

            contact.setPhones(phoneServiceImpl.savePhones(model.getPhones(), contact));

            contactVersionServiceImpl.updateVersion(user);

            ContactModel responseModel = new ContactModel(contact);

            SaveResponseModel response = new SaveResponseModel(user.getContactVersion().getVersion(), responseModel);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        public ResponseEntity<SaveResponseModelAll> saveContacts(List<ContactSaveModel> models) {

            User user = authService.getCurrentUser();

            contactVersionServiceImpl.updateVersion(user);

            List<Contact> contacts = models.stream()
                    .map(modelContact -> {

                        Contact contact = new Contact(modelContact, user);

                        contact = contactRepository.save(contact);

                        contact.setPhones(phoneServiceImpl.savePhones(modelContact.getPhones(), contact));

                        return contact;
                    })
                    .collect(Collectors.toList());

            //ATUALIZAR OS ID
            List<ContactModel> responseModels = contacts.stream()
                    .map(ContactModel::new)
                    .collect(Collectors.toList());

            SaveResponseModelAll response = new SaveResponseModelAll(user.getContactVersion().getVersion(), responseModels);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    public ResponseEntity<?> deleteContact(ContactDeleteModel model) {

        if (model == null || model.getId() == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        User user = authService.getCurrentUser();

        Optional<Contact> contactToDeleteSearch = contactRepository.findByIdAndUserId(model.getId(), user.getId());

        if(contactToDeleteSearch.isPresent()) {
            Contact contactToDelete = contactToDeleteSearch.get();

            contactRepository.delete(contactToDelete);

            contactVersionServiceImpl.updateVersion(user);
        }

        return new ResponseEntity<>(user.getContactVersion().getVersion(), HttpStatus.OK);
    }

    public ResponseEntity<Long> deleteContacts(List<ContactDeleteModel> models) {

        User user = authService.getCurrentUser();

        List<Long> validIds = models.stream()
                .filter(Objects::nonNull)
                .map(ContactDeleteModel::getId)
                .collect(Collectors.toList());

        if (validIds.size() > 0) {

            Optional<List<Contact>> contactsToDeleteSearch = contactRepository.findAllByIdAndUserId(validIds, user.getId());

            if (contactsToDeleteSearch.isPresent()) {

                List<Contact> contactsToDelete = contactsToDeleteSearch.get();

                contactRepository.deleteAll(contactsToDelete);

                contactVersionServiceImpl.updateVersion(user);
            }
        }

        return new ResponseEntity<>(user.getContactVersion().getVersion(), HttpStatus.OK);
    }

    public ResponseEntity<?> editContact(ContactModel model) {

        User user = authService.getCurrentUser();

        Optional<Contact> contactSearch = contactRepository.findByIdAndUserId(model.getId(), user.getId());

        if (contactSearch.isPresent()) {

            Contact contact = contactSearch.get();

            checkEdits(contact, model);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(user.getContactVersion().getVersion(), HttpStatus.OK);
    }

    public ResponseEntity<?> editContacts(List<ContactModel> models) {

        User user = authService.getCurrentUser();

        List<ContactModel> responseFailed = new ArrayList<>();

        models.forEach(model -> {

            Optional<Contact> contactSearch = contactRepository.findByIdAndUserId(model.getId(), user.getId());

            if (contactSearch.isPresent()) {

                Contact contact = contactSearch.get();

                checkEdits(contact, model);
            }
            else responseFailed.add(model);
        });

        if(models.size() > responseFailed.size()) {
            contactVersionServiceImpl.updateVersion(user);
        }
        if(responseFailed.size() > 0) {
            return new ResponseEntity<>(responseFailed, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user.getContactVersion().getVersion(), HttpStatus.OK);
    }

    private void checkEdits(Contact contact, ContactModel model) {

        boolean changed = ! model.getName().equals(contact.getName());
        if (changed) {
            contact.setName(model.getName());
        }
        if(! model.getDescription().equals(contact.getDescription())){
            changed = true;
            contact.setDescription(model.getDescription());
        }
        if(! model.getColor().equals(contact.getColor())){
            changed = true;
            contact.setColor(model.getColor());
        }
        if(changed) {
            contactRepository.save(contact);
        }
        phoneServiceImpl.editPhones(model.getPhones(), contact);
    }


}
