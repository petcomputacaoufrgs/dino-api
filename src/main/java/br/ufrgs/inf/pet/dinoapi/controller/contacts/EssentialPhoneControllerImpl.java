package br.ufrgs.inf.pet.dinoapi.controller.contacts;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialPhone;
import br.ufrgs.inf.pet.dinoapi.model.contacts.EssentialPhoneDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.EssentialPhoneRepository;
import br.ufrgs.inf.pet.dinoapi.service.contact.EssentialPhoneServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.ufrgs.inf.pet.dinoapi.constants.PathConstants.ESSENTIAL_PHONE;

@RestController
@RequestMapping(ESSENTIAL_PHONE)
public class EssentialPhoneControllerImpl extends SynchronizableControllerImpl<
        EssentialPhone, Long, EssentialPhoneDataModel, EssentialPhoneRepository, EssentialPhoneServiceImpl> {

    @Autowired
    protected EssentialPhoneControllerImpl(EssentialPhoneServiceImpl service) {
        super(service);
    }
}
