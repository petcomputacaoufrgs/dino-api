package br.ufrgs.inf.pet.dinoapi.service.synchronizable;

import br.ufrgs.inf.pet.dinoapi.constants.SynchronizableConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.SynchronizableException;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.*;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.*;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.*;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.utils.ListUtils;
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
            final Auth auth = authService.getCurrentAuth();

            final ENTITY entity = this.getEntity(model.getId(), auth);

            if (entity != null) {
                final DATA_MODEL data = this.internalConvertEntityToModel(entity);
                response.setSuccess(true);
                response.setData(data);
                return this.createResponse(response, HttpStatus.OK);
            }

            response.setSuccess(false);
            response.setError(SynchronizableConstants.NOT_FOUND);
            return this.createResponse(response, HttpStatus.OK);
        } catch (SynchronizableException e) {
            return this.createSynchronizableExceptionResponse(e, response);
        } catch (Exception e) {
            return this.createUnknownExceptionResponse(e, response);
        }
    }

    @Override
    public ResponseEntity<SynchronizableDataResponseModelImpl<ID, DATA_MODEL>> save(DATA_MODEL model) {
        final SynchronizableDataResponseModelImpl<ID, DATA_MODEL> response = new SynchronizableDataResponseModelImpl<>();
        try {
            if (model != null) {
                final Auth auth = authService.getCurrentAuth();
                final DATA_MODEL data;
                final ENTITY entity = this.getEntity(model.getId(), auth);

                if (entity != null) {
                    data = this.update(entity, model, auth);
                } else {
                    data = this.create(model, auth);
                }
                response.setSuccess(true);
                response.setData(data);
                this.sendUpdateMessage(data, auth);
            } else {
                response.setSuccess(false);
                response.setError(SynchronizableConstants.REQUEST_WITH_OUT_DATA);
            }

            return this.createResponse(response, HttpStatus.OK);
        } catch (SynchronizableException e) {
            return this.createSynchronizableExceptionResponse(e, response);
        } catch (Exception e) {
            return this.createUnknownExceptionResponse(e, response);
        }
    }

    @Override
    public ResponseEntity<SynchronizableDataResponseModelImpl<ID, DATA_MODEL>>
    delete(SynchronizableDeleteModel<ID> model) {
        final SynchronizableDataResponseModelImpl<ID, DATA_MODEL> response = new SynchronizableDataResponseModelImpl<>();
        try {
            final Auth auth = authService.getCurrentAuth();
            final ENTITY entity = this.getEntity(model.getId(), auth);
            if (entity != null && this.shouldDelete(entity, model)) {
                final boolean wasDeleted = this.delete(entity, model);

                if (wasDeleted) {
                    response.setSuccess(true);
                    this.sendDeleteMessage(entity.getId(), auth);
                } else {
                    response.setSuccess(false);
                    response.setError(SynchronizableConstants.YOUR_VERSION_IS_OUTDATED);
                    response.setData(this.internalConvertEntityToModel(entity));
                }
            } else {
                response.setSuccess(false);
                response.setError(SynchronizableConstants.NOT_FOUND);
            }
            return this.createResponse(response, HttpStatus.OK);
        } catch (SynchronizableException e) {
            return this.createSynchronizableExceptionResponse(e, response);
        } catch (Exception e) {
            return this.createUnknownExceptionResponse(e, response);
        }
    }

    @Override
    public ResponseEntity<SynchronizableListDataResponseModelImpl<ID, DATA_MODEL>> getAll() {
        final SynchronizableListDataResponseModelImpl<ID, DATA_MODEL> response = new SynchronizableListDataResponseModelImpl<>();

        try {
            final Auth auth = authService.getCurrentAuth();
            final List<ENTITY> entities = this.getAllEntities(auth);
            final List<DATA_MODEL> data = entities.stream().map(this::internalConvertEntityToModel).collect(Collectors.toList());

            response.setSuccess(true);
            response.setData(data);
            return this.createResponse(response, HttpStatus.OK);
        } catch (SynchronizableException e) {
            return this.createSynchronizableExceptionResponse(e, response);
        } catch (Exception e) {
            return this.createUnknownExceptionResponse(e, response);
        }
    }

    @Override
    public ResponseEntity<SynchronizableSaveAllResponseModel<ID, LOCAL_ID, DATA_MODEL>>
    saveAll(SynchronizableSaveAllModel<ID, LOCAL_ID, DATA_MODEL> model) {
        final SynchronizableSaveAllResponseModel<ID, LOCAL_ID, DATA_MODEL> response = new SynchronizableSaveAllResponseModel<>();

        try {
            final Auth auth = authService.getCurrentAuth();
            final List<DATA_MODEL> savedModels = this.internalSaveAll(model.getData(), auth);

            response.setSuccess(true);
            response.setData(savedModels);

            return this.createResponse(response, HttpStatus.OK);
        } catch (SynchronizableException e) {
            return this.createSynchronizableExceptionResponse(e, response);
        } catch (Exception e) {
            return this.createUnknownExceptionResponse(e, response);
        }
    }

    @Override
    public ResponseEntity<SynchronizableGenericDataResponseModelImpl<List<ID>>>
    deleteAll(SynchronizableDeleteAllListModel<ID> model) {
        final SynchronizableGenericDataResponseModelImpl<List<ID>> response = new SynchronizableGenericDataResponseModelImpl<>();

        try {
            final Auth auth = authService.getCurrentAuth();
            final List<ID> deletedIds = this.internalDeleteAll(model.getData(), auth);

            response.setSuccess(true);
            response.setData(deletedIds);

            return this.createResponse(response, HttpStatus.OK);
        } catch (SynchronizableException e) {
            return this.createSynchronizableExceptionResponse(e, response);
        } catch (Exception e) {
            return this.createUnknownExceptionResponse(e, response);
        }
    }

    @Override
    public ResponseEntity<SynchronizableSyncResponseModel<ID, LOCAL_ID, DATA_MODEL>> sync(SynchronizableSyncModel<ID, LOCAL_ID, DATA_MODEL> model) {
        final SynchronizableSyncResponseModel<ID, LOCAL_ID, DATA_MODEL> response = new SynchronizableSyncResponseModel<>();

        try {
            final Auth auth = authService.getCurrentAuth();

            final List<DATA_MODEL> itemsToSave = model.getSave();

            final List<SynchronizableDeleteModel<ID>> itemsToDelete = model.getDelete();

            final List<DATA_MODEL> savedModels = this.internalSaveAll(itemsToSave, auth);

            final List<ID> savedIds = savedModels.stream().map(SynchronizableDataModel::getId).collect(Collectors.toList());

            this.internalDeleteAll(itemsToDelete, auth);

            final List<ENTITY> entities = this.internalGetEntitiesByUserIdExceptIds(savedIds, auth);

            final List<DATA_MODEL> updatedModels = this.internalConvertEntitiesToModels(entities);

            updatedModels.addAll(savedModels);
            response.setData(updatedModels);
            response.setSuccess(true);
            return this.createResponse(response, HttpStatus.OK);
        } catch (SynchronizableException e) {
            return this.createSynchronizableExceptionResponse(e, response);
        } catch (Exception e) {
            return this.createUnknownExceptionResponse(e, response);
        }
    }

    protected List<DATA_MODEL> internalSaveAll(List<DATA_MODEL> models, Auth auth) throws AuthNullException, ConvertModelToEntityException {
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

        updatedData.addAll(this.updateAllItems(updateData, auth));
        updatedData.addAll(this.createEntities(newData, auth));

        this.sendUpdateMessage(updatedData, auth);

        return updatedData;
    }

    protected List<ID> internalDeleteAll(List<SynchronizableDeleteModel<ID>> models, Auth auth) throws AuthNullException {
        if (models.size() == 0) {
            return new ArrayList<>();
        }

        final List<SynchronizableDeleteModel<ID>> orderedData = models.stream()
                .filter(ListUtils.distinctByKey(SynchronizableDeleteModel::getId))
                .sorted(Comparator.comparing(SynchronizableDeleteModel::getId)).collect(Collectors.toList());

        final List<ID> orderedIds = orderedData.stream()
                .map(SynchronizableDeleteModel::getId).collect(Collectors.toList());

        final List<ENTITY> orderedEntities = this.getAllEntities(orderedIds, auth).stream()
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

        this.sendDeleteMessage(deletedIds, auth);

        return deletedIds;
    }

    protected DATA_MODEL internalConvertEntityToModel(ENTITY entity) {
        final DATA_MODEL model = this.convertEntityToModel(entity);
        model.setId(entity.getId());
        model.setLastUpdate(entity.getLastUpdate());

        return model;
    }

    public List<DATA_MODEL> internalConvertEntitiesToModels(List<ENTITY> entities) {
        return entities.stream().map(this::internalConvertEntityToModel).collect(Collectors.toList());
    }

    protected ENTITY internalConvertModelToEntity(DATA_MODEL model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        final ENTITY entity = this.convertModelToEntity(model, auth);
        entity.setId(model.getId());
        entity.setLastUpdate(model.getLastUpdate());

        return entity;
    }

    protected void internalUpdateEntity(ENTITY entity, DATA_MODEL model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        this.updateEntity(entity, model, auth);
        entity.setLastUpdate(model.getLastUpdate());
        entity.setId(model.getId());
    }

    protected List<ENTITY> internalGetEntitiesByUserIdExceptIds(List<ID> ids, Auth auth) throws AuthNullException {
        if (ids.size() > 0) {
            return this.getEntitiesByUserAuthExceptIds(auth, ids);
        }

        return this.getEntitiesByUserAuth(auth);
    }

    protected boolean canChange(ENTITY entity, SynchronizableModel<ID> model) {
        return entity.isOlderOrEqualThan(model);
    }

    protected ENTITY getEntity(ID id, Auth auth) throws AuthNullException {
        if (id != null) {
            final Optional<ENTITY> entitySearch = this.getEntityByIdAndUserAuth(id, auth);

            if (entitySearch.isPresent()) {
                return entitySearch.get();
            }
        }

        return null;
    }

    protected DATA_MODEL create(DATA_MODEL model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        ENTITY entity = this.internalConvertModelToEntity(model, auth);
        entity.setLastUpdate(model.getLastUpdate());
        entity = repository.save(entity);

        return this.internalConvertEntityToModel(entity);
    }

    protected DATA_MODEL update(ENTITY entity, DATA_MODEL model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        if (this.canChange(entity, model)) {
            this.internalUpdateEntity(entity, model, auth);
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

    protected List<ENTITY> getAllEntities(Auth auth) throws AuthNullException {
        return this.getEntitiesByUserAuth(auth);
    }

    protected List<ENTITY> getAllEntities(List<ID> ids, Auth auth) throws AuthNullException {
        return this.getEntitiesByIdsAndUserAuth(ids, auth);
    }

    protected List<DATA_MODEL> updateAllItems(List<DATA_MODEL> items, Auth auth) throws AuthNullException, ConvertModelToEntityException {
        final List<ENTITY> entitiesToSave = new ArrayList<>();

        final List<DATA_MODEL> orderedData = items.stream()
                .filter(ListUtils.distinctByKey(DATA_MODEL::getId))
                .sorted(Comparator.comparing(DATA_MODEL::getId)).collect(Collectors.toList());

        final List<ID> orderedIds = orderedData.stream()
                .map(DATA_MODEL::getId).collect(Collectors.toList());

        final List<ENTITY> orderedEntities = this.getAllEntities(orderedIds, auth).stream()
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
                        this.internalUpdateEntity(entity, model, auth);
                        entity.setLastUpdate(model.getLastUpdate());
                        entitiesToSave.add(entity);
                        modelsInSaveList.add(model);
                    }

                    count++;
                    continue;
                }
            }

            model.setId(null);

            final ENTITY newEntity = this.internalConvertModelToEntity(model, auth);
            entitiesToSave.add(newEntity);
            modelsInSaveList.add(model);
        }

        return saveEntitiesAndUpdateModels(entitiesToSave, modelsInSaveList);
    }

    protected List<DATA_MODEL> createEntities(List<DATA_MODEL> items, Auth auth) throws AuthNullException, ConvertModelToEntityException {
        final List<ENTITY> entitiesToSave = new ArrayList<>();
        final List<DATA_MODEL> modelsInSaveList = new ArrayList<>();
        for (DATA_MODEL item : items) {
            final ENTITY entity = this.internalConvertModelToEntity(item, auth);
            entity.setLastUpdate(item.getLastUpdate());
            entitiesToSave.add(entity);
            modelsInSaveList.add(item);
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

    protected void sendUpdateMessage(DATA_MODEL model, Auth auth) {
        List<DATA_MODEL> data = new ArrayList<>();
        data.add(model);
        this.sendUpdateMessage(data, auth);
    }

    protected void sendUpdateMessage(List<DATA_MODEL> data, Auth auth) {
        synchronizableMessageService.sendUpdateMessage(data, this.getUpdateWebSocketDestination(), auth);
    }

    protected void sendDeleteMessage(ID id, Auth auth) {
        final List<ID> data = new ArrayList<>();
        data.add(id);
        this.sendDeleteMessage(data, auth);
    }

    protected void sendDeleteMessage(List<ID> data, Auth auth) {
        synchronizableMessageService.sendDeleteMessage(data, this.getDeleteWebSocketDestination(), auth);
    }

    protected <T> ResponseEntity<T> createResponse(T response, HttpStatus status) {
        return new ResponseEntity<>(response, status);
    }

    protected <T extends SynchronizableGenericResponseModel> ResponseEntity<T>
    createSynchronizableExceptionResponse(SynchronizableException e, T response) {
        response.setSuccess(false);
        response.setError(e.getMessage());
        return this.createResponse(response, HttpStatus.UNAUTHORIZED);
    }

    protected <T extends SynchronizableGenericResponseModel> ResponseEntity<T>
    createUnknownExceptionResponse(Exception e, T response) {
        //TODO LOG NA API
        e.printStackTrace();
        response.setSuccess(false);
        response.setError(SynchronizableConstants.UNKNOWN_ERROR);
        return this.createResponse(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

