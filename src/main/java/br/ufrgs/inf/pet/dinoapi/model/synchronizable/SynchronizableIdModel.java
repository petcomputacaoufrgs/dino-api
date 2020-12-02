package br.ufrgs.inf.pet.dinoapi.model.synchronizable;

/**
 * Base for model with get synchronizable entity id
 * @param <ID>
 */
public interface SynchronizableIdModel<ID extends Comparable<ID>> {
    ID getId();
}
