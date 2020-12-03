package br.ufrgs.inf.pet.dinoapi.model.synchronizable.request;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataModel;

/**
 * Model for save a list of synchronizable entity
 * @param <ENTITY>: synchronizable entity type
 * @param <ID>: synchronizable entity id
 * @param <DATA_MODEL>: data model for synchronizable entity
 */
public class SynchronizableSaveAllListModel<
        ENTITY extends SynchronizableEntity<ID>,
        ID extends Comparable<ID>,
        DATA_MODEL extends SynchronizableDataModel<ENTITY, ID>>
        extends SynchronizableGenericListModel<ID, DATA_MODEL> {
}
