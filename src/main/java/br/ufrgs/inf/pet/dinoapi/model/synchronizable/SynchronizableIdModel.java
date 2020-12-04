package br.ufrgs.inf.pet.dinoapi.model.synchronizable;

import java.io.Serializable;

/**
 * Base for model with get synchronizable entity id
 * @param <ID>
 */
public interface SynchronizableIdModel<ID extends Comparable<ID> & Serializable> {
    ID getId();
}
