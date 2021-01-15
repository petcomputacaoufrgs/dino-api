package br.ufrgs.inf.pet.dinoapi.service.contact.async;

import br.ufrgs.inf.pet.dinoapi.communication.google.people.GooglePeopleCommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.GoogleContact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.contacts.PhoneDataModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.PhoneRepository;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.ContactServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.GoogleContactServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
public class AsyncPhoneService {
    private final ContactServiceImpl contactService;
    private final PhoneRepository phoneRepository;
    private final ClockServiceImpl clockService;
    private final GoogleContactServiceImpl googleContactService;
    private final GooglePeopleCommunicationImpl googlePeopleCommunication;

    @Autowired
    public AsyncPhoneService(ContactServiceImpl contactService, PhoneRepository phoneRepository,
                             ClockServiceImpl clockService, GoogleContactServiceImpl googleContactService,
                             GooglePeopleCommunicationImpl googlePeopleCommunication) {
        this.contactService = contactService;
        this.phoneRepository = phoneRepository;
        this.clockService = clockService;
        this.googleContactService = googleContactService;
        this.googlePeopleCommunication = googlePeopleCommunication;
    }

    @Async("threadPoolTaskExecutor")
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
                    final List<String> phoneNumbers = savedDataModels.stream().map(PhoneDataModel::getNumber).collect(Collectors.toList());
                    this.updateGoogleContactPhones(user, contact, phoneNumbers);
                }
            }
        }
    }

    @Async("threadPoolTaskExecutor")
    public void updatePhoneOnGoogleAPI(Phone entity, BiFunction<PhoneDataModel, Auth, PhoneDataModel> save) {
        final EssentialContact essentialContact = entity.getEssentialContact();

        if (essentialContact == null) return;

        final List<Phone> phones = phoneRepository.findAllByOriginalEssentialPhone(entity);

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
                this.updateGoogleContactPhones(user, contact);
            }
        }
    }

    @Async("threadPoolTaskExecutor")
    public void deletePhonesOnGoogleAPI(Phone entity, BiFunction<SynchronizableDeleteModel<Long>, Auth, Void> delete) {
        final List<Phone> phones = phoneRepository.findAllByOriginalEssentialPhone(entity);

        for (Phone phone : phones) {
            final Contact contact = phone.getContact();
            final User user = contact.getUser();

            final Auth fakeAuth = new Auth();
            fakeAuth.setUser(user);

            final SynchronizableDeleteModel<Long> model = new SynchronizableDeleteModel<>();
            model.setLastUpdate(clockService.getUTCZonedDateTime());
            model.setId(phone.getId());

            delete.apply(model, fakeAuth);

            this.updateGoogleContactPhones(user, contact);
        }
    }

    private void updateGoogleContactPhones(User user, Contact contact, List<String> phoneNumbers) {
        final Optional<GoogleContact> googleContactSearch = googleContactService.findByContactId(contact.getId());

        googleContactSearch.ifPresent(googleContact -> googlePeopleCommunication
                .updateContact(user, contact.getName(), contact.getDescription(), phoneNumbers, googleContact));
    }

    private void updateGoogleContactPhones(User user, Contact contact) {
        final Optional<GoogleContact> googleContactSearch = googleContactService.findByContactId(contact.getId());

        googleContactSearch.ifPresent(googleContact -> {
            final List<Phone> contactPhones = phoneRepository.findAllByContactId(contact.getId());
            final List<String> phoneNumbers = contactPhones.stream().map(Phone::getNumber).collect(Collectors.toList());

            googlePeopleCommunication.updateContact(user, contact.getName(), contact.getDescription(), phoneNumbers, googleContact);
        });
    }
}
