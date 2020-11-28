package br.ufrgs.inf.pet.dinoapi.service.teste;

import br.ufrgs.inf.pet.dinoapi.entity.teste.TesteEntity;
import br.ufrgs.inf.pet.dinoapi.model.teste.TesteDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.teste.TesteRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TesteService extends SynchronizableService<TesteEntity, Long, TesteDataModel, TesteRepository> {
    @Autowired
    public TesteService(TesteRepository repository, AuthServiceImpl authService) {
        super(repository, authService);
    }

    @Override
    protected TesteDataModel createDataModel(TesteEntity entity) {
        final TesteDataModel model = new TesteDataModel(entity);
        model.setName(entity.getName());

        return model;
    }

    @Override
    protected TesteEntity createEntity(TesteDataModel model) {
        final TesteEntity entity = new TesteEntity();
        entity.setName(model.getName());
        return null;
    }

    @Override
    protected void updateEntity(TesteEntity entity, TesteDataModel model) {
        entity.setName(model.getName());
    }

    @Override
    protected Optional<TesteEntity> getEntityByIdAndUserId(Long id, Long userId) {
        return repository.findByIdAndUserId(id, userId);
    }
}
