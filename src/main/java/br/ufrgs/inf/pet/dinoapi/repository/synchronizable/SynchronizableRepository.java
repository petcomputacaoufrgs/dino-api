package br.ufrgs.inf.pet.dinoapi.repository.synchronizable;

import br.ufrgs.inf.pet.dinoapi.entity.sync.SynchronizableEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SynchronizableRepository<T extends SynchronizableEntity> extends CrudRepository<T, Long> {
    Optional<T> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userID);
}
