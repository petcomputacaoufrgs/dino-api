package br.ufrgs.inf.pet.dinoapi.model.synchronizable.request;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;
import java.io.Serializable;

/**
 * Model for save a list of synchronizable entity
 * @param <ID>: synchronizable entity id
 * @param <LOCAL_ID>: local id for app control
 * @param <DATA_MODEL>: data model for synchronizable entity
 */
public class SynchronizableSaveAllModel<
        ID extends Comparable<ID> & Serializable,
        DATA_MODEL extends SynchronizableDataLocalIdModel<ID>>
        extends SynchronizableGenericListLocalIdModel<ID, DATA_MODEL> {
}
