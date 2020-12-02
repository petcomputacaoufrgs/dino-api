package br.ufrgs.inf.pet.dinoapi.model.synchronizable;

import java.time.LocalDateTime;

/**
 * Base model for get synchronizable entity id and lastUpdate
 * @param <ID>
 */
public interface SynchronizableModel<ID extends Comparable<ID>> extends SynchronizableIdModel<ID> {
    LocalDateTime getLastUpdate();
}
