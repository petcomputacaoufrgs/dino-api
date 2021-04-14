package br.ufrgs.inf.pet.dinoapi.service.contact.async;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.GoogleContact;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.repository.contact.ContactRepository;
import br.ufrgs.inf.pet.dinoapi.service.contact.GoogleContactServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsyncGoogleContactService extends LogUtilsBase {
    private final ContactRepository contactRepository;
    private final GoogleContactServiceImpl googleContactService;

    @Autowired
    public AsyncGoogleContactService(LogAPIErrorServiceImpl logAPIErrorService, ContactRepository contactRepository,
                                     @Lazy GoogleContactServiceImpl googleContactService) {
        super(logAPIErrorService);
        this.contactRepository = contactRepository;
        this.googleContactService = googleContactService;
    }

    @Async("contactsThreadPool")
    public void updateUserGoogleContacts(User user) {
        try {
            final List<Contact> contacts = contactRepository.findAllByUserOrderById(user.getId());
            final List<GoogleContact> googleContacts = googleContactService.findAllByUserOrderByContactId(user);
            int index = 0;
            for(Contact contact : contacts) {
                GoogleContact googleContact = null;
                boolean hasGoogleContact = false;

                if (index < googleContacts.size()) {
                    googleContact = googleContacts.get(index);
                    hasGoogleContact = googleContact.getContact().getId().equals(contact.getId());
                }

                if (hasGoogleContact) {
                    googleContactService.updateGoogleContact(contact, googleContact);
                    index++;
                } else {
                    googleContactService.createNewGoogleContact(contact, user);
                }
            }
        } catch (Exception e) {
            this.logAPIError(e);
        }
    }
}
