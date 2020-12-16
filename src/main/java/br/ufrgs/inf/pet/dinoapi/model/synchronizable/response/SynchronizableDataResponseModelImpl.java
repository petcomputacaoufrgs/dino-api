package br.ufrgs.inf.pet.dinoapi.model.synchronizable.response;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataModel;

import java.io.Serializable;

/**
 * Response model for synchronizable entity
 * @param <ID>: synchronizable entity id's type
 * @param <DATA_MODEL>: data model of synchronizable entity
 */
public class SynchronizableDataResponseModelImpl<
        ID extends Comparable<ID> & Serializable,
        LOCAL_ID,
        DATA_MODEL extends SynchronizableDataModel<ID, LOCAL_ID>> extends SynchronizableGenericDataResponseModelImpl<DATA_MODEL> {
}
