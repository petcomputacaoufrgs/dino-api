package br.ufrgs.inf.pet.dinoapi.controller.faq;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import br.ufrgs.inf.pet.dinoapi.model.faq.*;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqRepository;
import br.ufrgs.inf.pet.dinoapi.service.faq.FaqServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/faq/")
public class FaqControllerImpl extends SynchronizableControllerImpl<
        Faq, Long, FaqDataModel, FaqRepository, FaqServiceImpl> {

    @Autowired
    protected FaqControllerImpl(FaqServiceImpl service) {
        super(service);
    }
}


