package br.ufrgs.inf.pet.dinoapi.model.synchronizable.request;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataModel;

import java.io.Serializable;

/**
 * Model for save a list of synchronizable entity
 * @param <ID>: synchronizable entity id
 * @param <DATA_MODEL>: data model for synchronizable entity
 */
public class SynchronizableSaveAllListModel<
        ID extends Comparable<ID> & Serializable,
        DATA_MODEL extends SynchronizableDataModel<ID>>
        extends SynchronizableGenericListModel<ID, DATA_MODEL> {
}
