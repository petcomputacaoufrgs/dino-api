package br.ufrgs.inf.pet.dinoapi.repository.synchronizable;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SynchronizableRepository<T extends SynchronizableEntity> extends CrudRepository<T, Long> {
    Optional<T> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userID);
}
