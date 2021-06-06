package br.ufrgs.inf.pet.dinoapi.service.contact.async;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialPhone;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.contacts.PhoneDataModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.ContactServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.EssentialContactServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.PhoneServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AsyncEssentialPhoneService extends LogUtilsBase {
    private final ContactServiceImpl contactService;
    private final EssentialContactServiceImpl essentialContactService;
    private final ClockServiceImpl clockService;
    private final PhoneServiceImpl phoneService;

    @Autowired
    public AsyncEssentialPhoneService(LogAPIErrorServiceImpl logAPIErrorService,
                                      ContactServiceImpl contactService,
                                      EssentialContactServiceImpl essentialContactService,
                                      ClockServiceImpl clockService, PhoneServiceImpl phoneService) {
        super(logAPIErrorService);
        this.contactService = contactService;
        this.essentialContactService = essentialContactService;
        this.clockService = clockService;
        this.phoneService = phoneService;
    }

    @Async("contactThreadPoolTaskExecutor")
    public void createUsersPhones(EssentialPhone entity) {
        try {
            final Optional<EssentialContact> essentialContactSearch = essentialContactService.findById(entity.getEssentialContact().getId());

            essentialContactSearch.ifPresent(essentialContact -> {
                final List<Contact> contacts = contactService.findAllByEssentialContact(essentialContact);
                this.createPhones(entity, contacts);
            });
        } catch (Exception e) {
            this.logAPIError(e);
        }
    }

    @Async("contactThreadPoolTaskExecutor")
    public void updateUsersPhones(EssentialPhone entity) {
        final List<Contact> contactsWithoutPhone = contactService.findAllWhichShouldHaveEssentialPhoneButDoesnt(entity);
        if (contactsWithoutPhone.size() > 0) {
            this.createPhones(entity, contactsWithoutPhone);
        }

        final List<Phone> phones = phoneService.findAllByEssentialPhone(entity);
        for (Phone phone : phones) {
            try {
                final Contact contact = phone.getContact();
                final User user = phone.getContact().getUser();

                save(entity, contact, user, phone);
            } catch (ConvertModelToEntityException | AuthNullException e) {
                this.logAPIError(e);
            }
        }
    }

    @Async("contactThreadPoolTaskExecutor")
    public void deleteUsersPhones(List<Phone> phones) {
        for (Phone phone : phones) {
            try {
                final Contact contact = phone.getContact();
                final User user = contact.getUser();

                final SynchronizableDeleteModel<Long> deleteModel = new SynchronizableDeleteModel<>();
                deleteModel.setLastUpdate(clockService.getUTCZonedDateTime());
                deleteModel.setId(phone.getId());

                phoneService.deleteByUser(deleteModel, user);
            } catch (AuthNullException e) {
                this.logAPIError(e);
            }
        }
    }

    private void createPhones(EssentialPhone essentialPhone, List<Contact> contacts) {
        for (Contact contact : contacts) {
            try {
                final User user = contact.getUser();
                if (user.getUserAppSettings().getIncludeEssentialContact()) {
                    final Phone phone = new Phone();
                    phone.setType(essentialPhone.getType());
                    phone.setNumber(essentialPhone.getNumber());
                    phone.setContact(contact);
                    phone.setLastUpdate(LocalDateTime.now());
                    phone.setEssentialPhone(essentialPhone);
                    final Phone savedPhone = phoneService.saveDirectly(phone);

                    save(essentialPhone, contact, user, savedPhone);
                }
            } catch (AuthNullException | ConvertModelToEntityException e) {
                this.logAPIError(e);
            }
        }
    }

    private void save(EssentialPhone essentialPhone, Contact contact, User user, Phone savedPhone) throws AuthNullException, ConvertModelToEntityException {
        final PhoneDataModel phoneDataModel = new PhoneDataModel();
        phoneDataModel.setNumber(essentialPhone.getNumber());
        phoneDataModel.setContactId(contact.getId());
        phoneDataModel.setType(essentialPhone.getType());
        phoneDataModel.setLastUpdate(clockService.getUTCZonedDateTime());
        phoneDataModel.setId(savedPhone.getId());

        phoneService.saveByUser(phoneDataModel, user);
    }
}
