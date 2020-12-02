package br.ufrgs.inf.pet.dinoapi.service.synchronizable;

import br.ufrgs.inf.pet.dinoapi.constants.SynchronizableConstants;
import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.*;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.*;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.SynchronizableDeleteAllResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.SynchronizableListResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.SynchronizableResponseModel;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.utils.ListUtils;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.generic.GenericQueueMessageServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
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
        ID extends Comparable<ID>,
        DATA_MODEL extends SynchronizableDataModel<ENTITY, ID>,
        REPOSITORY extends CrudRepository<ENTITY, ID>> implements SynchronizableService<ENTITY, ID, DATA_MODEL> {

    protected final REPOSITORY repository;
    protected final AuthServiceImpl authService;
    protected final GenericQueueMessageServiceImpl genericQueueMessageService;

    public SynchronizableServiceImpl(REPOSITORY repository, AuthServiceImpl authService, GenericQueueMessageServiceImpl genericQueueMessageService) {
        this.repository = repository;
        this.authService = authService;
        this.genericQueueMessageService = genericQueueMessageService;
    }

    @Override
    public ResponseEntity<SynchronizableResponseModel<ENTITY, ID, DATA_MODEL>> get(SynchronizableGetModel<ID> model) {
        final SynchronizableResponseModel<ENTITY, ID, DATA_MODEL> response = new SynchronizableResponseModel<>();
        final ENTITY entity = this.getEntity(model.getId());

        if (entity != null) {
            final DATA_MODEL data = this.createDataModel(entity);
            response.setSuccess(true);
            response.setData(data);
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
            final ENTITY entity = this.getEntity(model.getId());
            if (entity != null) {
                data = this.update(entity, model);
            } else {
                data = this.create(model);
            }
            response.setData(data);
            this.sendUpdateMessage(data);
        } else {
            response.setSuccess(false);
            response.setError(SynchronizableConstants.REQUEST_WITH_OUT_DATA);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SynchronizableResponseModel<ENTITY, ID, DATA_MODEL>>
    delete(SynchronizableDeleteModel<ID> model) {
        final SynchronizableResponseModel<ENTITY, ID, DATA_MODEL> response = new SynchronizableResponseModel<>();
        final ENTITY entity = this.getEntity(model.getId());
        if (entity != null) {
            final boolean wasDeleted = this.delete(entity, model);

            if (wasDeleted) {
                response.setSuccess(true);
                this.sendDeleteMessage(entity.getId());
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

    @Override
    public ResponseEntity<SynchronizableListResponseModel<ENTITY, ID, DATA_MODEL>>
    saveAll(SynchronizableSaveAllModel<ENTITY, ID, DATA_MODEL> model) {
        final List<DATA_MODEL> newData = new ArrayList<>();
        final List<DATA_MODEL> updateData = new ArrayList<>();
        final List<ENTITY> newEntities = new ArrayList<>();
        final SynchronizableListResponseModel<ENTITY, ID, DATA_MODEL> response = new SynchronizableListResponseModel<>();

        model.getData().forEach(item -> {
            if (item.getId() != null) {
                updateData.add(item);
            } else {
                newData.add(item);
            }
        });

        newEntities.addAll(this.updateAllItems(updateData));
        newEntities.addAll(this.createEntities(newData));

        final List<ENTITY> savedEntities = Lists.newArrayList(repository.saveAll(newEntities));

        final List<DATA_MODEL> savedModels = savedEntities.stream().map(this::createDataModel)
                .collect(Collectors.toList());

        response.setSuccess(true);
        response.setData(savedModels);
        this.sendUpdateMessage(savedModels);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SynchronizableDeleteAllResponseModel<ID>>
    deleteAll(SynchronizableDeleteAllModel<ID> model) {
        final List<SynchronizableDeleteModel<ID>> orderedData = model.getData().stream()
                .filter(ListUtils.distinctByKey(SynchronizableDeleteModel::getId))
                .sorted(Comparator.comparing(SynchronizableDeleteModel::getId)).collect(Collectors.toList());

        final List<ID> orderedIds = orderedData.stream()
                .map(SynchronizableDeleteModel::getId).collect(Collectors.toList());

        final List<ENTITY> orderedEntities = this.getAllEntities(orderedIds).stream()
                .sorted(Comparator.comparing(SynchronizableEntity::getId)).collect(Collectors.toList());

        final List<ID> deletedIds = new ArrayList<>();

        final List<ENTITY> entitiesToDelete = new ArrayList<>();

        int count = 0;

        for (ENTITY entity : orderedEntities) {
            final ID entityId = entity.getId();

            ID id = orderedIds.get(count);

            while(id != entityId) {
                count++;

                id = orderedIds.get(count);
            }

            if (this.canChange(entity, orderedData.get(count))) {
                deletedIds.add(entity.getId());
                entitiesToDelete.add(entity);
            }

            count++;
        }

        repository.deleteAll(entitiesToDelete);

        final SynchronizableDeleteAllResponseModel<ID> response = new SynchronizableDeleteAllResponseModel<>();

        response.setSuccess(true);
        response.setData(deletedIds);
        this.sendDeleteMessage(deletedIds);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    protected boolean canChange(ENTITY entity, SynchronizableModel<ID> model) {
        return entity.isOlderOrEqualThan(model);
    }

    protected ENTITY getEntity(ID id) {
        final User user = authService.getCurrentUser();

        if (id != null) {
            final Optional<ENTITY> entitySearch = this.getEntityByIdAndUserId(id, user.getId());

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
        if (this.canChange(entity, model)) {
            this.updateEntity(entity, model);
            entity.setLastUpdate(model.getLastUpdate());
            entity = repository.save(entity);
        }
        return this.createDataModel(entity);
    }

    protected boolean delete(ENTITY entity, SynchronizableDeleteModel<ID> model) {
        if (this.canChange(entity, model)) {
            repository.delete(entity);
            return true;
        }

        return false;
    }

    protected List<ENTITY> getAllEntities() {
        final User user = authService.getCurrentUser();

        return this.getEntitiesByUserId(user.getId());
    }

    protected List<ENTITY> getAllEntities(List<ID> ids) {
        final User user = authService.getCurrentUser();

        return this.getEntitiesByIdsAndUserId(ids, user.getId());
    }

    protected List<ENTITY> updateAllItems(List<DATA_MODEL> items) {
        final List<ENTITY> entitiesToSave = new ArrayList<>();

        final List<DATA_MODEL> orderedData = items.stream()
                .filter(ListUtils.distinctByKey(DATA_MODEL::getId))
                .sorted(Comparator.comparing(DATA_MODEL::getId)).collect(Collectors.toList());

        final List<ID> orderedIds = orderedData.stream()
                .map(DATA_MODEL::getId).collect(Collectors.toList());

        final List<ENTITY> orderedEntities = this.getAllEntities(orderedIds).stream()
                .sorted(Comparator.comparing(SynchronizableEntity::getId)).collect(Collectors.toList());

        int count = 0;

        for (DATA_MODEL model : orderedData) {
            final ENTITY entity = orderedEntities.get(count);
            final ID entityId = entity.getId();

            if (model.getId() != entityId) {
                entitiesToSave.add(this.createEntity(model));
            } else {
                if (this.canChange(entity, model)) {
                    this.updateEntity(entity, model);
                    entity.setLastUpdate(model.getLastUpdate());
                    entitiesToSave.add(entity);
                }

                count++;
            }
        }

        return entitiesToSave;
    }

    protected List<ENTITY> createEntities(List<DATA_MODEL> items) {
        return items.stream().map(item -> {
            ENTITY entity = this.createEntity(item);
            entity.setLastUpdate(item.getLastUpdate());
            return entity;
        }).collect(Collectors.toList());
    }

    protected void sendUpdateMessage(Object object) {
        try {
            List<Object> data = new ArrayList<>();
            data.add(object);
            genericQueueMessageService.sendObjectMessage(data, this.getUpdateWebsocketDestination());
        } catch (JsonProcessingException e) {
            e.printStackTrace(); //TODO Log error
        }
    }

    protected void sendUpdateMessage(List<Object> object) {
        try {
            genericQueueMessageService.sendObjectMessage(object, this.getUpdateWebsocketDestination());
        } catch (JsonProcessingException e) {
            e.printStackTrace(); //TODO Log error
        }
    }

    protected void sendDeleteMessage(ID id) {
        try {
            final List<Object> data = new ArrayList<>();
            data.add(id);
            genericQueueMessageService.sendObjectMessage(data, this.getDeleteWebsocketDestination());
        } catch (JsonProcessingException e) {
            e.printStackTrace(); //TODO Log error
        }
    }

    protected void sendDeleteMessage(List<ID> ids) {
        try {
            genericQueueMessageService.sendObjectMessage(ids, this.getDeleteWebsocketDestination());
        } catch (JsonProcessingException e) {
            e.printStackTrace(); //TODO Log error
        }
    }
}

