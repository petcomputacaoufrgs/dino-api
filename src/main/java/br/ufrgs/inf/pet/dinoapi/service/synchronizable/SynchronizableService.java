package br.ufrgs.inf.pet.dinoapi.service.synchronizable;

import br.ufrgs.inf.pet.dinoapi.constants.SynchronizableConstants;
import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableModel;
import br.ufrgs.inf.pet.dinoapi.repository.synchronizable.SynchronizableRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public abstract class SynchronizableService<T extends SynchronizableEntity> {
    protected final SynchronizableRepository<T> repository;
    protected final AuthServiceImpl authService;

    public SynchronizableService(SynchronizableRepository<T> repository, AuthServiceImpl authService) {
        this.repository = repository;
        this.authService = authService;
    }

    public ResponseEntity<?> synchronize(SynchronizableModel<T> model) {
        final User user = authService.getCurrentUser();

        if (user != null) {
            final Optional<T> serverSearch = repository.findByIdAndUserId(model.getId(), user.getId());
            if (serverSearch.isPresent()) {
                T entity = serverSearch.get();
                if (entity.isMoreUpdated(model)) {
                    entity = model.updateEntity(entity);
                    this.repository.save(entity);
                }
            }

            return new ResponseEntity<>(SynchronizableConstants.MODEl_ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(SynchronizableConstants.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
    }

}
