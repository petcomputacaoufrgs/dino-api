package br.ufrgs.inf.pet.dinoapi.model.synchronizable;

import java.time.LocalDateTime;

public interface SynchronizableModel {
    Long getId();

    LocalDateTime getLastUpdate();
}
