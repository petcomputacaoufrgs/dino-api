package br.ufrgs.inf.pet.dinoapi.service.contact.async;

import br.ufrgs.inf.pet.dinoapi.communication.google.people.GooglePeopleCommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.GoogleContact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.enumerable.PermissionEnum;
import br.ufrgs.inf.pet.dinoapi.model.google.people.GooglePeopleModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.PhoneRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.google.GoogleScopeServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.GoogleContactServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AsyncPhoneService extends LogUtilsBase {
    private final PhoneRepository phoneRepository;
    private final GoogleContactServiceImpl googleContactService;
    private final GooglePeopleCommunicationImpl googlePeopleCommunication;
    private final GoogleScopeServiceImpl googleScopeService;

    @Autowired
    public AsyncPhoneService(PhoneRepository phoneRepository, GoogleScopeServiceImpl googleScopeService,
                             GoogleContactServiceImpl googleContactService,
                             GooglePeopleCommunicationImpl googlePeopleCommunication,
                             LogAPIErrorServiceImpl logAPIErrorService) {
        super(logAPIErrorService);
        this.phoneRepository = phoneRepository;
        this.googleContactService = googleContactService;
        this.googlePeopleCommunication = googlePeopleCommunication;
        this.googleScopeService = googleScopeService;
    }

    @Async("contactsThreadPool")
    public void updateGoogleContactPhones(User user, Phone phone) {
        if (googleScopeService.hasGoogleContactScope(user)) {
            final Contact contact = phone.getContact();
            final Optional<GoogleContact> googleContactSearch = googleContactService.findByContactId(contact.getId());

            googleContactSearch.ifPresent(googleContact -> {
                final List<Phone> contactPhones = phoneRepository.findAllByContactId(contact.getId());
                final List<String> phoneNumbers = contactPhones.stream().map(Phone::getNumber).collect(Collectors.toList());

                final GooglePeopleModel model = googlePeopleCommunication.updateContact(user, contact.getName(), contact.getDescription(), phoneNumbers, googleContact);

                if (model != null && !model.getResourceName().equals(googleContact.getResourceName())) {
                    googleContact.setResourceName(model.getResourceName());
                    googleContactService.save(googleContact);
                }
            });
        }
    }
}
