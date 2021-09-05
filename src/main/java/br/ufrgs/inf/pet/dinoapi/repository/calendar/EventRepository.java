package br.ufrgs.inf.pet.dinoapi.repository.calendar;

import br.ufrgs.inf.pet.dinoapi.entity.calendar.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {
    @Query("SELECT e FROM Event e WHERE e.id = :id AND e.user.id = :userId")
    Optional<Event> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT e FROM Event e WHERE e.id IN :ids AND e.user.id = :userId")
    List<Event> findAllByIdsAndUserId(List<Long> ids, Long userId);

    @Query("SELECT e FROM Event e WHERE e.id NOT IN :ids AND e.user.id = :userId")
    List<Event> findAllByUserIdExcludingIds(Long userId, List<Long> ids);

    @Query("SELECT e FROM Event e WHERE e.user.id = :userId")
    List<Event> findAllByUserId(Long userId);
}
