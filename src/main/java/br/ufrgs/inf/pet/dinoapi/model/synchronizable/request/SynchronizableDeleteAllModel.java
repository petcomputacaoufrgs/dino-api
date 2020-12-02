package br.ufrgs.inf.pet.dinoapi.model.synchronizable.request;

/**
 * Model for delete a list of synchronizable entity
 * @param <ID> Id type of synchronizable entity
 */
public class SynchronizableDeleteAllModel<ID extends Comparable<ID>>
    extends SynchronizableGenericListModel<ID, SynchronizableDeleteModel<ID>> {
}
