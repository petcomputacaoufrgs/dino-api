package br.ufrgs.inf.pet.dinoapi.controller.synchronizable;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.*;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteAllListModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableGetModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableSaveAllListModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.SynchronizableGenericResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.SynchronizableListDataResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.SynchronizableDataResponseModel;
import br.ufrgs.inf.pet.dinoapi.websocket.model.synchronizable.SynchronizableWSDeleteModel;
import br.ufrgs.inf.pet.dinoapi.websocket.model.synchronizable.SynchronizableWSUpdateModel;
import org.springframework.http.ResponseEntity;

/**
 * Base Controller with get, getAll, save/update and delete for synchronizable entity
 *
 * @param <ID> Type of synchronizable entity ID
 * @param <DATA_MODEL> Data model of synchronizable entity
 */
public interface SynchronizableController<ENTITY extends SynchronizableEntity<ID>, ID extends Comparable<ID>, DATA_MODEL extends SynchronizableDataModel<ENTITY, ID>> {
    /**
     * Search for a item using id and lastUpdate date
     * @param model: object with search data
     * @return if server version exists:
     *              - return server version
     *         otherwise:
     *              - return error
     */
    ResponseEntity<SynchronizableDataResponseModel<ENTITY, ID, DATA_MODEL>> get(SynchronizableGetModel<ID> model);

    /**
     * Save a object on server, if exists update based in lastUpdate
     * @param model: object with new data
     * @return if server version exists:
     *              - if server version is more updated return server version
     *              - otherwise save new version and return new server version
     *         otherwise:
     *              - create new server data and return it
     *
     * Obs.: If save/update entity then send the server version by websocket to related users using {@link SynchronizableWSUpdateModel}
     */
    ResponseEntity<SynchronizableDataResponseModel<ENTITY, ID, DATA_MODEL>> save(DATA_MODEL model);

    /**
     * Delete an element if exists
     * @param model: object with delete data
     * @return if server version exists:
     *              - if server version is more updated return server version
     *              - otherwise delete server version and return success
     *         otherwise:
     *              - return error
     *
     * Obs.: If save/update entity then send the server version by websocket to related users using {@link SynchronizableWSDeleteModel}
     */
    ResponseEntity<SynchronizableDataResponseModel<ENTITY, ID, DATA_MODEL>> delete(SynchronizableDeleteModel<ID> model);

    /**
     * Get all entities related to user
     * @return model with list of entities
     */
    ResponseEntity<SynchronizableListDataResponseModel<ENTITY, ID, DATA_MODEL>> getAll();

    /**
     * Save a list of elements if exists
     *
     * for each element:
     *      if server version exists:
     *          - if server version is more updated do nothing
     *          - otherwise update server version
     *      otherwise:
     *          - create new server data
     *
     * @return Always return success
     *
     * Obs.: If save/update entities then send the updated entities by websocket to related users using {@link SynchronizableWSUpdateModel}
     */
    ResponseEntity<SynchronizableGenericResponseModel> saveAll(SynchronizableSaveAllListModel<ENTITY, ID, DATA_MODEL> model);

    /**
     * Delete a list of elements if exists
     * @param model: model with ids and delete infos
     * @return for each element:
     *              if server version exists:
     *                  - if server version is more updated do nothing
     *                  - otherwise delete server version and return it's id
     *              otherwise:
     *                  - do nothing
     *
     * Obs.: If delete entities then send the deleted ids by websocket to related users using {@link SynchronizableWSDeleteModel}
     */
    ResponseEntity<SynchronizableGenericResponseModel> deleteAll(SynchronizableDeleteAllListModel<ID> model);
}
