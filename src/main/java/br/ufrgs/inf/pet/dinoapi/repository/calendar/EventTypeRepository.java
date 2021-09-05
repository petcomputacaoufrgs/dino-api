package br.ufrgs.inf.pet.dinoapi.repository.calendar;

import br.ufrgs.inf.pet.dinoapi.entity.calendar.EventType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTypeRepository extends CrudRepository<EventType, Long> {
}
