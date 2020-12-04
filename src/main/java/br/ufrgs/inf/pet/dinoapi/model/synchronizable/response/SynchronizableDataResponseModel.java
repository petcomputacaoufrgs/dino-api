package br.ufrgs.inf.pet.dinoapi.model.synchronizable.response;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataModel;

import java.io.Serializable;

/**
 * Response model for synchronizable entity
 * @param <ID>: synchronizable entity id's type
 * @param <DATA_MODEL>: data model of synchronizable entity
 */
public class SynchronizableDataResponseModel<
        ID extends Comparable<ID> & Serializable,
        DATA_MODEL extends SynchronizableDataModel<ID>> extends SynchronizableGenericDataResponseModel<DATA_MODEL> {
}
