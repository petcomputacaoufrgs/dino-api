package br.ufrgs.inf.pet.dinoapi.controller.contacts;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.model.contacts.EssentialContactDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.EssentialContactRepository;
import br.ufrgs.inf.pet.dinoapi.service.contact.EssentialContactServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/essential_contact/")
public class EssentialContactControllerImpl extends SynchronizableControllerImpl<
        EssentialContact, Long, EssentialContactDataModel, EssentialContactRepository, EssentialContactServiceImpl> {

    @Autowired
    public EssentialContactControllerImpl(EssentialContactServiceImpl service) {
        super(service);
    }
}
