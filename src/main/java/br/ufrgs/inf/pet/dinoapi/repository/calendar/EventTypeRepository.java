package br.ufrgs.inf.pet.dinoapi.repository.calendar;

import br.ufrgs.inf.pet.dinoapi.entity.calendar.EventType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventTypeRepository extends CrudRepository<EventType, Long> {

    @Query("SELECT et FROM EventType et WHERE et.id = :id AND et.user.id = :userId")
    Optional<EventType> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT et FROM EventType et WHERE et.id IN :ids AND et.user.id = :userId")
    List<EventType> findAllByIdsAndUserId(List<Long> ids, Long userId);

    @Query("SELECT et FROM EventType et WHERE et.id NOT IN :ids AND et.user.id = :userId")
    List<EventType> findAllByUserIdExcludingIds(Long userId, List<Long> ids);

    @Query("SELECT et FROM EventType et WHERE et.user.id = :userId")
    List<EventType> findAllByUserId(Long userId);
}
