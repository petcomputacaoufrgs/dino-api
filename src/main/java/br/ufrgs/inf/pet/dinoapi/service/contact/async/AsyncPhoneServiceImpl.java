package br.ufrgs.inf.pet.dinoapi.service.contact.async;

import br.ufrgs.inf.pet.dinoapi.communication.google.people.GooglePeopleCommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.GoogleContact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.contacts.PhoneDataModel;
import br.ufrgs.inf.pet.dinoapi.model.google.people.GooglePeopleModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.PhoneRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.google.GoogleScopeServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.ContactServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.GoogleContactServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import br.ufrgs.inf.pet.dinoapi.service.user.UserSettingsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
public class AsyncPhoneServiceImpl extends LogUtilsBase implements AsyncPhoneService {
    private final ContactServiceImpl contactService;
    private final PhoneRepository phoneRepository;
    private final ClockServiceImpl clockService;
    private final GoogleContactServiceImpl googleContactService;
    private final GooglePeopleCommunicationImpl googlePeopleCommunication;
    private final GoogleScopeServiceImpl googleScopeService;
    private final UserSettingsServiceImpl userSettingsService;
    private final AsyncSaveGoogleContact asyncSaveGoogleContact;

    @Autowired
    public AsyncPhoneServiceImpl(ContactServiceImpl contactService, PhoneRepository phoneRepository,
                                 ClockServiceImpl clockService, GoogleContactServiceImpl googleContactService,
                                 GooglePeopleCommunicationImpl googlePeopleCommunication,
                                 GoogleScopeServiceImpl googleScopeService, AsyncSaveGoogleContactImpl asyncSaveGoogleContact,
                                 LogAPIErrorServiceImpl logAPIErrorService, UserSettingsServiceImpl userSettingsService) {
        super(logAPIErrorService);
        this.contactService = contactService;
        this.phoneRepository = phoneRepository;
        this.clockService = clockService;
        this.googleContactService = googleContactService;
        this.googlePeopleCommunication = googlePeopleCommunication;
        this.googleScopeService = googleScopeService;
        this.userSettingsService = userSettingsService;
        this.asyncSaveGoogleContact = asyncSaveGoogleContact;
    }

    @Override
    @Async("defaultThreadPoolTaskExecutor")
    public void createPhoneOnGoogleAPI(PhoneDataModel model, BiFunction<List<PhoneDataModel>, Auth, List<PhoneDataModel>> saveAll) {
        final Long essentialContactId = model.getEssentialContactId();

        if (essentialContactId == null) return;

        final List<Contact> contacts = contactService.findAllByEssentialContactId(essentialContactId);
        final List<Phone> phones = phoneRepository.findAllByEssentialContactId(essentialContactId);

        for (Contact contact : contacts) {
            final User user = contact.getUser();

            if (user.getUserAppSettings().getIncludeEssentialContact()) {
                final Auth fakeAuth = new Auth();
                fakeAuth.setUser(user);

                final List<PhoneDataModel> phoneDataModels = phones.stream().map(phone -> {
                    final PhoneDataModel phoneDataModel = new PhoneDataModel();
                    phoneDataModel.setType(phone.getType());
                    phoneDataModel.setNumber(phone.getNumber());
                    phoneDataModel.setContactId(contact.getId());
                    phoneDataModel.setLastUpdate(clockService.getUTCZonedDateTime());
                    phoneDataModel.setOriginalEssentialPhoneId(phone.getId());

                    return phoneDataModel;
                }).collect(Collectors.toList());

                final List<PhoneDataModel> savedDataModels = saveAll.apply(phoneDataModels, fakeAuth);

                if (savedDataModels.size() > 0) {
                    if (this.shouldSyncWithGoogleAPI(user)) {
                        final List<String> phoneNumbers = savedDataModels.stream().map(PhoneDataModel::getNumber).collect(Collectors.toList());
                        this.updateGoogleContactPhones(user, contact, phoneNumbers);
                    }
                }
            }
        }
    }

    @Override
    @Async("defaultThreadPoolTaskExecutor")
    public void updatePhoneOnGoogleAPI(Phone entity, BiFunction<PhoneDataModel, Auth, PhoneDataModel> save) {
        final EssentialContact essentialContact = entity.getEssentialContact();

        if (essentialContact == null) return;

        final List<Phone> phones = phoneRepository.findAllByOriginalEssentialPhone(entity.getId());

        for (Phone phone : phones) {
            final Contact contact = phone.getContact();
            final User user = contact.getUser();

            final Auth fakeAuth = new Auth();
            fakeAuth.setUser(user);

            final PhoneDataModel phoneDataModel = new PhoneDataModel();
            phoneDataModel.setType(phone.getType());
            phoneDataModel.setNumber(phone.getNumber());
            phoneDataModel.setContactId(phone.getContact().getId());
            phoneDataModel.setLastUpdate(clockService.getUTCZonedDateTime());
            phoneDataModel.setId(phone.getId());
            phoneDataModel.setOriginalEssentialPhoneId(entity.getId());

            final PhoneDataModel saveDataModel = save.apply(phoneDataModel, fakeAuth);

            if (saveDataModel != null) {
                if (this.shouldSyncWithGoogleAPI(user)) {
                    this.updateGoogleContactPhones(user, contact);
                }
            }
        }
    }

    @Async("defaultThreadPoolTaskExecutor")
    public void deletePhonesOnGoogleAPI(List<Phone> deletedEssentialPhones, BiFunction<SynchronizableDeleteModel<Long>, Auth, Void> delete) {
        for (Phone phone : deletedEssentialPhones) {
            final Contact contact = phone.getContact();
            final User user = contact.getUser();

            final Auth fakeAuth = new Auth();
            fakeAuth.setUser(user);

            final SynchronizableDeleteModel<Long> model = new SynchronizableDeleteModel<>();
            model.setLastUpdate(clockService.getUTCZonedDateTime());
            model.setId(phone.getId());

            delete.apply(model, fakeAuth);

            if (this.shouldSyncWithGoogleAPI(user)) {
                this.updateGoogleContactPhones(user, contact, phone);
            }
        }
    }

    private boolean shouldSyncWithGoogleAPI(User user) {
        return googleScopeService.hasGoogleContactScope(user)
                && userSettingsService.saveContactsOnGoogleAPI(user);
    }

    private void updateGoogleContactPhones(User user, Contact contact, List<String> phoneNumbers) {
        final Optional<GoogleContact> googleContactSearch = googleContactService.findByContactId(contact.getId());

        googleContactSearch.ifPresent(googleContact -> {
            final GooglePeopleModel model = googlePeopleCommunication.updateContact(user, contact.getName(), contact.getDescription(), phoneNumbers, googleContact.getResourceName());
            this.saveGoogleContact(user, model, googleContact);
        });
    }

    private void updateGoogleContactPhones(User user, Contact contact, Phone phone) {
        final Optional<GoogleContact> googleContactSearch = googleContactService.findByContactId(contact.getId());

        googleContactSearch.ifPresent(googleContact -> {
            final List<Phone> contactPhones = phoneRepository.findAllByContactId(contact.getId());
            final List<String> phoneNumbers = contactPhones.stream()
                    .filter(p -> !p.getId().equals(phone.getId()))
                    .map(Phone::getNumber).collect(Collectors.toList());

            final GooglePeopleModel model = googlePeopleCommunication.updateContact(user, contact.getName(), contact.getDescription(), phoneNumbers, googleContact.getResourceName());

            this.saveGoogleContact(user, model, googleContact);
        });
    }

    private void updateGoogleContactPhones(User user, Contact contact) {
        final Optional<GoogleContact> googleContactSearch = googleContactService.findByContactId(contact.getId());

        googleContactSearch.ifPresent(googleContact -> {
            final List<Phone> contactPhones = phoneRepository.findAllByContactId(contact.getId());
            final List<String> phoneNumbers = contactPhones.stream()
                    .map(Phone::getNumber).collect(Collectors.toList());

            final GooglePeopleModel model = googlePeopleCommunication.updateContact(user, contact.getName(), contact.getDescription(), phoneNumbers, googleContact.getResourceName());

            this.saveGoogleContact(user, model, googleContact);
        });
    }

    private void saveGoogleContact(User user, GooglePeopleModel model, GoogleContact googleContact) {
        this.asyncSaveGoogleContact.saveGoogleContact(user, googleContact.getId(), googleContact.getContact().getId(), model, (googleDataModel, auth) -> {
            try {
                this.googleContactService.saveByUser(googleDataModel, user);
            } catch (Exception e) {
                this.logAPIError(e);
            }
            return null;
        });
    }

}
