package br.ufrgs.inf.pet.dinoapi.controller.synchronizable;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.*;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteAllModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableGetModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableSaveAllModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.SynchronizableDeleteAllResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.SynchronizableListResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.SynchronizableResponseModel;
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
     * Save a object on server, if exists update based in lastUpdate
     * @return if server version exists:
     *              - if server version is more updated return server version
     *              - otherwise delete server version and return success
     *         otherwise:
     *              - return error
     */
    ResponseEntity<SynchronizableListResponseModel<ENTITY, ID, DATA_MODEL>> getAll();

    /**
     * Save a list of elements if exists
     * @return for each element:
     *              if server version exists:
     *                  - if server version is more updated do nothing
     *                  - otherwise update server version
     *              otherwise:
     *                  -  create new server data and return it
     */
    ResponseEntity<SynchronizableListResponseModel<ENTITY, ID, DATA_MODEL>> saveAll(SynchronizableSaveAllModel<ENTITY, ID, DATA_MODEL> model);

    /**
     * Delete a list of elements if exists
     * @param model: model with ids and delete infos
     * @return for each element:
     *              if server version exists:
     *                  - if server version is more updated do nothing
     *                  - otherwise delete server version
     *              otherwise:
     *                  - do nothing
     */
    ResponseEntity<SynchronizableDeleteAllResponseModel<ID>> deleteAll(SynchronizableDeleteAllModel<ID> model);
}
