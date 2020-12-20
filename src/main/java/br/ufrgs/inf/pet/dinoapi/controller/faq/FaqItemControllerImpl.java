package br.ufrgs.inf.pet.dinoapi.controller.faq;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.faq.FaqRepository;
import br.ufrgs.inf.pet.dinoapi.service.faq.FaqServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/faq_item/")
public class FaqItemControllerImpl extends SynchronizableControllerImpl<
        Faq, Long, Integer, FaqDataModel, FaqRepository, FaqServiceImpl> {

    @Autowired
    protected FaqItemControllerImpl(FaqServiceImpl service) {
        super(service);
    }
}
