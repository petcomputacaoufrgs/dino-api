package br.ufrgs.inf.pet.dinoapi.service.synchronizable;

import br.ufrgs.inf.pet.dinoapi.constants.SynchronizableConstants;
import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.*;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

public abstract class SynchronizableService<
        ENTITY extends SynchronizableEntity,
        ID,
        DATA_MODEL extends SynchronizableDataModel,
        REPOSITORY extends CrudRepository<ENTITY, ID>> {
    protected final REPOSITORY repository;
    protected final AuthServiceImpl authService;

    public SynchronizableService(REPOSITORY repository, AuthServiceImpl authService) {
        this.repository = repository;
        this.authService = authService;
    }

    /**
     * Create a complete data model ({@link DATA_MODEL}) based in an entity ({@link ENTITY})
     * @param entity: base entity
     * @return data model
     */
    protected abstract DATA_MODEL createDataModel(ENTITY entity);

    /**
     * Create a new entity ({@link ENTITY}) based in a data model ({@link DATA_MODEL})
     * @param model: data model
     * @return entity
     */
    protected abstract ENTITY createEntity(DATA_MODEL model);

    /**
     * Update entity's ({@link ENTITY}) attributes based in a data model ({@link DATA_MODEL})
     * @param entity: entity
     * @param model: data model
     */
    protected abstract void updateEntity(ENTITY entity, DATA_MODEL model);

    /**
     * Get entity from database using userId for security validation (only takes data that the user has access)
     * @param id: entity's id
     * @param userId: user's id
     * @return database entity if valid params or null
     */
    protected abstract Optional<ENTITY> getEntityByIdAndUserId(Long id, Long userId);

    public final ResponseEntity<SynchronizableResponseModel> get(SynchronizableDeleteModel model) {
        final ENTITY entity = this.getEntity(model);
        final SynchronizableResponseModel response = new SynchronizableResponseModel();
        if (entity != null) {
            if (entity.isNewerThan(model)) {
                response.setSuccess(true);
                response.setData(this.createDataModel(entity));
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            response.setSuccess(true);
            response.setError(SynchronizableConstants.NOT_MODIFIED);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.setSuccess(false);
        response.setError(SynchronizableConstants.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public final ResponseEntity<SynchronizableResponseModel> save(SynchronizableRequestModel<DATA_MODEL> model) {
        final SynchronizableResponseModel response = new SynchronizableResponseModel();

        if (model.getData() != null) {
            final ENTITY entity = this.getEntity(model.getData());
            response.setSuccess(true);
            if (entity != null) {
                this.update(entity, model.getData());
            } else {
                SynchronizableDataModel data = this.create(model.getData());
                response.setData(data);
            }
        } else {
            response.setSuccess(false);
            response.setError(SynchronizableConstants.REQUEST_WITH_OUT_DATA);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public final ResponseEntity<SynchronizableResponseModel> delete(SynchronizableDeleteModel model) {
        final ENTITY entity = this.getEntity(model);
        final SynchronizableResponseModel response = new SynchronizableResponseModel();

        if (entity != null && entity.isOlderThan(model)) {
            repository.delete(entity);
            response.setSuccess(true);
        } else {
            response.setSuccess(false);
            response.setError(SynchronizableConstants.NOT_FOUND);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    protected final ENTITY getEntity(SynchronizableModel model) {
        final User user = authService.getCurrentUser();

        if (model.getId() != null) {
            final Optional<ENTITY> entitySearch = this.getEntityByIdAndUserId(model.getId(), user.getId());

            if (entitySearch.isPresent()) {
                return entitySearch.get();
            }
        }

        return null;
    }

    private DATA_MODEL create(DATA_MODEL model) {
        ENTITY entity = this.createEntity(model);
        entity.setLastUpdate(model.getLastUpdate());
        entity = repository.save(entity);

        return this.createDataModel(entity);
    }

    private void update(ENTITY entity, DATA_MODEL model) {
        if (entity.isOlderThan(model)) {
            this.updateEntity(entity, model);
            repository.save(entity);
        }
    }
}
