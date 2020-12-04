package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.contacts.ContactSaveModel;
import br.ufrgs.inf.pet.dinoapi.model.contacts.EssentialContactModel;
import br.ufrgs.inf.pet.dinoapi.model.contacts.EssentialContactSaveModel;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqIdModel;
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

    public ResponseEntity<?> saveEssentialContactAll(List<EssentialContactSaveModel> models) {

        List<EssentialContact> itemsResponse = new ArrayList<>();

        models.forEach(model -> {

            Contact contact = contactRepository.save(new Contact(model));

            contact.setPhones(phoneServiceImpl.savePhones(model.getPhones(), contact));

            List<Long> faqIds = model.getFaqIds();

            if (faqIds != null) {

                faqIds.forEach(faqId -> {

                    Optional<Faq> faqSearch = faqRepository.findById(faqId);

                    if (faqSearch.isPresent()) {

                        Faq faqDB = faqSearch.get();

                        Optional<EssentialContact> eContactSearch = essentialContactRepository
                                .findByEssentialContactNameAndFaqId(model.getName(), faqDB.getId());

                        if(eContactSearch.isEmpty()) {
                            itemsResponse.add(essentialContactRepository.save(new EssentialContact(faqDB, contact)));
                        }
                    }
                });
            } else {

                Optional<EssentialContact> eContactSearch = essentialContactRepository.findByEssentialContactName(model.getName());

                if(eContactSearch.isEmpty()) {
                    itemsResponse.add(essentialContactRepository.save(new EssentialContact(contact)));
                }
            }
        });

        List<EssentialContactModel> response = itemsResponse.stream()
                .map(EssentialContactModel::new).collect(Collectors.toList());

        if(response.size() > 0) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public void setUsersDefaultContacts(User user) {

        List<EssentialContact> eContactsSearch = essentialContactRepository.findByFaqIdIsNull();

        eContactsSearch.forEach(dContact ->
                contactServiceImpl.saveContactOnRepository(
                        new ContactSaveModel(dContact.getContact()), user)
        );
    }

    public ResponseEntity<?> setUserTreatmentContacts(FaqIdModel model) {

        final User user = authServiceImpl.getCurrentUser();

        Optional<Faq> faqSearch = faqRepository.findById(model.getId());

        if(faqSearch.isPresent()) {

            Optional<List<EssentialContact>> eContactsSearch = essentialContactRepository
                    .findByEssentialContactsByFaqId(faqSearch.get().getId());

            if (eContactsSearch.isPresent()) {
                eContactsSearch.get().forEach(dContact -> {
                    contactServiceImpl.saveContactOnRepository(
                            new ContactSaveModel(dContact.getContact()), user);
                });
                contactVersionServiceImpl.updateVersion(user);

                return new ResponseEntity<>(HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
