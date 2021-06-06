package br.ufrgs.inf.pet.dinoapi.service.contact.async;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.contacts.ContactDataModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.ContactServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.PhoneServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import br.ufrgs.inf.pet.dinoapi.service.treatment.TreatmentServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsyncEssentialContactService extends LogUtilsBase {
    private final TreatmentServiceImpl treatmentService;
    private final UserServiceImpl userService;
    private final ClockServiceImpl clockService;
    private final ContactServiceImpl contactService;
    private final PhoneServiceImpl phoneService;

    @Autowired
    public AsyncEssentialContactService(TreatmentServiceImpl treatmentService, UserServiceImpl userService,
                                        ClockServiceImpl clockService, ContactServiceImpl contactService,
                                        LogAPIErrorServiceImpl logAPIErrorService, PhoneServiceImpl phoneService) {
        super(logAPIErrorService);
        this.treatmentService = treatmentService;
        this.userService = userService;
        this.clockService = clockService;
        this.contactService = contactService;
        this.phoneService = phoneService;
    }

    @Async("contactThreadPoolTaskExecutor")
    public void createUsersContacts(EssentialContact entity) {
        final List<Treatment> treatments = treatmentService.getEntitiesByEssentialContact(entity);

        List<User> users;
        if (treatments.size() > 0) {
            users = userService.findWhoCanHaveEssentialContacts(treatments);
        } else {
            users = userService.findWhoCanHaveEssentialContacts();
        }

        this.createUserContacts(users, entity);
    }

    @Async("contactThreadPoolTaskExecutor")
    public void updateUsersContacts(EssentialContact entity) {
        final List<Treatment> contactTreatments = entity.getTreatments();
        final List<Contact> contacts = contactService.findAllByEssentialContact(entity);

        List<User> users;
        if (contactTreatments.size() > 0) {
            users = userService.findWhoCanHaveEssentialContactsButDontHave(contactTreatments, entity);
        } else {
            users = userService.findWhoCanHaveEssentialContactsButDontHave(entity);
        }

        if (users.size() > 0) {
            createUserContacts(users, entity);
        }

        for (Contact contact : contacts) {
            try {
                final User user = contact.getUser();

                if (contactTreatments.size() > 0) {
                    final Treatment userTreatment = user.getUserAppSettings().getTreatment();
                    final boolean eContactHasUserTreatment = contactTreatments.stream()
                            .anyMatch(contactTreatment -> contactTreatment.getId().equals(userTreatment.getId()));
                    if (!eContactHasUserTreatment) {
                        this.deletePhones(contact, user);
                        this.deleteContact(contact, user);
                        return;
                    }
                }

                final ContactDataModel contactDataModel = new ContactDataModel();
                contactDataModel.setEssentialContactId(entity.getId());
                contactDataModel.setColor(entity.getColor());
                contactDataModel.setDescription(entity.getDescription());
                contactDataModel.setName(entity.getName());
                contactDataModel.setLastUpdate(clockService.getUTCZonedDateTime());
                contactDataModel.setId(contact.getId());

                contactService.saveByUser(contactDataModel, user);
            } catch (ConvertModelToEntityException | AuthNullException e) {
                this.logAPIError(e);
            }
        }
    }

    @Async("contactThreadPoolTaskExecutor")
    public void deleteContacts(List<Contact> contacts) {
        for (Contact contact : contacts) {
            try {
                final List<Phone> phones = phoneService.findAllByContactId(contact.getId());

                if (phones.size() == 0) {
                    final User user = contact.getUser();

                    final SynchronizableDeleteModel<Long> model = new SynchronizableDeleteModel<>();
                    model.setLastUpdate(clockService.getUTCZonedDateTime());
                    model.setId(contact.getId());
                    contactService.deleteByUser(model, user);
                }
            } catch (AuthNullException e) {
                this.logAPIError(e);
            }
        }
    }

    private void createUserContacts(List<User> users, EssentialContact entity) {
        for (User user : users) {
            try {
                final Contact contact = new Contact();
                contact.setEssentialContact(entity);
                contact.setLastUpdate(LocalDateTime.now());
                contact.setUser(user);
                contact.setName(entity.getName());

                final Contact savedContact = contactService.saveDirectly(contact);

                final ContactDataModel contactDataModel = new ContactDataModel();
                contactDataModel.setColor(entity.getColor());
                contactDataModel.setDescription(entity.getDescription());
                contactDataModel.setName(entity.getName());
                contactDataModel.setLastUpdate(clockService.getUTCZonedDateTime());
                contactDataModel.setId(savedContact.getId());

                contactService.saveByUser(contactDataModel, user);
            } catch (AuthNullException | ConvertModelToEntityException e) {
                this.logAPIError(e);
            }
        }
    }

    private void deletePhones(Contact contact, User user) throws AuthNullException {
        final List<Phone> userPhones = phoneService.findAllByContactId(contact.getId());
        final List<SynchronizableDeleteModel<Long>> deletePhoneModels = userPhones.stream().map(phone -> {
            final SynchronizableDeleteModel<Long> deleteModel = new SynchronizableDeleteModel<>();
            deleteModel.setLastUpdate(clockService.getUTCZonedDateTime());
            deleteModel.setId(phone.getId());
            return deleteModel;
        }).collect(Collectors.toList());
        phoneService.deleteAllByUser(deletePhoneModels, user);
    }

    private void deleteContact(Contact contact, User user) throws AuthNullException {
        final SynchronizableDeleteModel<Long> deleteContactModel = new SynchronizableDeleteModel<>();
        deleteContactModel.setLastUpdate(clockService.getUTCZonedDateTime());
        deleteContactModel.setId(contact.getId());
        contactService.deleteByUser(deleteContactModel, user);
    }
}
