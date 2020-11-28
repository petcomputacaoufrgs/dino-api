package br.ufrgs.inf.pet.dinoapi.controller.teste;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableController;
import br.ufrgs.inf.pet.dinoapi.entity.teste.TesteEntity;
import br.ufrgs.inf.pet.dinoapi.service.teste.TesteService;
import org.springframework.beans.factory.annotation.Autowired;

public class TesteController extends SynchronizableController<TesteEntity, Teste> {
    @Autowired
    protected TesteController(TesteService service) {
        super(service);
    }
}
