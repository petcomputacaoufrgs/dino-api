package br.ufrgs.inf.pet.dinoapi.controller.contacts;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.model.contacts.ContactDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.ContactRepository;
import br.ufrgs.inf.pet.dinoapi.service.contact.ContactServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/contact/")
public class ContactControllerImpl extends SynchronizableControllerImpl<
        Contact, Long, ContactDataModel, ContactRepository, ContactServiceImpl> {

    @Autowired
    public ContactControllerImpl(ContactServiceImpl service) {
        super(service);
    }
}
