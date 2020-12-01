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

/**
 * Service with get, save/update and delete for synchronizable entity
 *
 * @param <ENTITY> Synchronizable entity
 * @param <ID> Id type of synchronizable entity
 * @param <DATA_MODEL> Data model of synchronizable entity
 * @param <REPOSITORY> Repository of synchronizable entity
 */
public abstract class SynchronizableService<
        ENTITY extends SynchronizableEntity<ID>,
        ID,
        DATA_MODEL extends SynchronizableDataModel<ID, ENTITY>,
        REPOSITORY extends CrudRepository<ENTITY, ID>> {
    protected final REPOSITORY repository;
    protected final AuthServiceImpl authService;

    public SynchronizableService(REPOSITORY repository, AuthServiceImpl authService) {
        this.repository = repository;
        this.authService = authService;
    }

    /**
     * Create a complete data model ({@link DATA_MODEL}) based in an entity ({@link ENTITY})
     * @exception NullPointerException service will throws this exception if this method returns null
     * @param entity: base entity
     * @return data model
     */
    protected abstract DATA_MODEL createDataModel(ENTITY entity);

    /**
     * Create a new entity ({@link ENTITY}) based in a data model ({@link DATA_MODEL})
     * @exception NullPointerException service will throws this exception if this method returns null
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
    protected abstract Optional<ENTITY> getEntityByIdAndUserId(ID id, Long userId);

    public ResponseEntity<SynchronizableResponseModel> get(SynchronizableGetModel<ID> model) {
        final ENTITY entity = this.getEntity(model);
        final SynchronizableResponseModel response = new SynchronizableResponseModel();

        if (entity != null) {
            response.setSuccess(true);
            response.setData(this.createDataModel(entity));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.setSuccess(false);
        response.setError(SynchronizableConstants.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<SynchronizableResponseModel> save(DATA_MODEL model) {
        final SynchronizableResponseModel response = new SynchronizableResponseModel();

        if (model != null) {
            response.setSuccess(true);
            final DATA_MODEL data;
            final ENTITY entity = this.getEntity(model);
            if (entity != null) {
                data = this.update(entity, model);
            } else {
                data = this.create(model);
            }
            response.setData(data);
        } else {
            response.setSuccess(false);
            response.setError(SynchronizableConstants.REQUEST_WITH_OUT_DATA);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<SynchronizableResponseModel> delete(SynchronizableDeleteModel<ID> model) {
        final SynchronizableResponseModel response = new SynchronizableResponseModel();
        final ENTITY entity = this.getEntity(model);
        if (entity != null) {
                if (entity.isOlderOrEqualThan(model)) {
                    response.setSuccess(true);
                    repository.delete(entity);
                } else {
                    response.setSuccess(false);
                    response.setError(SynchronizableConstants.YOUR_VERSION_IS_OUTDATED);
                    response.setData(this.createDataModel(entity));
                }
        } else {
            response.setSuccess(false);
            response.setError(SynchronizableConstants.NOT_FOUND);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    protected ENTITY getEntity(SynchronizableIdModel<ID> model) {
        final User user = authService.getCurrentUser();

        if (model.getId() != null) {
            final Optional<ENTITY> entitySearch = this.getEntityByIdAndUserId(model.getId(), user.getId());

            if (entitySearch.isPresent()) {
                return entitySearch.get();
            }
        }

        return null;
    }

    protected DATA_MODEL create(DATA_MODEL model) {
        ENTITY entity = this.createEntity(model);
        entity.setLastUpdate(model.getLastUpdate());
        entity = repository.save(entity);

        return this.createDataModel(entity);
    }

    protected DATA_MODEL update(ENTITY entity, DATA_MODEL model) {
        if (entity.isOlderThan(model)) {
            this.updateEntity(entity, model);
            entity.setLastUpdate(model.getLastUpdate());
            entity = repository.save(entity);
        }
        return this.createDataModel(entity);
    }
}
