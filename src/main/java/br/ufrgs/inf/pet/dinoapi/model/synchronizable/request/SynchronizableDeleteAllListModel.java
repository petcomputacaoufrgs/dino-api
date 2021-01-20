package br.ufrgs.inf.pet.dinoapi.model.synchronizable.request;

import java.io.Serializable;

/**
 * Model for delete a list of synchronizable entity
 *
 * @param <ID> Id type of synchronizable entity
 */
public class SynchronizableDeleteAllListModel<ID extends Comparable<ID> & Serializable>
        extends SynchronizableGenericListModel<ID, SynchronizableDeleteModel<ID>> {
}
