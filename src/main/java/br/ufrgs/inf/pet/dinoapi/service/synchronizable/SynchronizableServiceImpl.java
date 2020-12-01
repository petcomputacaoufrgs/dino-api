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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Base service with get, getAll, save/update and delete for synchronizable entity
 *
 * @param <ENTITY> Synchronizable entity
 * @param <ID> Id type of synchronizable entity
 * @param <DATA_MODEL> Data model of synchronizable entity
 * @param <REPOSITORY> Repository of synchronizable entity
 */
public abstract class SynchronizableServiceImpl<
        ENTITY extends SynchronizableEntity<ID>,
        ID,
        DATA_MODEL extends SynchronizableDataModel<ENTITY, ID>,
        REPOSITORY extends CrudRepository<ENTITY, ID>> implements SynchronizableService<ENTITY, ID, DATA_MODEL> {

    protected final REPOSITORY repository;
    protected final AuthServiceImpl authService;

    public SynchronizableServiceImpl(REPOSITORY repository, AuthServiceImpl authService) {
        this.repository = repository;
        this.authService = authService;
    }

    @Override
    public ResponseEntity<SynchronizableResponseModel<ENTITY, ID, DATA_MODEL>> get(SynchronizableGetModel<ID> model) {
        final SynchronizableResponseModel<ENTITY, ID, DATA_MODEL> response = new SynchronizableResponseModel<>();
        final ENTITY entity = this.getEntity(model);

        if (entity != null) {
            response.setSuccess(true);
            response.setData(this.createDataModel(entity));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.setSuccess(false);
        response.setError(SynchronizableConstants.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SynchronizableResponseModel<ENTITY, ID, DATA_MODEL>> save(DATA_MODEL model) {
        final SynchronizableResponseModel<ENTITY, ID, DATA_MODEL> response = new SynchronizableResponseModel<>();

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

    @Override
    public ResponseEntity<SynchronizableResponseModel<ENTITY, ID, DATA_MODEL>> delete(SynchronizableDeleteModel<ID> model) {
        final SynchronizableResponseModel<ENTITY, ID, DATA_MODEL> response = new SynchronizableResponseModel<>();
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

    @Override
    public ResponseEntity<SynchronizableListResponseModel<ENTITY, ID, DATA_MODEL>> getAll() {
        final List<ENTITY> entities = this.getAllEntities();
        final List<DATA_MODEL> data = entities.stream().map(this::createDataModel).collect(Collectors.toList());

        final SynchronizableListResponseModel<ENTITY, ID, DATA_MODEL> response = new SynchronizableListResponseModel<>();

        response.setSuccess(true);
        response.setData(data);

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

    protected List<ENTITY> getAllEntities() {
        final User user = authService.getCurrentUser();

        return this.getEntitiesByUserId(user.getId());
    }
}
