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
import br.ufrgs.inf.pet.dinoapi.service.contact.GoogleContactServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.PhoneServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import br.ufrgs.inf.pet.dinoapi.service.treatment.TreatmentServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AsyncEssentialContactService extends LogUtilsBase {
    private final TreatmentServiceImpl treatmentService;
    private final UserServiceImpl userService;
    private final ClockServiceImpl clockService;
    private final ContactServiceImpl contactService;
    private final GoogleContactServiceImpl googleContactService;
    private final PhoneServiceImpl phoneService;

    @Autowired
    public AsyncEssentialContactService(TreatmentServiceImpl treatmentService, UserServiceImpl userService,
                                        ClockServiceImpl clockService, ContactServiceImpl contactService,
                                        LogAPIErrorServiceImpl logAPIErrorService,
                                        GoogleContactServiceImpl googleContactService, PhoneServiceImpl phoneService) {
        super(logAPIErrorService);
        this.treatmentService = treatmentService;
        this.userService = userService;
        this.clockService = clockService;
        this.contactService = contactService;
        this.googleContactService = googleContactService;
        this.phoneService = phoneService;
    }

    @Async("contactsThreadPool")
    public void createUsersContacts(EssentialContact entity) {
        final List<Treatment> treatments = treatmentService.getEntitiesByEssentialContact(entity);

        List<User> users;
        if (treatments.size() > 0) {
            users = userService.findUserBySaveEssentialContactsAndTreatments(treatments);
        } else {
            users = userService.findUserBySaveEssentialContacts();
        }

        for (User user : users) {
            try {
                ContactDataModel contactDataModel = new ContactDataModel();
                contactDataModel.setEssentialContactId(entity.getId());
                contactDataModel.setColor(entity.getColor());
                contactDataModel.setDescription(entity.getDescription());
                contactDataModel.setName(entity.getName());
                contactDataModel.setLastUpdate(clockService.getUTCZonedDateTime());

                contactDataModel = contactService.saveByUser(contactDataModel, user);

                if (contactDataModel.getId() != null) {
                    final Optional<Contact> contact = contactService.findById(contactDataModel.getId());
                    contact.ifPresent(value -> this.googleContactService.createNewGoogleContact(value, user));
                }
            } catch (AuthNullException | ConvertModelToEntityException e) {
                this.logAPIError(e);
            }
        }
    }

    @Async("contactsThreadPool")
    public void updateUsersContacts(EssentialContact entity) {
        final List<Contact> contacts = contactService.findAllByEssentialContact(entity);
        for (Contact contact : contacts) {
            try {
                final User user = contact.getUser();

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

    @Async("contactsThreadPool")
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
}
