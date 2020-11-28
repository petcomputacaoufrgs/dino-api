package br.ufrgs.inf.pet.dinoapi.service.teste;

import br.ufrgs.inf.pet.dinoapi.entity.teste.TesteEntity;
import br.ufrgs.inf.pet.dinoapi.repository.teste.TesteRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableService;
import org.springframework.beans.factory.annotation.Autowired;

public class TesteService extends SynchronizableService<TesteEntity> {
    @Autowired
    public TesteService(TesteRepository testeRepository, AuthServiceImpl authService) {
        super(testeRepository, authService);
    }


}
