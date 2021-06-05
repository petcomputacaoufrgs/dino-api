package br.ufrgs.inf.pet.dinoapi.service.synchronizable;

import br.ufrgs.inf.pet.dinoapi.constants.SynchronizableConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.enumerable.PermissionEnum;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.SynchronizableException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.UserWithoutPermissionException;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.*;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.*;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import br.ufrgs.inf.pet.dinoapi.utils.ListUtils;
import br.ufrgs.inf.pet.dinoapi.utils.Tuple2;
import br.ufrgs.inf.pet.dinoapi.websocket.service.SynchronizableMessageService;
import com.google.api.client.util.Lists;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Base service with get, getAll, save/update and delete for synchronizable entity
 *
 * @param <ENTITY>     Synchronizable entity
 * @param <ID>         Id type of synchronizable entity
 * @param <DATA_MODEL> Data model of synchronizable entity
 * @param <REPOSITORY> Repository of synchronizable entity
 */
public abstract class SynchronizableServiceImpl<
        ENTITY extends SynchronizableEntity<ID>,
        ID extends Comparable<ID> & Serializable,
        DATA_MODEL extends SynchronizableDataLocalIdModel<ID>,
        REPOSITORY extends CrudRepository<ENTITY, ID>> extends LogUtilsBase implements SynchronizableService<ENTITY, ID, DATA_MODEL> {

    protected final REPOSITORY repository;
    protected final AuthServiceImpl authService;
    protected final SynchronizableMessageService<ID, DATA_MODEL> synchronizableMessageService;
    protected final ClockServiceImpl clock;

    public SynchronizableServiceImpl(REPOSITORY repository, AuthServiceImpl authService, ClockServiceImpl clock,
                                     SynchronizableMessageService<ID, DATA_MODEL> synchronizableMessageService,
                                     LogAPIErrorServiceImpl logAPIErrorService) {
        super(logAPIErrorService);
        this.repository = repository;
        this.authService = authService;
        this.synchronizableMessageService = synchronizableMessageService;
        this.clock = clock;
    }

    /**
     * Override it to do something after a new entity is created
     *
     * @param entity created entity
     * @param auth authentication of user that fire the event
     */
    protected void afterDataCreated(ENTITY entity, Auth auth) {
    }

    /**
     * Override it to do something after a entity is updated
     *
     * @param entity updated entity
     * @param auth authentication of user that fire the event
     */
    protected void afterDataUpdated(ENTITY entity, Auth auth) {
    }

    /**
     * Override it to do something before a entity be deleted
     *
     * @param entity deleted entity
     * @param auth authentication of user that fire the event
     */
    protected void beforeDataDeleted(ENTITY entity, Auth auth) {
    }

    /**
     * Override it to do something after a entity be deleted
     *
     * @param entity deleted entity
     * @param auth authentication of user that fire the event
     */
    protected void afterDataDeleted(ENTITY entity, Auth auth) {
    }

    /**
     * Override it to define if entity should be deleted
     *
     * @param entity base entity
     */
    protected boolean shouldDelete(ENTITY entity, SynchronizableDeleteModel<ID> model) {
        return true;
    }

    /**
     * Override it to treat a model without id before save process
     * @param model entity data model
     */
    protected void treatBeforeSave(DATA_MODEL model) { }

    /**
     * Override it to treat models without id before save all process
     * @param models list of entities data models
     */
    protected List<DATA_MODEL> treatBeforeSaveAll(List<DATA_MODEL> models) {
        return models;
    }

    /**
     * Define a list of authorities, to edit entity user mus have one of them.
     * Default is empty list without restrictions
     * @return List of authorities
     */
    protected List<PermissionEnum> getNecessaryPermissionsToEdit() {
        return new ArrayList<>();
    }

    @Override
    public ResponseEntity<SynchronizableDataResponseModelImpl<ID, DATA_MODEL>> get(SynchronizableGetModel<ID> model) {
        final SynchronizableDataResponseModelImpl<ID, DATA_MODEL> response = new SynchronizableDataResponseModelImpl<>();
        try {
            final Auth auth = authService.getCurrentAuth();

            final ENTITY entity = this.getEntityToRead(model.getId(), auth);

            if (entity != null) {
                final DATA_MODEL data = this.completeConvertEntityToModel(entity);
                response.setSuccess(true);
                response.setData(data);
                return this.createResponse(response);
            }

            response.setSuccess(false);
            response.setError(SynchronizableConstants.NOT_FOUND);
            return this.createResponse(response);
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
                this.validateEditPermission(auth);
                final DATA_MODEL data = this.internalSave(model, auth);
                response.setSuccess(true);
                response.setData(data);
            } else {
                response.setSuccess(false);
                response.setError(SynchronizableConstants.REQUEST_WITH_OUT_DATA);
            }

            return this.createResponse(response);
        } catch (UserWithoutPermissionException e)  {
            return this.createUserWithoutPermissionExceptionResponse(e, response);
        } catch (SynchronizableException e) {
            return this.createSynchronizableExceptionResponse(e, response);
        } catch (Exception e) {
            return this.createUnknownExceptionResponse(e, response);
        }
    }

    @Override
    public ResponseEntity<SynchronizableDataResponseModelImpl<ID, DATA_MODEL>>
    delete(SynchronizableDeleteModel<ID> model) {
        try {
            final Auth auth = authService.getCurrentAuth();
            this.validateEditPermission(auth);
            return this.internalDelete(model, auth);
        } catch (UserWithoutPermissionException e)  {
            return this.createUserWithoutPermissionExceptionResponse(e, new SynchronizableDataResponseModelImpl<>());
        } catch (SynchronizableException e) {
            return this.createSynchronizableExceptionResponse(e, new SynchronizableDataResponseModelImpl<>());
        } catch (Exception e) {
            return this.createUnknownExceptionResponse(e, new SynchronizableDataResponseModelImpl<>());
        }
    }

    @Override
    public ResponseEntity<SynchronizableListDataResponseModelImpl<ID, DATA_MODEL>> getAll() {
        final SynchronizableListDataResponseModelImpl<ID, DATA_MODEL> response = new SynchronizableListDataResponseModelImpl<>();

        try {
            final Auth auth = authService.getCurrentAuth();
            final List<ENTITY> entities = this.getAllEntities(auth);
            final List<DATA_MODEL> data = entities.stream().map(this::completeConvertEntityToModel).collect(Collectors.toList());
            response.setSuccess(true);
            response.setData(data);
            return this.createResponse(response);
        } catch (SynchronizableException e) {
            return this.createSynchronizableExceptionResponse(e, response);
        } catch (Exception e) {
            return this.createUnknownExceptionResponse(e, response);
        }
    }

    @Override
    public ResponseEntity<SynchronizableSaveAllResponseModel<ID, DATA_MODEL>>
    saveAll(SynchronizableSaveAllModel<ID, DATA_MODEL> model) {
        final SynchronizableSaveAllResponseModel<ID, DATA_MODEL> response = new SynchronizableSaveAllResponseModel<>();
        try {
            final Auth auth = authService.getCurrentAuth();
            this.validateEditPermission(auth);
            final List<DATA_MODEL> savedModels = this.internalSaveAll(model.getData(), auth);
            response.setSuccess(true);
            response.setData(savedModels);
            return this.createResponse(response);
        } catch (UserWithoutPermissionException e)  {
            return this.createUserWithoutPermissionExceptionResponse(e, response);
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
            this.validateEditPermission(auth);
            final List<ID> deletedIds = this.internalDeleteAll(model.getData(), auth);
            response.setSuccess(true);
            response.setData(deletedIds);
            return this.createResponse(response);
        } catch (UserWithoutPermissionException e)  {
            return this.createUserWithoutPermissionExceptionResponse(e, response);
        } catch (SynchronizableException e) {
            return this.createSynchronizableExceptionResponse(e, response);
        } catch (Exception e) {
            return this.createUnknownExceptionResponse(e, response);
        }
    }

    @Override
    public ResponseEntity<SynchronizableSyncResponseModel<ID, DATA_MODEL>> sync(SynchronizableSaveSyncModel<ID, DATA_MODEL> model) {
        final SynchronizableSyncResponseModel<ID, DATA_MODEL> response = new SynchronizableSyncResponseModel<>();

        try {
            final Auth auth = authService.getCurrentAuth();
            final boolean hasEditPermission = this.hasEditPermission(auth);
            final List<DATA_MODEL> data = hasEditPermission ? this.syncWithEdit(auth, model) : this.syncWithoutEdit(auth);
            response.setData(data);
            response.setSuccess(true);
            return this.createResponse(response);
        } catch (SynchronizableException e) {
            return this.createSynchronizableExceptionResponse(e, response);
        } catch (Exception e) {
            return this.createUnknownExceptionResponse(e, response);
        }
    }

    public void saveByUser(DATA_MODEL dataModel, User user) throws AuthNullException, ConvertModelToEntityException {
        final Auth fakeAuth = this.getFakeAuth(user);

        this.internalSave(dataModel, fakeAuth);
    }

    public void deleteByUser(SynchronizableDeleteModel<ID> model, User user) throws AuthNullException {
        final Auth fakeAuth = this.getFakeAuth(user);

        this.internalDelete(model, fakeAuth);
    }

    protected DATA_MODEL internalSave(DATA_MODEL model, Auth auth) throws AuthNullException, ConvertModelToEntityException {
        this.treatBeforeSave(model);
        final ENTITY entity = this.getEntityToEdit(model.getId(), auth);
        final DATA_MODEL result;

        if (entity != null) {
            result = this.update(entity, model, auth);
        } else {
            result = this.create(model, auth);
        }

        this.sendUpdateMessage(result, auth);

        return result;
    }

    protected List<DATA_MODEL> internalSaveAll(List<DATA_MODEL> models, Auth auth) throws AuthNullException, ConvertModelToEntityException {
        if (models.size() == 0) {
            return new ArrayList<>();
        }

        final List<DATA_MODEL> newData = new ArrayList<>();
        final List<DATA_MODEL> updateData = new ArrayList<>();
        final List<DATA_MODEL> saveData = new ArrayList<>();

        models = this.treatBeforeSaveAll(models);

        models.forEach(model -> {
            if (model.getId() != null) {
                updateData.add(model);
            } else {
                newData.add(model);
            }
        });

        final Tuple2<List<DATA_MODEL>, Tuple2<Tuple2<List<DATA_MODEL>, List<ENTITY>>, List<DATA_MODEL>>>
                updateResult = this.updateAllItems(updateData, auth);
        final List<DATA_MODEL> createdData = this.createEntities(newData, auth);

        saveData.addAll(updateResult.getFirst());
        saveData.addAll(updateResult.getSecond().getFirst().getFirst());
        saveData.addAll(createdData);
        this.sendUpdateMessage(saveData, auth);
        saveData.addAll(updateResult.getSecond().getSecond());
        return saveData;
    }

    protected ResponseEntity<SynchronizableDataResponseModelImpl<ID, DATA_MODEL>>
    internalDelete(SynchronizableDeleteModel<ID> model, Auth auth) throws AuthNullException {
        final SynchronizableDataResponseModelImpl<ID, DATA_MODEL> response = new SynchronizableDataResponseModelImpl<>();

        final ENTITY entity = this.getEntityToEdit(model.getId(), auth);

        if (entity != null && this.shouldDelete(entity, model)) {
            final boolean wasDeleted = this.delete(entity, model, auth);

            if (wasDeleted) {
                response.setSuccess(true);
                this.sendDeleteMessage(entity.getId(), auth);
            } else {
                response.setSuccess(false);
                response.setError(SynchronizableConstants.YOUR_VERSION_IS_OUTDATED);
                response.setData(this.completeConvertEntityToModel(entity));
            }
        } else {
            response.setSuccess(false);
            response.setError(SynchronizableConstants.NOT_FOUND);
        }
        return this.createResponse(response);
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

        final List<ENTITY> orderedEntities = this.getAllEntitiesThatUserCanEdit(orderedIds, auth).stream()
                .sorted(Comparator.comparing(SynchronizableEntity::getId)).collect(Collectors.toList());

        final List<ID> deletedIds = new ArrayList<>();

        final List<ENTITY> entitiesToDelete = new ArrayList<>();

        int count = 0;

        for (ENTITY entity : orderedEntities) {
            final ID entityId = entity.getId();

            ID id = orderedIds.get(count);

            while (!Objects.equals(entityId, id)) {
                count++;

                id = orderedIds.get(count);
            }

            if (this.canChange(entity, orderedData.get(count)) && this.shouldDelete(entity, orderedData.get(count))) {
                deletedIds.add(entity.getId());
                entitiesToDelete.add(entity);
            }

            count++;
        }

        for (ENTITY entityToDelete : entitiesToDelete) {
            this.beforeDataDeleted(entityToDelete, auth);
        }

        repository.deleteAll(entitiesToDelete);

        for (ENTITY entityToDelete : entitiesToDelete) {
            this.afterDataDeleted(entityToDelete, auth);
        }

        this.sendDeleteMessage(deletedIds, auth);

        return deletedIds;
    }

    protected DATA_MODEL completeConvertEntityToModel(ENTITY entity) {
        final DATA_MODEL model = this.convertEntityToModel(entity);
        model.setId(entity.getId());
        model.setLastUpdate(clock.toUTCZonedDateTime(entity.getLastUpdate()));

        return model;
    }

    protected List<DATA_MODEL> completeConvertEntitiesToModels(List<ENTITY> entities) {
        return entities.stream().map(this::completeConvertEntityToModel).collect(Collectors.toList());
    }

    protected ENTITY completeConvertModelToEntity(DATA_MODEL model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        final ENTITY entity = this.convertModelToEntity(model, auth);
        entity.setId(model.getId());
        entity.setLastUpdate(model.getLastUpdate().toLocalDateTime());

        return entity;
    }

    protected void internalUpdateEntity(ENTITY entity, DATA_MODEL model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        this.updateEntity(entity, model, auth);
        entity.setLastUpdate(model.getLastUpdate().toLocalDateTime());
        entity.setId(model.getId());
    }

    protected List<ENTITY> internalGetEntitiesByUserIdExcludingIds(List<ID> ids, Auth auth) throws AuthNullException {
        if (ids.size() > 0) {
            return this.findEntitiesThatUserCanReadExcludingIds(auth, ids);
        }

        return this.getAllEntities(auth);
    }

    private boolean canChange(ENTITY entity, SynchronizableModel<ID> model) {
        return entity.isOlderOrEqualThan(model);
    }

    protected ENTITY getEntityToRead(ID id, Auth auth) throws AuthNullException {
        if (id != null) {
            final Optional<ENTITY> entitySearch = this.findEntityByIdThatUserCanRead(id, auth);

            return entitySearch.orElse(null);
        }

        return null;
    }

    protected ENTITY getEntityToEdit(ID id, Auth auth) throws AuthNullException {
        if (id != null) {
            final Optional<ENTITY> entitySearch = this.findEntityByIdThatUserCanEdit(id, auth);

            return entitySearch.orElse(null);
        }

        return null;
    }

    protected List<ENTITY> getAllEntities(Auth auth) throws AuthNullException {
        return this.findEntitiesThatUserCanRead(auth);
    }

    protected List<ENTITY> getAllEntitiesThatUserCanEdit(List<ID> ids, Auth auth) throws AuthNullException {
        return this.findEntitiesByIdThatUserCanEdit(ids, auth);
    }

    protected Tuple2<List<DATA_MODEL>, Tuple2<Tuple2<List<DATA_MODEL>, List<ENTITY>>, List<DATA_MODEL>>>
    updateAllItems(List<DATA_MODEL> items, Auth auth) throws AuthNullException, ConvertModelToEntityException {
        final List<ENTITY> entitiesToCreate = new ArrayList<>();
        final List<ENTITY> entitiesToUpdate = new ArrayList<>();
        final List<ENTITY> notUpdatedEntities = new ArrayList<>();
        final List<DATA_MODEL> notUpdatedModels = new ArrayList<>();

        final List<DATA_MODEL> orderedData = items.stream()
                .filter(ListUtils.distinctByKey(DATA_MODEL::getId))
                .sorted(Comparator.comparing(DATA_MODEL::getId)).collect(Collectors.toList());

        final List<ID> orderedIds = orderedData.stream()
                .map(DATA_MODEL::getId).collect(Collectors.toList());

        final List<ENTITY> orderedEntities = this.getAllEntitiesThatUserCanEdit(orderedIds, auth).stream()
                .sorted(Comparator.comparing(SynchronizableEntity::getId)).collect(Collectors.toList());

        int count = 0;

        final int orderedEntitiesSize = orderedEntities.size();

        final List<DATA_MODEL> modelsInCreateList = new ArrayList<>();
        final List<DATA_MODEL> modelsInUpdateList = new ArrayList<>();

        for (DATA_MODEL model : orderedData) {
            this.treatBeforeSaveAll(orderedData);
            if (count < orderedEntitiesSize) {
                final ENTITY entity = orderedEntities.get(count);
                final ID entityId = entity.getId();

                if (model.getId().equals(entityId)) {
                    if (this.canChange(entity, model)) {
                        this.internalUpdateEntity(entity, model, auth);
                        entity.setLastUpdate(model.getLastUpdate().toLocalDateTime());
                        entitiesToUpdate.add(entity);
                        modelsInUpdateList.add(model);
                    } else {
                        notUpdatedEntities.add(entity);
                        notUpdatedModels.add(model);
                    }
                    count++;
                    continue;
                }
            }

            model.setId(null);

            final ENTITY newEntity = this.completeConvertModelToEntity(model, auth);
            entitiesToCreate.add(newEntity);
            modelsInCreateList.add(model);
        }

        final Tuple2<List<DATA_MODEL>, Tuple2<Tuple2<List<DATA_MODEL>, List<ENTITY>>, List<DATA_MODEL>>>
                result = new Tuple2<>();

        result.setFirst(saveEntitiesAndUpdateModels(entitiesToCreate, modelsInCreateList, auth, true).getFirst());

        final Tuple2<List<DATA_MODEL>, List<ENTITY>> updateResult =
                saveEntitiesAndUpdateModels(entitiesToUpdate, modelsInUpdateList, auth, false);
        final List<DATA_MODEL> notUpdateResult = new ArrayList<>();

        count = 0;

        for (ENTITY notUpdatedEntity : notUpdatedEntities) {
            final DATA_MODEL notUpdatedModel = this.completeConvertEntityToModel(notUpdatedEntity);
            notUpdatedModel.setLocalId(notUpdatedModels.get(count).getLocalId());
            notUpdateResult.add(notUpdatedModel);
            count++;
        }

        final Tuple2<Tuple2<List<DATA_MODEL>, List<ENTITY>>, List<DATA_MODEL>> secondResult = new Tuple2<>();
        secondResult.setFirst(updateResult);
        secondResult.setSecond(notUpdateResult);

        result.setSecond(secondResult);

        return result;
    }

    private List<DATA_MODEL> syncWithEdit(Auth auth, SynchronizableSaveSyncModel<ID, DATA_MODEL> model) throws AuthNullException, ConvertModelToEntityException {
        final List<DATA_MODEL> itemsToSave = model.getSave();
        final List<DATA_MODEL> savedModels = this.internalSaveAll(itemsToSave, auth);
        final List<ID> savedIds = savedModels.stream().map(SynchronizableDataModel::getId).collect(Collectors.toList());
        final List<ENTITY> entities = this.internalGetEntitiesByUserIdExcludingIds(savedIds, auth);
        final List<DATA_MODEL> data = this.completeConvertEntitiesToModels(entities);
        data.addAll(savedModels);
        return data;
    }

    private List<DATA_MODEL> syncWithoutEdit(Auth auth) throws AuthNullException {
        final List<ENTITY> entities  = this.getAllEntities(auth);
        return this.completeConvertEntitiesToModels(entities);
    }

    private DATA_MODEL create(DATA_MODEL model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        ENTITY entity = this.completeConvertModelToEntity(model, auth);
        entity.setLastUpdate(model.getLastUpdate().toLocalDateTime());
        entity = repository.save(entity);

        afterDataCreated(entity, auth);

        return this.completeConvertEntityToModel(entity);
    }

    private DATA_MODEL update(ENTITY entity, DATA_MODEL model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        if (this.canChange(entity, model)) {
            this.internalUpdateEntity(entity, model, auth);
            entity.setLastUpdate(model.getLastUpdate().toLocalDateTime());
            entity = repository.save(entity);

            afterDataUpdated(entity, auth);
        }
        return this.completeConvertEntityToModel(entity);
    }

    private boolean delete(ENTITY entity, SynchronizableDeleteModel<ID> model, Auth auth) {
        if (this.canChange(entity, model)) {
            this.beforeDataDeleted(entity, auth);

            repository.delete(entity);

            this.afterDataDeleted(entity, auth);

            return true;
        }

        return false;
    }

    private List<DATA_MODEL> createEntities(List<DATA_MODEL> items, Auth auth) throws AuthNullException, ConvertModelToEntityException {
        final List<ENTITY> entitiesToSave = new ArrayList<>();
        final List<DATA_MODEL> modelsInSaveList = new ArrayList<>();
        this.treatBeforeSaveAll(items);
        for (DATA_MODEL item : items) {
            final ENTITY entity = this.completeConvertModelToEntity(item, auth);
            entitiesToSave.add(entity);
            modelsInSaveList.add(item);
        }

        return saveEntitiesAndUpdateModels(entitiesToSave, modelsInSaveList, auth, true).getFirst();
    }

    private Tuple2<List<DATA_MODEL>, List<ENTITY>>
    saveEntitiesAndUpdateModels(List<ENTITY> entitiesToSave, List<DATA_MODEL> modelsInSaveList, Auth auth, boolean create) {
        final List<DATA_MODEL> updatedModels = new ArrayList<>();

        int count = 0;

        final List<ENTITY> entities = Lists.newArrayList(repository.saveAll(entitiesToSave));

        for (ENTITY entity : entities) {
            if (create) {
                this.afterDataCreated(entity, auth);
            } else {
                this.afterDataUpdated(entity, auth);
            }

            final DATA_MODEL originalModel = modelsInSaveList.get(count);
            final DATA_MODEL model = this.completeConvertEntityToModel(entity);
            model.setLocalId(originalModel.getLocalId());
            updatedModels.add(model);
            count++;
        }

        final Tuple2<List<DATA_MODEL>, List<ENTITY>> result = new Tuple2<>();
        result.setFirst(updatedModels);
        result.setSecond(entities);

        return result;
    }

    protected void sendUpdateMessage(DATA_MODEL model, Auth auth) {
        List<DATA_MODEL> data = new ArrayList<>();
        data.add(model);
        this.sendUpdateMessage(data, auth);
    }

    protected void sendUpdateMessage(List<DATA_MODEL> data, Auth auth) {
        synchronizableMessageService.sendUpdateMessage(data, this.getWebSocketDestination(), auth);
    }

    protected void sendDeleteMessage(ID id, Auth auth) {
        final List<ID> data = new ArrayList<>();
        data.add(id);
        this.sendDeleteMessage(data, auth);
    }

    protected void sendDeleteMessage(List<ID> data, Auth auth) {
        synchronizableMessageService.sendDeleteMessage(data, this.getWebSocketDestination(), auth);
    }

    private boolean hasEditPermission(Auth auth) {
        final List<PermissionEnum> necessaryPermissions = this.getNecessaryPermissionsToEdit();
        if (necessaryPermissions.isEmpty()) return true;

        final String userPermission = auth.getUser().getPermission();
        return necessaryPermissions.stream().anyMatch(permission -> permission.getValue().equals(userPermission));
    }

    private void validateEditPermission(Auth auth) throws UserWithoutPermissionException {
        if (this.hasEditPermission(auth)) return;

        throw new UserWithoutPermissionException();
    }

    private <T> ResponseEntity<T> createResponse(T response) {
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private <T> ResponseEntity<T> createForbiddenResponse(T response) {
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    private Auth getFakeAuth(User user) {
        final Auth fakeAuth = new Auth();
        fakeAuth.setUser(user);

        return fakeAuth;
    }

    private <T extends SynchronizableGenericResponseModel> ResponseEntity<T>
    createUserWithoutPermissionExceptionResponse(UserWithoutPermissionException e, T response) {
        response.setSuccess(false);
        response.setError(e.getMessage());
        return this.createForbiddenResponse(response);
    }


    private <T extends SynchronizableGenericResponseModel> ResponseEntity<T>
    createSynchronizableExceptionResponse(SynchronizableException e, T response) {
        response.setSuccess(false);
        response.setError(e.getMessage());
        return this.createResponse(response);
    }

    private <T extends SynchronizableGenericResponseModel> ResponseEntity<T>
    createUnknownExceptionResponse(Exception e, T response) {
        this.logAPIError(e);
        response.setSuccess(false);
        response.setError(SynchronizableConstants.UNKNOWN_ERROR);
        return this.createResponse(response);
    }
}

