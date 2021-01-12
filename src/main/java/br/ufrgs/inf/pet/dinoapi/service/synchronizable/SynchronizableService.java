package br.ufrgs.inf.pet.dinoapi.service.synchronizable;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.*;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.*;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Base service with get, getAll, save/update and delete for synchronizable entity
 *
 * @param <ENTITY> Synchronizable entity
 * @param <ID> Id type of synchronizable entity
 * @param <DATA_MODEL> Data model of synchronizable entity
 */
public interface SynchronizableService<
        ENTITY extends SynchronizableEntity<ID>,
        ID extends Comparable<ID> & Serializable,
        LOCAL_ID,
        DATA_MODEL extends SynchronizableDataLocalIdModel<ID, LOCAL_ID>> {

    /**
     * Create a complete data model ({@link DATA_MODEL}) based in an entity ({@link ENTITY})
     * @exception NullPointerException service will throws this exception if this method returns null
     * @param entity base entity
     * @return data model
     */
    DATA_MODEL convertEntityToModel(ENTITY entity);

    /**
     * Create a new entity ({@link ENTITY}) based in a data model ({@link DATA_MODEL})
     * @exception NullPointerException service will throws this exception if this method returns null
     * @param model data model
     * @return entity
     */
    ENTITY convertModelToEntity(DATA_MODEL model, Auth auth) throws ConvertModelToEntityException, AuthNullException;

    /**
     * Update entity's ({@link ENTITY}) attributes based in a data model ({@link DATA_MODEL})
     * @param entity entity
     * @param model data model
     */
    void updateEntity(ENTITY entity, DATA_MODEL model, Auth auth) throws ConvertModelToEntityException, AuthNullException;

    /**
     * Get entity from database based on authenticated user for security validation (only takes data that the user has access)
     * All data here can be edited and deleted by the user.
     * @param id entity's id
     * @param auth current auth user
     * @return database entity if valid params or null
     */
    Optional<ENTITY> getEntityByIdAndUserAuth(ID id, Auth auth) throws AuthNullException;

    /**
     * Get entities from database based on authenticated user for security validation (only takes data that the user has access)
     * Should be used without authentication when user can only see the data (without edit our remove).
     * @param auth current auth user
     * @return list of database entities (can be an empty list)
     */
    List<ENTITY> getEntitiesThatUserCanRead(Auth auth) throws AuthNullException;

    /**
     * Get entities from database using a list of ids based on authenticated user for security validation (only takes data that the user has access)
     * All data here can be edited and deleted by the user.
     * @param ids list of entity ids
     * @param auth current auth user
     * @return list of database entities (can be an empty list)
     */
    List<ENTITY> getEntitiesByIdsAndUserAuth(List<ID> ids, Auth auth) throws AuthNullException;

    /**
     * Get entities from database based on authenticated user except when entity id is in param list "ids"
     * Should be used without authentication when any user can only see the data (without edit our remove).
     * @param auth current auth user
     * @param ids ids of entities to exclude em search
     * @return list of database entities (can be an empty list)
     */
    List<ENTITY> getEntitiesThatUserCanReadExcludingIds(Auth auth, List<ID> ids) throws AuthNullException;

    /**
     * Get WebSocket base destination
     * @return WebSocketDestinationEnum
     */
    WebSocketDestinationsEnum getWebSocketDestination();

    /**
     * Implements get method of {@link br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableController}
     */
    ResponseEntity<SynchronizableDataResponseModelImpl<ID, DATA_MODEL>> get(SynchronizableGetModel<ID> model);

    /**
     * Implements save method of {@link br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableController}
     */
    ResponseEntity<SynchronizableDataResponseModelImpl<ID, DATA_MODEL>> save(DATA_MODEL model);

    /**
     * Implements delete method of {@link br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableController}
     */
    ResponseEntity<SynchronizableDataResponseModelImpl<ID, DATA_MODEL>> delete(SynchronizableDeleteModel<ID> model);

    /**
     * Implements getAll method of {@link br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableController}
     */
    ResponseEntity<SynchronizableListDataResponseModelImpl<ID, DATA_MODEL>> getAll();

    /**
     * Implements saveAll method of {@link br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableController}
     */
    ResponseEntity<SynchronizableSaveAllResponseModel<ID, LOCAL_ID, DATA_MODEL>>
    saveAll(SynchronizableSaveAllModel<ID, LOCAL_ID, DATA_MODEL> model);

    /**
     * Implements deleteAll method of {@link br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableController}
     */
    ResponseEntity<SynchronizableGenericDataResponseModelImpl<List<ID>>>
    deleteAll(SynchronizableDeleteAllListModel<ID> model);


    /**
     * Implements sync method of {@link br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableController}
     */
    ResponseEntity<SynchronizableSyncResponseModel<ID, LOCAL_ID, DATA_MODEL>> sync(SynchronizableSyncModel<ID, LOCAL_ID, DATA_MODEL> model);
}
