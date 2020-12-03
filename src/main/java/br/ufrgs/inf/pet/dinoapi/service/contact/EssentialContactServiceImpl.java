package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import br.ufrgs.inf.pet.dinoapi.model.contacts.EssentialContactModel;
import br.ufrgs.inf.pet.dinoapi.model.contacts.EssentialContactSaveModel;
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

    public ResponseEntity<?> saveEssentialContacts(List<EssentialContactSaveModel> models) {

        List<EssentialContact> itemsResponse = new ArrayList<>();

        models.forEach(model -> {

                Contact contact = new Contact(model);

                contact = contactRepository.save(contact);

                contact.setPhones(phoneServiceImpl.savePhones(model.getPhones(), contact));

                List<Long> faqIds = model.getFaqIds();

                if (faqIds != null) {

                    Contact finalContact = contact;

                    faqIds.forEach(faqId -> {

                        Optional<Faq> faqSearch = faqRepository.findById(faqId);

                        if (faqSearch.isPresent()) {

                            Faq faqDB = faqSearch.get();

                            itemsResponse.add(essentialContactRepository
                                    .save(new EssentialContact(faqDB, finalContact)));
                        }
                    });
                } else {
                    itemsResponse.add(essentialContactRepository
                            .save(new EssentialContact(contact)));
                }
        });

        List<EssentialContactModel> response = itemsResponse.stream()
                .map(EssentialContactModel::new)
                .collect(Collectors.toList());

        return response.size() > 0
                ? new ResponseEntity<>(response, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
