package br.ufrgs.inf.pet.dinoapi.repository.calendar;

import br.ufrgs.inf.pet.dinoapi.entity.calendar.GoogleEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoogleEventRepository extends CrudRepository<GoogleEvent, Long> {
    @Query("SELECT n FROM GoogleEvent n WHERE n.id = :id AND n.event.user.id = :userId")
    Optional<GoogleEvent> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT n FROM GoogleEvent n WHERE n.event.user.id = :id")
    List<GoogleEvent> findAllByUserId(@Param("id") Long id);

    @Query("SELECT n FROM GoogleEvent n WHERE n.id IN :ids AND n.event.user.id = :userId")
    List<GoogleEvent> findAllByIdAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    @Query("SELECT n FROM GoogleEvent n WHERE n.id NOT IN :ids AND n.event.user.id = :userId")
    List<GoogleEvent> findAllByUserIdExcludingIds(@Param("userId") Long userId, @Param("ids") List<Long> ids);

    @Query("SELECT n FROM GoogleEvent n WHERE n.event.id = :eventId")
    Optional<GoogleEvent> findByEventId(@Param("eventId") Long eventId);

}
