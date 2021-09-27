package br.ufrgs.inf.pet.dinoapi.service.contact.async;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.GoogleContact;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.enumerable.PermissionEnum;
import br.ufrgs.inf.pet.dinoapi.repository.contact.ContactRepository;
import br.ufrgs.inf.pet.dinoapi.service.contact.GoogleContactServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Async("contactThreadPoolTaskExecutor")
    public void createContact(Contact entity, Auth auth) {
        final User user = auth.getUser();

        googleContactService.createNewGoogleContact(entity, user);
    }

    @Async("contactThreadPoolTaskExecutor")
    public void updateContact(Contact entity, Auth auth) {
        final User user = auth.getUser();

        final Optional<GoogleContact> googleContactSearch = this.googleContactService.findByContactId(entity.getId());

        if (googleContactSearch.isPresent()) {
            googleContactService.updateGoogleContact(entity, googleContactSearch.get());
        } else {
            googleContactService.createNewGoogleContact(entity, user);
        }
    }

    @Async("contactThreadPoolTaskExecutor")
    public void deleteContact(GoogleContact googleContact, Auth auth) {
        final User user = auth.getUser();
        final boolean hasUserPermission = user.getPermission().equals(PermissionEnum.USER.getValue());

        if (hasUserPermission) {
            googleContactService.deleteGoogleContact(googleContact, user);
        }
    }

    @Async("contactThreadPoolTaskExecutor")
    public void asyncUpdateAllUserGoogleContacts(User user) {
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
