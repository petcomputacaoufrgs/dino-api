package br.ufrgs.inf.pet.dinoapi.controller.calendar;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.calendar.Event;
import br.ufrgs.inf.pet.dinoapi.model.calendar.EventDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.calendar.EventRepository;
import br.ufrgs.inf.pet.dinoapi.service.calendar.EventServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.ufrgs.inf.pet.dinoapi.constants.PathConstants.EVENT;

@RestController
@RequestMapping(EVENT)
public class EventController extends SynchronizableControllerImpl<
        Event, Long, EventDataModel, EventRepository, EventServiceImpl> {
    protected EventController(EventServiceImpl service) {
        super(service);
    }
}
