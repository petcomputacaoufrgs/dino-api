package br.ufrgs.inf.pet.dinoapi.controller.teste;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.teste.TesteEntity;
import br.ufrgs.inf.pet.dinoapi.model.teste.TesteDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.teste.TesteRepository;
import br.ufrgs.inf.pet.dinoapi.service.teste.TesteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teste/")
public class TesteControllerImpl extends SynchronizableControllerImpl<TesteEntity, Long, TesteDataModel, TesteRepository, TesteService> {
    @Autowired
    protected TesteControllerImpl(TesteService service) {
        super(service);
    }
}
