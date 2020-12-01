package br.ufrgs.inf.pet.dinoapi.controller.synchronizable;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableGetModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableResponseModel;
import org.springframework.http.ResponseEntity;

/**
 * Base for Synchronizable Controller
 * @param <ID> Type of synchronizable entity ID
 * @param <DATA_MODEL> Data model of synchronizable entity
 */
public interface SynchronizableController<ID, DATA_MODEL extends SynchronizableDataModel> {
    /**
     * Search for a item using id and lastUpdate date
     * @param model: object with search data
     * @return if server version exists:
     *              - return server version
     *         otherwise:
     *              - return error
     */
    ResponseEntity<SynchronizableResponseModel> get(SynchronizableGetModel<ID> model);

    /**
     * Save a object on server, if exists update based in lastUpdate
     * @param model: object with new data
     * @return if server version exists:
     *              - if server version is more updated return server version
     *              - otherwise save new version and return new server version
     *         otherwise:
     *              - create new server data and return it
     */
    ResponseEntity<SynchronizableResponseModel> save(DATA_MODEL model);

    /**
     * Delete an element if exists
     * @param model: object with delete data
     * @return if server version exists:
     *              - if server version is more updated return server version
     *              - otherwise delete server version and return success
     *         otherwise:
     *              - return error
     */
    ResponseEntity<SynchronizableResponseModel> delete(SynchronizableDeleteModel<ID> model);
}
