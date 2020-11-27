package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.GoogleContact;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import br.ufrgs.inf.pet.dinoapi.repository.contact.ContactRepository;
import br.ufrgs.inf.pet.dinoapi.repository.contact.GoogleContactRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.auth.google.GoogleAuthServiceImpl;
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
    private final AuthServiceImpl authServiceImpl;
    private final PhoneServiceImpl phoneServiceImpl;
    private final GoogleContactRepository googleContactRepository;
    private final GoogleAuthServiceImpl googleAuthService;

    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository, ContactVersionServiceImpl contactVersionServiceImpl, AuthServiceImpl authServiceImpl, PhoneServiceImpl phoneServiceImpl, GoogleContactRepository googleContactRepository, GoogleAuthServiceImpl googleAuthService) {
        this.contactRepository = contactRepository;
        this.contactVersionServiceImpl = contactVersionServiceImpl;
        this.phoneServiceImpl = phoneServiceImpl;
        this.authServiceImpl = authServiceImpl;
        this.googleContactRepository = googleContactRepository;
        this.googleAuthService = googleAuthService;
    }


    public ResponseEntity<List<ContactModel>> getUserContacts() {
            final User user = authServiceImpl.getCurrentUser();

            final List<Contact> contacts = contactRepository.findByUserIdWithGoogleContacts(user.getId());

            final List<ContactModel> response = contacts.stream().map(ContactModel::new).collect(Collectors.toList());

            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<SaveResponseModel> saveContact(ContactSaveModel model) {
            final User user = authServiceImpl.getCurrentUser();

            final Contact contact = contactRepository.save(new Contact(model, user));

            final ContactModel responseModel = saveContactRelatedData(user, model, contact);

            contactVersionServiceImpl.updateVersion(user);

            final SaveResponseModel response = new SaveResponseModel(user.getContactVersion().getVersion(), responseModel);

            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<SaveResponseModelAll> saveContacts(List<ContactSaveModel> models) {

        final User user = authServiceImpl.getCurrentUser();

        contactVersionServiceImpl.updateVersion(user);

        final List<ContactModel> responseModels = new ArrayList<>();

        models.forEach(modelContact -> {
            Contact contact = new Contact(modelContact, user);

            contact = contactRepository.save(contact);

            final ContactModel contactModel = saveContactRelatedData(user, modelContact, contact);

            responseModels.add(contactModel);
        });

        final SaveResponseModelAll response = new SaveResponseModelAll(user.getContactVersion().getVersion(), responseModels);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private ContactModel saveContactRelatedData(User user, ContactSaveModel modelContact, Contact contact) {
        contact.setPhones(phoneServiceImpl.savePhones(modelContact.getPhones(), contact));

        ContactModel responseModel = new ContactModel(contact);

        final String googleResourceName = modelContact.getResourceName();

        final Boolean hasGResourceName = googleResourceName != null && !googleResourceName.isEmpty();

        if (hasGResourceName) {
            final GoogleContact googleContact = new GoogleContact(contact, modelContact.getResourceName(), user);
            googleContactRepository.save(googleContact);
            responseModel.setResourceName(modelContact.getResourceName());
        }

        return responseModel;
    }

    public ResponseEntity<?> deleteContact(ContactDeleteModel model) {
        if (model == null || model.getId() == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        final User user = authServiceImpl.getCurrentUser();

        final Optional<Contact> contactToDeleteSearch = contactRepository.findByIdAndUserId(model.getId(), user.getId());

        if(contactToDeleteSearch.isPresent()) {
            final Contact contactToDelete = contactToDeleteSearch.get();

            contactRepository.delete(contactToDelete);

            contactVersionServiceImpl.updateVersion(user);
        }

        return new ResponseEntity<>(user.getContactVersion().getVersion(), HttpStatus.OK);
    }

    public ResponseEntity<Long> deleteContacts(List<ContactDeleteModel> models) {
        final User user = authServiceImpl.getCurrentUser();

        final List<Long> validIds = models.stream()
                .filter(Objects::nonNull)
                .map(ContactDeleteModel::getId)
                .collect(Collectors.toList());

        if (validIds.size() > 0) {
            final Optional<List<Contact>> contactsToDeleteSearch = contactRepository.findAllByIdAndUserId(validIds, user.getId());

            if (contactsToDeleteSearch.isPresent()) {
                final List<Contact> contactsToDelete = contactsToDeleteSearch.get();

                contactRepository.deleteAll(contactsToDelete);

                contactVersionServiceImpl.updateVersion(user);
            }
        }

        return new ResponseEntity<>(user.getContactVersion().getVersion(), HttpStatus.OK);
    }

    public ResponseEntity<?> editContact(ContactModel model) {
        final User user = authServiceImpl.getCurrentUser();

        final Optional<Contact> contactSearch = contactRepository.findByIdAndUserId(model.getId(), user.getId());

        if (contactSearch.isPresent()) {
            final Contact contact = contactSearch.get();

            checkEdits(contact, model, user);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user.getContactVersion().getVersion(), HttpStatus.OK);
    }

    public ResponseEntity<?> editContacts(List<ContactModel> models) {
        final User user = authServiceImpl.getCurrentUser();

        models.forEach(model -> {
            final Optional<Contact> contactSearch = contactRepository.findByIdAndUserId(model.getId(), user.getId());

            if (contactSearch.isPresent()) {
                final Contact contact = contactSearch.get();

                checkEdits(contact, model, user);
            }
        });

        if(models.size() > 0) {
            contactVersionServiceImpl.updateVersion(user);
        }

        return new ResponseEntity<>(user.getContactVersion().getVersion(), HttpStatus.OK);
    }

    public ResponseEntity<?> declineGoogleContacts() {
        return googleAuthService.declineGoogleContacts();
    }

    private void checkEdits(Contact contact, ContactModel model, User user) {
        boolean changed = ! model.getName().equals(contact.getName());
        if (changed) {
            contact.setName(model.getName());
        }
        if(!model.getDescription().equals(contact.getDescription())){
            changed = true;
            contact.setDescription(model.getDescription());
        }

        if (model.getColor() != null) {
            final Byte color = model.getColor();

            if(color == null || !color.equals(contact.getColor())){
                changed = true;
                contact.setColor(model.getColor());
            }
        }

        this.checkGResourceNameEdit(contact, model, user);

        if(changed) {
            contactRepository.save(contact);
        }
        phoneServiceImpl.editPhones(model.getPhones(), contact);
    }

    private void checkGResourceNameEdit(Contact contact, ContactModel model, User user) {
        final String gResourceName = model.getResourceName();

        final Boolean hasGResourceName = !gResourceName.isEmpty();

        if (hasGResourceName) {
            final Optional<GoogleContact> googleContactSearch = googleContactRepository.findByContactIdAndUserId(contact.getId(), user.getId());

            if (googleContactSearch.isPresent()) {
                final GoogleContact googleContact = googleContactSearch.get();
                if (!gResourceName.equals(googleContact.getResourceName())) {
                    googleContact.setResourceName(gResourceName);
                    googleContactRepository.save(googleContact);
                }
            }
        }
    }
}
