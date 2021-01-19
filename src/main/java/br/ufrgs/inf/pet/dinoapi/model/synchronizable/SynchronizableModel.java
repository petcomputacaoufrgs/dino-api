package br.ufrgs.inf.pet.dinoapi.model.synchronizable;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Base model for get synchronizable entity id and lastUpdate
 *
 * @param <ID>
 */
public interface SynchronizableModel<ID extends Comparable<ID> & Serializable> extends SynchronizableIdModel<ID> {
    ZonedDateTime getLastUpdate();
}
