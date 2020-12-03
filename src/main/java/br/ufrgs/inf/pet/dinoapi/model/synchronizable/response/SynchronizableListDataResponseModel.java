package br.ufrgs.inf.pet.dinoapi.model.synchronizable.response;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataModel;

import java.util.List;

/**
 * Response model for list of synchronizable entity
 * @param <ENTITY>: synchronizable entity type
 * @param <ID>: synchronizable entity id's type
 * @param <DATA_MODEL>: data model of synchronizable entity
 */
public class SynchronizableListDataResponseModel<
        ENTITY extends SynchronizableEntity<ID>,
        ID extends Comparable<ID>,
        DATA_MODEL extends SynchronizableDataModel<ENTITY, ID>> extends SynchronizableGenericDataResponseModel<List<DATA_MODEL>> {
}
