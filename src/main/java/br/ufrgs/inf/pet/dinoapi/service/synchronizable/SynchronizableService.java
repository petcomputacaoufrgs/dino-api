package br.ufrgs.inf.pet.dinoapi.service.synchronizable;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

/**
 * Base service with get, getAll, save/update and delete for synchronizable entity
 *
 * @param <ENTITY> Synchronizable entity
 * @param <ID> Id type of synchronizable entity
 * @param <DATA_MODEL> Data model of synchronizable entity
 */
public interface SynchronizableService<ENTITY extends SynchronizableEntity<ID>,
        ID, DATA_MODEL extends SynchronizableDataModel<ENTITY, ID>> {

    /**
     * Create a complete data model ({@link DATA_MODEL}) based in an entity ({@link ENTITY})
     * @exception NullPointerException service will throws this exception if this method returns null
     * @param entity: base entity
     * @return data model
     */
    DATA_MODEL createDataModel(ENTITY entity);

    /**
     * Create a new entity ({@link ENTITY}) based in a data model ({@link DATA_MODEL})
     * @exception NullPointerException service will throws this exception if this method returns null
     * @param model: data model
     * @return entity
     */
    ENTITY createEntity(DATA_MODEL model);

    /**
     * Update entity's ({@link ENTITY}) attributes based in a data model ({@link DATA_MODEL})
     * @param entity: entity
     * @param model: data model
     */
    void updateEntity(ENTITY entity, DATA_MODEL model);

    /**
     * Get entity from database using userId for security validation (only takes data that the user has access)
     * @param id: entity's id
     * @param userId: user's id
     * @return database entity if valid params or null
     */
    Optional<ENTITY> getEntityByIdAndUserId(ID id, Long userId);

    /**
     * Get entities from database using userId for security validation (only takes data that the user has access)
     * @param userId: user's id
     * @return list of database entities (can be an empty list)
     */
    List<ENTITY> getEntitiesByUserId(Long userId);

    /**
     * Implements get method of {@link br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableController}
     */
    ResponseEntity<SynchronizableResponseModel<ENTITY, ID, DATA_MODEL>> get(SynchronizableGetModel<ID> model);

    /**
     * Implements save method of {@link br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableController}
     */
    ResponseEntity<SynchronizableResponseModel<ENTITY, ID, DATA_MODEL>> save(DATA_MODEL model);

    /**
     * Implements delete method of {@link br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableController}
     */
    ResponseEntity<SynchronizableResponseModel<ENTITY, ID, DATA_MODEL>> delete(SynchronizableDeleteModel<ID> model);

    /**
     * Implements getAll method of {@link br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableController}
     */
    ResponseEntity<SynchronizableListResponseModel<ENTITY, ID, DATA_MODEL>> getAll();
}
