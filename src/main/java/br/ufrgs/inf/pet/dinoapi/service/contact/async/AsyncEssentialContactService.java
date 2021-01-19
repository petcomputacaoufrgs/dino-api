package br.ufrgs.inf.pet.dinoapi.service.contact.async;

import br.ufrgs.inf.pet.dinoapi.communication.google.people.GooglePeopleCommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.GoogleContact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.contacts.ContactDataModel;
import br.ufrgs.inf.pet.dinoapi.model.contacts.EssentialContactDataModel;
import br.ufrgs.inf.pet.dinoapi.model.contacts.GoogleContactDataModel;
import br.ufrgs.inf.pet.dinoapi.model.google.people.GooglePeopleModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.service.auth.google.GoogleScopeServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.ContactServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.GoogleContactServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.PhoneServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.treatment.TreatmentServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AsyncEssentialContactService {
    private final TreatmentServiceImpl treatmentService;
    private final UserServiceImpl userService;
    private final ClockServiceImpl clockService;
    private final ContactServiceImpl contactService;
    private final PhoneServiceImpl phoneService;
    private final GoogleContactServiceImpl googleContactService;
    private final GooglePeopleCommunicationImpl googlePeopleCommunication;
    private final GoogleScopeServiceImpl googleScopeService;

    @Autowired
    public AsyncEssentialContactService(TreatmentServiceImpl treatmentService, UserServiceImpl userService,
                                        ClockServiceImpl clockService, ContactServiceImpl contactService,
                                        PhoneServiceImpl phoneService, GoogleContactServiceImpl googleContactService,
                                        GooglePeopleCommunicationImpl googlePeopleCommunication,
                                        GoogleScopeServiceImpl googleScopeService) {
        this.treatmentService = treatmentService;
        this.userService = userService;
        this.clockService = clockService;
        this.contactService = contactService;
        this.phoneService = phoneService;
        this.googleContactService = googleContactService;
        this.googlePeopleCommunication = googlePeopleCommunication;
        this.googleScopeService = googleScopeService;
    }

    @Async("essentialContactsThreadPoolTaskExecutor")
    public void createContactsOnGoogleAPI(EssentialContactDataModel model) throws AuthNullException, ConvertModelToEntityException {
        final List<Treatment> treatments = treatmentService.getEntitiesByIds(model.getTreatmentIds());

        List<User> users;
        if (treatments.size() > 0) {
            users = userService.findUserBySaveEssentialContactsAndTreatments(treatments);
        } else {
            users = userService.findUserBySaveEssentialContacts();
        }

        for (User user : users) {
            if(user.getUserAppSettings().getIncludeEssentialContact()) {
                final ContactDataModel contactDataModel = new ContactDataModel();
                contactDataModel.setEssentialContactId(model.getId());
                contactDataModel.setColor(model.getColor());
                contactDataModel.setDescription(model.getDescription());
                contactDataModel.setName(model.getName());
                contactDataModel.setLastUpdate(clockService.getUTCZonedDateTime());

                final ContactDataModel savedDataModel = contactService.saveByUser(contactDataModel, user);

                this.createGoogleContact(user, savedDataModel);
            }
        }
    }

    @Async("essentialContactsThreadPoolTaskExecutor")
    public void updateContactsOnGoogleAPI(EssentialContactDataModel model) throws AuthNullException, ConvertModelToEntityException {
        final List<Contact> contacts = contactService.findAllByEssentialContactId(model.getId());
        for (Contact contact : contacts) {
            final User user = contact.getUser();

            final ContactDataModel contactDataModel = new ContactDataModel();
            contactDataModel.setEssentialContactId(model.getId());
            contactDataModel.setColor(model.getColor());
            contactDataModel.setDescription(model.getDescription());
            contactDataModel.setName(model.getName());
            contactDataModel.setLastUpdate(clockService.getUTCZonedDateTime());
            contactDataModel.setId(contact.getId());

            final ContactDataModel savedDataModel = contactService.saveByUser(contactDataModel, user);
            final Long contactId = savedDataModel.getId();
            final Optional<GoogleContact> googleContactSearch = googleContactService.findByContactId(contactId);
            if (googleContactSearch.isEmpty()) {
                this.createGoogleContact(user, savedDataModel);
            } else {
                final GoogleContact googleContact = googleContactSearch.get();
                final String resourceName = googleContact.getResourceName();
                final List<String> phones = phoneService.findAllPhoneNumbersByContactId(contactId);
                final GooglePeopleModel googlePeopleModel = googlePeopleCommunication.updateContact(user, savedDataModel.getName(), savedDataModel.getDescription(), phones, resourceName);
                if (googlePeopleModel != null && !googlePeopleModel.getResourceName().equals(resourceName)) {
                    this.saveGoogleContact(user, googleContact.getId(), googleContact.getContact().getId(), googlePeopleModel);
                }
            }
        }
    }

    @Async("essentialContactsThreadPoolTaskExecutor")
    public void deleteContactsOnGoogleAPI(List<Contact> contacts) throws AuthNullException {
        for (Contact contact : contacts) {
            final User user = contact.getUser();

            final Optional<GoogleContact> googleContact = googleContactService.findByContactId(contact.getId());
            googleContact.ifPresent(value -> {
                if (googleScopeService.hasGoogleContactScope(user)) {
                    googlePeopleCommunication.deleteContact(user, value);
                }
            });

            final List<Phone> phones = phoneService.findAllByContactId(contact.getId());

            final List<SynchronizableDeleteModel<Long>> phoneModels = phones.stream().map(phone -> {
                final SynchronizableDeleteModel<Long> model = new SynchronizableDeleteModel<>();
                model.setLastUpdate(clockService.getUTCZonedDateTime());
                model.setId(phone.getId());
                return model;
            }).collect(Collectors.toList());
            phoneService.deleteAllByUser(phoneModels, user);

            final SynchronizableDeleteModel<Long> model = new SynchronizableDeleteModel<>();
            model.setLastUpdate(clockService.getUTCZonedDateTime());
            model.setId(contact.getId());
            contactService.deleteByUser(model, user);
        }
    }

    private void createGoogleContact(User user, ContactDataModel savedDataModel) throws AuthNullException, ConvertModelToEntityException {
        if (googleScopeService.hasGoogleContactScope(user)) {
            final GooglePeopleModel googlePeopleModel = googlePeopleCommunication.createContact(user, savedDataModel.getName(), savedDataModel.getDescription(), new ArrayList<>());

            this.saveGoogleContact(user, null, savedDataModel.getId(), googlePeopleModel);
        }
    }

    private void saveGoogleContact(User user, Long id, Long contactId, GooglePeopleModel googlePeopleModel) throws AuthNullException, ConvertModelToEntityException {
        final GoogleContactDataModel googleContactDataModel = new GoogleContactDataModel();
        googleContactDataModel.setContactId(contactId);
        googleContactDataModel.setLastUpdate(clockService.getUTCZonedDateTime());
        googleContactDataModel.setId(id);

        if (googlePeopleModel != null) {
            googleContactDataModel.setResourceName(googlePeopleModel.getResourceName());
        }

        googleContactService.saveByUser(googleContactDataModel, user);
    }
}
