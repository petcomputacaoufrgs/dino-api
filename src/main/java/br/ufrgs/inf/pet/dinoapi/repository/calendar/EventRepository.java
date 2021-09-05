package br.ufrgs.inf.pet.dinoapi.repository.calendar;

import br.ufrgs.inf.pet.dinoapi.entity.calendar.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {
}
