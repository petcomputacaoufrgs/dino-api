package br.ufrgs.inf.pet.dinoapi.service.synchronizable;

import br.ufrgs.inf.pet.dinoapi.entity.sync.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableModel;
import br.ufrgs.inf.pet.dinoapi.repository.synchronizable.SynchronizableRepository;
import java.util.Optional;

public abstract class SynchronizableService<T extends SynchronizableEntity> {
    private final SynchronizableRepository<T> repository;

    public SynchronizableService(SynchronizableRepository<T> repository) {
        this.repository = repository;
    }

    public void update(SynchronizableModel<T> model, User user) {
        final Optional<T> serverSearch = repository.findByIdAndUserId(model.getId(), user.getId());
        if (serverSearch.isPresent()) {
            T entity = serverSearch.get();
            if (entity.isMoreUpdated(model)) {
                entity = model.updateEntity(entity);
                this.repository.save(entity);
            }
        }
    }

}
