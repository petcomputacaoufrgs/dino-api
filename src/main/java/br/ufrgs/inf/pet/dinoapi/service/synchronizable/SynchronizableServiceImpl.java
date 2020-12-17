package br.ufrgs.inf.pet.dinoapi.service.synchronizable;

import br.ufrgs.inf.pet.dinoapi.constants.SynchronizableConstants;
import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.*;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.*;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.*;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.utils.ListUtils;
import br.ufrgs.inf.pet.dinoapi.websocket.model.synchronizable.SynchronizableWSDeleteModel;
import br.ufrgs.inf.pet.dinoapi.websocket.model.synchronizable.SynchronizableWSUpdateModel;
import br.ufrgs.inf.pet.dinoapi.websocket.service.SynchronizableMessageService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.io.Serializable;
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
        ID extends Comparable<ID> & Serializable,
        LOCAL_ID,
        DATA_MODEL extends SynchronizableDataLocalIdModel<ID, LOCAL_ID>,
        REPOSITORY extends CrudRepository<ENTITY, ID>> implements SynchronizableService<ENTITY, ID, LOCAL_ID, DATA_MODEL> {

    protected final REPOSITORY repository;
    protected final AuthServiceImpl authService;
    protected final SynchronizableMessageService<ID, LOCAL_ID, DATA_MODEL> synchronizableMessageService;

    public SynchronizableServiceImpl(REPOSITORY repository, AuthServiceImpl authService,
                                     SynchronizableMessageService<ID, LOCAL_ID, DATA_MODEL> synchronizableMessageService) {
        this.repository = repository;
        this.authService = authService;
        this.synchronizableMessageService = synchronizableMessageService;
    }

    @Override
    public boolean shouldDelete(ENTITY entity, SynchronizableDeleteModel<ID> model) {
        return true;
    }

    @Override
    public ResponseEntity<SynchronizableDataResponseModelImpl<ID, DATA_MODEL>> get(SynchronizableGetModel<ID> model) {
        final SynchronizableDataResponseModelImpl<ID, DATA_MODEL> response = new SynchronizableDataResponseModelImpl<>();
        try {
            final ENTITY entity = this.getEntity(model.getId());

            if (entity != null) {
                final DATA_MODEL data = this.internalConvertEntityToModel(entity);
                response.setSuccess(true);
                response.setData(data);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            response.setSuccess(false);
            response.setError(SynchronizableConstants.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            //TODO Log API Error
            response.setSuccess(false);
            response.setError(SynchronizableConstants.UNKNOWN_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<SynchronizableDataResponseModelImpl<ID, DATA_MODEL>> save(DATA_MODEL model) {
        final SynchronizableDataResponseModelImpl<ID, DATA_MODEL> response = new SynchronizableDataResponseModelImpl<>();

        try {
            final User user = authService.getCurrentUser();

            if (model != null) {
                final DATA_MODEL data;
                final ENTITY entity = this.getEntity(model.getId());
                try {
                    if (entity != null) {
                        data = this.update(entity, model);
                    } else {
                        data = this.create(model, user);
                    }
                    response.setSuccess(true);
                    response.setData(data);
                    this.sendUpdateMessage(data);
                } catch (ConvertModelToEntityException e) {
                    response.setSuccess(false);
                    response.setError(e.getMessage());
                }
            } else {
                response.setSuccess(false);
                response.setError(SynchronizableConstants.REQUEST_WITH_OUT_DATA);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            //TODO Log API Error
            response.setSuccess(false);
            response.setError(SynchronizableConstants.UNKNOWN_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<SynchronizableDataResponseModelImpl<ID, DATA_MODEL>>
    delete(SynchronizableDeleteModel<ID> model) {
        final SynchronizableDataResponseModelImpl<ID, DATA_MODEL> response = new SynchronizableDataResponseModelImpl<>();
        try {
            final ENTITY entity = this.getEntity(model.getId());
            if (entity != null && this.shouldDelete(entity, model)) {
                final boolean wasDeleted = this.delete(entity, model);

                if (wasDeleted) {
                    response.setSuccess(true);
                    this.sendDeleteMessage(entity.getId());
                } else {
                    response.setSuccess(false);
                    response.setError(SynchronizableConstants.YOUR_VERSION_IS_OUTDATED);
                    response.setData(this.internalConvertEntityToModel(entity));
                }
            } else {
                response.setSuccess(false);
                response.setError(SynchronizableConstants.NOT_FOUND);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            //TODO Log API Error
            response.setSuccess(false);
            response.setError(SynchronizableConstants.UNKNOWN_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<SynchronizableListDataResponseModelImpl<ID, DATA_MODEL>> getAll() {
        final SynchronizableListDataResponseModelImpl<ID, DATA_MODEL> response = new SynchronizableListDataResponseModelImpl<>();

        try {
            final List<ENTITY> entities = this.getAllEntities();
            final List<DATA_MODEL> data = entities.stream().map(this::internalConvertEntityToModel).collect(Collectors.toList());


            response.setSuccess(true);
            response.setData(data);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            //TODO Log API Error
            response.setSuccess(false);
            response.setError(SynchronizableConstants.UNKNOWN_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<SynchronizableSaveAllResponseModel<ID, LOCAL_ID, DATA_MODEL>>
    saveAll(SynchronizableSaveAllModel<ID, LOCAL_ID, DATA_MODEL> model) {
        final SynchronizableSaveAllResponseModel<ID, LOCAL_ID, DATA_MODEL> response = new SynchronizableSaveAllResponseModel<>();

        try {
            final User user = authService.getCurrentUser();

            final List<DATA_MODEL> savedModels = this.internalSaveAll(model.getData(), user);

            response.setSuccess(true);
            response.setData(savedModels);
            this.sendUpdateMessage(savedModels);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            //TODO Log API Error
            response.setSuccess(false);
            response.setError(SynchronizableConstants.UNKNOWN_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<SynchronizableGenericDataResponseModelImpl<List<ID>>>
    deleteAll(SynchronizableDeleteAllListModel<ID> model) {
        final SynchronizableGenericDataResponseModelImpl<List<ID>> response = new SynchronizableGenericDataResponseModelImpl<>();

        try {
            final List<ID> deletedIds = this.internalDeleteAll(model.getData());

            response.setSuccess(true);
            response.setData(deletedIds);
            this.sendDeleteMessage(deletedIds);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            //TODO Log API Error
            response.setSuccess(false);
            response.setError(SynchronizableConstants.UNKNOWN_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<SynchronizableSyncResponseModel<ID, LOCAL_ID, DATA_MODEL>> sync(SynchronizableSyncModel<ID, LOCAL_ID, DATA_MODEL> model) {
        final SynchronizableSyncResponseModel<ID, LOCAL_ID, DATA_MODEL> response = new SynchronizableSyncResponseModel<>();

        try {
            final User user = authService.getCurrentUser();

            final List<DATA_MODEL> itemsToSave = model.getSave();

            final List<SynchronizableDeleteModel<ID>> itemsToDelete = model.getDelete();

            final List<DATA_MODEL> savedModels = this.internalSaveAll(itemsToSave, user);

            final List<ID> savedIds = savedModels.stream().map(SynchronizableDataModel::getId).collect(Collectors.toList());

            this.internalDeleteAll(itemsToDelete);

            final List<ENTITY> entities = this.internalGetEntitiesByUserIdExceptIds(user, savedIds);

            final List<DATA_MODEL> updatedModels = this.internalConvertEntitiesToModels(entities);

            response.setData(updatedModels);
            response.setSuccess(true);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            //TODO Log API Error
            response.setSuccess(false);
            response.setError(SynchronizableConstants.UNKNOWN_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    protected List<DATA_MODEL> internalSaveAll(List<DATA_MODEL> models, User user) {
        if (models.size() == 0) {
            return new ArrayList<>();
        }

        final List<DATA_MODEL> newData = new ArrayList<>();
        final List<DATA_MODEL> updateData = new ArrayList<>();
        final List<DATA_MODEL> updatedData = new ArrayList<>();

        models.forEach(model -> {
            if (model.getId() != null) {
                updateData.add(model);
            } else {
                newData.add(model);
            }
        });

        updatedData.addAll(this.updateAllItems(updateData, user));
        updatedData.addAll(this.createEntities(newData, user));

        return updatedData;
    }

    protected List<ID> internalDeleteAll(List<SynchronizableDeleteModel<ID>> models) {
        if (models.size() == 0) {
            return new ArrayList<>();
        }

        final List<SynchronizableDeleteModel<ID>> orderedData = models.stream()
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

            if (this.canChange(entity, orderedData.get(count)) && this.shouldDelete(entity, orderedData.get(count))) {
                deletedIds.add(entity.getId());
                entitiesToDelete.add(entity);
            }

            count++;
        }

        repository.deleteAll(entitiesToDelete);

        return deletedIds;
    }

    protected DATA_MODEL internalConvertEntityToModel(ENTITY entity) {
        final DATA_MODEL model = this.convertEntityToModel(entity);
        model.setId(entity.getId());
        model.setLastUpdate(entity.getLastUpdate());

        return model;
    }

    protected List<DATA_MODEL> internalConvertEntitiesToModels(List<ENTITY> entities) {
        return entities.stream().map(this::internalConvertEntityToModel).collect(Collectors.toList());
    }

    protected ENTITY internalConvertModelToEntity(DATA_MODEL model, User user) throws ConvertModelToEntityException {
        final ENTITY entity = this.convertModelToEntity(model, user);
        entity.setId(model.getId());
        entity.setLastUpdate(model.getLastUpdate());

        return entity;
    }

    protected void internalUpdateEntity(ENTITY entity, DATA_MODEL model) throws ConvertModelToEntityException {
        this.updateEntity(entity, model);
        entity.setLastUpdate(model.getLastUpdate());
        entity.setId(model.getId());
    }

    protected List<ENTITY> internalGetEntitiesByUserIdExceptIds(User user, List<ID> ids) {
        if (ids.size() > 0) {
            return this.getEntitiesByUserIdExceptIds(user, ids);
        }

        return this.getEntitiesByUserId(user);
    }

    protected boolean canChange(ENTITY entity, SynchronizableModel<ID> model) {
        return entity.isOlderOrEqualThan(model);
    }

    protected ENTITY getEntity(ID id) {
        final User user = authService.getCurrentUser();

        if (id != null) {
            final Optional<ENTITY> entitySearch = this.getEntityByIdAndUser(id, user);

            if (entitySearch.isPresent()) {
                return entitySearch.get();
            }
        }

        return null;
    }

    protected DATA_MODEL create(DATA_MODEL model, User user) throws ConvertModelToEntityException {
        ENTITY entity = this.internalConvertModelToEntity(model, user);
        entity.setLastUpdate(model.getLastUpdate());
        entity = repository.save(entity);

        return this.internalConvertEntityToModel(entity);
    }

    protected DATA_MODEL update(ENTITY entity, DATA_MODEL model) throws ConvertModelToEntityException {
        if (this.canChange(entity, model)) {
            this.internalUpdateEntity(entity, model);
            entity.setLastUpdate(model.getLastUpdate());
            entity = repository.save(entity);
        }
        return this.internalConvertEntityToModel(entity);
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

        return this.getEntitiesByUserId(user);
    }

    protected List<ENTITY> getAllEntities(List<ID> ids) {
        final User user = authService.getCurrentUser();

        return this.getEntitiesByIdsAndUserId(ids, user);
    }

    protected List<DATA_MODEL> updateAllItems(List<DATA_MODEL> items, User user) {
        final List<ENTITY> entitiesToSave = new ArrayList<>();

        final List<DATA_MODEL> orderedData = items.stream()
                .filter(ListUtils.distinctByKey(DATA_MODEL::getId))
                .sorted(Comparator.comparing(DATA_MODEL::getId)).collect(Collectors.toList());

        final List<ID> orderedIds = orderedData.stream()
                .map(DATA_MODEL::getId).collect(Collectors.toList());

        final List<ENTITY> orderedEntities = this.getAllEntities(orderedIds).stream()
                .sorted(Comparator.comparing(SynchronizableEntity::getId)).collect(Collectors.toList());

        int count = 0;

        final int orderedEntitiesSize = orderedEntities.size();

        final List<DATA_MODEL> modelsInSaveList = new ArrayList<>();

        for (DATA_MODEL model : orderedData) {
            if (count < orderedEntitiesSize) {
                final ENTITY entity = orderedEntities.get(count);
                final ID entityId = entity.getId();

                if (model.getId() == entityId) {
                    if (this.canChange(entity, model)) {
                        try {
                            this.internalUpdateEntity(entity, model);
                            entity.setLastUpdate(model.getLastUpdate());
                            entitiesToSave.add(entity);
                            modelsInSaveList.add(model);
                        } catch (ConvertModelToEntityException e) {
                            //TODO Log API Error
                        }
                    }

                    count++;
                    continue;
                }
            }

            model.setId(null);
            try {
                final ENTITY newEntity = this.internalConvertModelToEntity(model, user);
                entitiesToSave.add(newEntity);
                modelsInSaveList.add(model);
            } catch (ConvertModelToEntityException e) {
                //TODO Log API Error
            }
        }

        return saveEntitiesAndUpdateModels(entitiesToSave, modelsInSaveList);
    }

    protected List<DATA_MODEL> createEntities(List<DATA_MODEL> items, User user) {
        final List<ENTITY> entitiesToSave = new ArrayList<>();
        final List<DATA_MODEL> modelsInSaveList = new ArrayList<>();
        for (DATA_MODEL item : items) {
            try {
                final ENTITY entity = this.internalConvertModelToEntity(item, user);
                entity.setLastUpdate(item.getLastUpdate());
                entitiesToSave.add(entity);
                modelsInSaveList.add(item);
            } catch (ConvertModelToEntityException e) {
                //TODO: Log API Error
            }
        }

        return saveEntitiesAndUpdateModels(entitiesToSave, modelsInSaveList);
    }

    protected List<DATA_MODEL> saveEntitiesAndUpdateModels(List<ENTITY> entitiesToSave, List<DATA_MODEL> modelsInSaveList) {
        final List<DATA_MODEL> updatedModels = new ArrayList<>();

        int count = 0;

        for (ENTITY entity: repository.saveAll(entitiesToSave)) {
            final DATA_MODEL originalModel = modelsInSaveList.get(count);
            final DATA_MODEL model = this.internalConvertEntityToModel(entity);
            model.setLocalId(originalModel.getLocalId());

            updatedModels.add(model);
            count++;
        }

        return updatedModels;
    }

    protected void sendUpdateMessage(DATA_MODEL model) {
        List<DATA_MODEL> data = new ArrayList<>();
        data.add(model);
        this.sendUpdateMessage(data);
    }

    protected void sendUpdateMessage(DATA_MODEL model, User user) {
        List<DATA_MODEL> data = new ArrayList<>();
        data.add(model);
        this.sendUpdateMessage(data, user);
    }

    protected void sendUpdateMessage(List<DATA_MODEL> data) {
        synchronizableMessageService.sendUpdateMessage(data, this.getUpdateWebSocketDestination());
    }

    protected void sendUpdateMessage(List<DATA_MODEL> data, User user) {
        synchronizableMessageService.sendUpdateMessage(data, this.getUpdateWebSocketDestination(), user);
    }

    protected void sendDeleteMessage(ID id) {
        final List<ID> data = new ArrayList<>();
        data.add(id);
        this.sendDeleteMessage(data);
    }

    protected void sendDeleteMessage(List<ID> data) {
        synchronizableMessageService.sendDeleteMessage(data, this.getDeleteWebSocketDestination());
    }
}

