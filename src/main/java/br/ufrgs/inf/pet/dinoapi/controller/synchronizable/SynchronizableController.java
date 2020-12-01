package br.ufrgs.inf.pet.dinoapi.controller.synchronizable;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.*;
import org.springframework.http.ResponseEntity;

/**
 * Base Controller with get, getAll, save/update and delete for synchronizable entity
 *
 * @param <ID> Type of synchronizable entity ID
 * @param <DATA_MODEL> Data model of synchronizable entity
 */
public interface SynchronizableController<ENTITY extends SynchronizableEntity<ID>, ID, DATA_MODEL extends SynchronizableDataModel<ENTITY, ID>> {
    /**
     * Search for a item using id and lastUpdate date
     * @param model: object with search data
     * @return if server version exists:
     *              - return server version
     *         otherwise:
     *              - return error
     */
    ResponseEntity<SynchronizableResponseModel<ENTITY, ID, DATA_MODEL>> get(SynchronizableGetModel<ID> model);

    /**
     * Save a object on server, if exists update based in lastUpdate
     * @param model: object with new data
     * @return if server version exists:
     *              - if server version is more updated return server version
     *              - otherwise save new version and return new server version
     *         otherwise:
     *              - create new server data and return it
     */
    ResponseEntity<SynchronizableResponseModel<ENTITY, ID, DATA_MODEL>> save(DATA_MODEL model);

    /**
     * Delete an element if exists
     * @param model: object with delete data
     * @return if server version exists:
     *              - if server version is more updated return server version
     *              - otherwise delete server version and return success
     *         otherwise:
     *              - return error
     */
    ResponseEntity<SynchronizableResponseModel<ENTITY, ID, DATA_MODEL>> delete(SynchronizableDeleteModel<ID> model);

    /**
     * Get all elements related to current user
     * @return if server version exists:
     *              - if server version is more updated return server version
     *              - otherwise delete server version and return success
     *         otherwise:
     *              - return error
     */
    ResponseEntity<SynchronizableListResponseModel<ENTITY, ID, DATA_MODEL>> getAll();
}
