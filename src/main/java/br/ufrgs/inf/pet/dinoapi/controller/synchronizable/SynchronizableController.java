package br.ufrgs.inf.pet.dinoapi.controller.synchronizable;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.*;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.*;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.*;
import br.ufrgs.inf.pet.dinoapi.websocket.model.SynchronizableWSDeleteModel;
import br.ufrgs.inf.pet.dinoapi.websocket.model.SynchronizableWSUpdateModel;
import org.springframework.http.ResponseEntity;
import java.io.Serializable;
import java.util.List;

/**
 * Base Controller with get, getAll, save/update and delete for synchronizable entity
 *
 * @param <ID> Type of synchronizable entity ID
 * @param <DATA_MODEL> Data model of synchronizable entity
 */
public interface SynchronizableController<
        ID extends Comparable<ID> & Serializable,
        DATA_MODEL extends SynchronizableDataLocalIdModel<ID>> {
    /**
     * Search for a item using id and lastUpdate date
     * @param model: object with search data
     * @return if server version exists:
     *              - return server version
     *         otherwise:
     *              - return error
     */
    ResponseEntity<SynchronizableDataResponseModelImpl<ID, DATA_MODEL>> get(SynchronizableGetModel<ID> model);

    /**
     * Save a object on server, if exists update based in lastUpdate
     * @param model: object with new data
     * @return if server version exists:
     *              - if server version is more updated return server version
     *              - otherwise save new version and return new server version
     *                  - can return error if conversion of model to entity fails
     *         otherwise:
     *              - create new server data and return it
     *
     * Obs.: If save/update entity then send the server version by websocket to related users using {@link SynchronizableWSUpdateModel}
     */
    ResponseEntity<SynchronizableDataResponseModelImpl<ID, DATA_MODEL>> save(DATA_MODEL model);

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
    ResponseEntity<SynchronizableDataResponseModelImpl<ID, DATA_MODEL>> delete(SynchronizableDeleteModel<ID> model);

    /**
     * Get all entities related to user
     * @return model with list of entities
     */
    ResponseEntity<SynchronizableListDataResponseModelImpl<ID, DATA_MODEL>> getAll();

    /**
     * Save a list of elements if exists
     *
     * for each element:
     *      if server version exists:
     *          - if server version is more updated do nothing
     *          - otherwise update server version
     *              - if conversion of model to entity fail item is ignored
     *      otherwise:
     *          - create new server data
     *              - if conversion of model to entity fail item is ignored
     *
     * @return model with list of saved entities
     *
     * Obs.: If save/update entities then send the updated entities by websocket to related users using {@link SynchronizableWSUpdateModel}
     */
    ResponseEntity<SynchronizableSaveAllResponseModel<ID, DATA_MODEL>> saveAll(SynchronizableSaveAllModel<ID, DATA_MODEL> model);

    /**
     * Delete a list of elements if exists
     * @param model: model with ids and delete infos
     * for each element:
     *         if server version exists:
     *             - if server version is more updated do nothing
     *             - otherwise delete server version and return it's id
     *         otherwise:
     *             - do nothing
     *
     * @return model with list of deleted entities ids
     * Obs.: If delete entities then send the deleted ids by websocket to related users using {@link SynchronizableWSDeleteModel}
     */
    ResponseEntity<SynchronizableGenericDataResponseModelImpl<List<ID>>> deleteAll(SynchronizableDeleteAllListModel<ID> model);

    /**
     * Sync items: Save and update items based on last update date and return result data
     *
     * @return model with all items
     */
    ResponseEntity<SynchronizableSyncResponseModel<ID, DATA_MODEL>> sync(SynchronizableSaveSyncModel<ID, DATA_MODEL> model);
}
