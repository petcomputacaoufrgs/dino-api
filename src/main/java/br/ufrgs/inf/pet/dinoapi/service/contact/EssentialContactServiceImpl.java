package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import br.ufrgs.inf.pet.dinoapi.model.contacts.EssentialContactModel;
import br.ufrgs.inf.pet.dinoapi.model.contacts.EssentialContactSaveModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.ContactRepository;
import br.ufrgs.inf.pet.dinoapi.repository.contact.EssentialContactRepository;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EssentialContactServiceImpl {

    private final EssentialContactRepository essentialContactRepository;
    private final ContactRepository contactRepository;
    private final FaqRepository faqRepository;
    private final ContactVersionServiceImpl contactVersionServiceImpl;
    private final AuthServiceImpl authServiceImpl;
    private final ContactServiceImpl contactServiceImpl;
    private final PhoneServiceImpl phoneServiceImpl;

    @Autowired
    public EssentialContactServiceImpl(EssentialContactRepository essentialContactRepository, ContactRepository contactRepository,
                                       ContactVersionServiceImpl contactVersionServiceImpl, AuthServiceImpl authServiceImpl,
                                       PhoneServiceImpl phoneServiceImpl, ContactServiceImpl contactServiceImpl,
                                       FaqRepository faqRepository) {
        this.essentialContactRepository = essentialContactRepository;
        this.contactRepository = contactRepository;
        this.contactVersionServiceImpl = contactVersionServiceImpl;
        this.phoneServiceImpl = phoneServiceImpl;
        this.authServiceImpl = authServiceImpl;
        this.contactServiceImpl = contactServiceImpl;
        this.faqRepository = faqRepository;
    }

    /*
    public ResponseEntity<List<ContactModel>> getUserContacts() {
        User user = authServiceImpl.getCurrentUser();

        List<Contact> contacts = contactRepository.findByUserId(user.getId());

        List<ContactModel> response = contacts.stream().map(ContactModel::new).collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    public ResponseEntity<SaveResponseModel> saveContact(ContactSaveModel model) {

        //fazer segurança

        User user = authServiceImpl.getCurrentUser();

        Contact contact = contactRepository.save(new Contact(model, user));

        contact.setPhones(phoneServiceImpl.savePhones(model.getPhones(), contact));

        contactVersionServiceImpl.updateVersion(user);

        ContactModel responseModel = new ContactModel(contact);

        SaveResponseModel response = new SaveResponseModel(user.getContactVersion().getVersion(), responseModel);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

     */

    public ResponseEntity<?> saveEssentialContacts(List<EssentialContactSaveModel> models) {

        List<EssentialContact> itemsResponse = new ArrayList<>();

        models.forEach(model -> {

                Contact contact = new Contact(model);

                contact = contactRepository.save(contact);

                contact.setPhones(phoneServiceImpl.savePhones(model.getPhones(), contact));

                List<Long> faqIds = model.getFaqIds();

                if (faqIds != null) {

                    Contact finalContact = contact;

                    faqIds.forEach(faqId -> {

                        Optional<Faq> faqSearch = faqRepository.findById(faqId);

                        if (faqSearch.isPresent()) {

                            Faq faqDB = faqSearch.get();

                            itemsResponse.add(essentialContactRepository
                                    .save(new EssentialContact(faqDB, finalContact)));
                        }
                    });
                } else {
                    itemsResponse.add(essentialContactRepository
                            .save(new EssentialContact(contact)));
                }
        });

        List<EssentialContactModel> response = itemsResponse.stream()
                .map(EssentialContactModel::new)
                .collect(Collectors.toList());

        return response.size() > 0
                ? new ResponseEntity<>(response, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /*

    public ResponseEntity<?> deleteContact(ContactDeleteModel model) {

        if (model == null || model.getId() == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        User user = authServiceImpl.getCurrentUser();

        Optional<Contact> contactToDeleteSearch = contactRepository.findByIdAndUserId(model.getId(), user.getId());

        if(contactToDeleteSearch.isPresent()) {
            Contact contactToDelete = contactToDeleteSearch.get();

            contactRepository.delete(contactToDelete);

            contactVersionServiceImpl.updateVersion(user);
        }

        return new ResponseEntity<>(user.getContactVersion().getVersion(), HttpStatus.OK);
    }

    public ResponseEntity<Long> deleteContacts(List<ContactDeleteModel> models) {

        User user = authServiceImpl.getCurrentUser();

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

        User user = authServiceImpl.getCurrentUser();

        Optional<Contact> contactSearch = contactRepository.findByIdAndUserId(model.getId(), user.getId());

        if (contactSearch.isPresent()) {

            Contact contact = contactSearch.get();

            checkEdits(contact, model);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(user.getContactVersion().getVersion(), HttpStatus.OK);
    }

    public ResponseEntity<?> editContacts(List<ContactModel> models) {

        User user = authServiceImpl.getCurrentUser();

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

     */

}
