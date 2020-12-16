package br.ufrgs.inf.pet.dinoapi.model.synchronizable.response;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataModel;

import java.io.Serializable;
import java.util.List;

/**
 * Response model for list of synchronizable entity
 * @param <ID>: synchronizable entity id's type
 * @param <DATA_MODEL>: data model of synchronizable entity
 */
public class SynchronizableListDataResponseModelImpl<
        ID extends Comparable<ID> & Serializable,
        LOCAL_ID,
        DATA_MODEL extends SynchronizableDataModel<ID, LOCAL_ID>> extends SynchronizableGenericDataResponseModelImpl<List<DATA_MODEL>> {
}
