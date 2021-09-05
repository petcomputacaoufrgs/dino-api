package br.ufrgs.inf.pet.dinoapi.controller.calendar;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.calendar.EventType;
import br.ufrgs.inf.pet.dinoapi.model.calendar.EventTypeDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.calendar.EventTypeRepository;
import br.ufrgs.inf.pet.dinoapi.service.calendar.EventTypeServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.ufrgs.inf.pet.dinoapi.constants.PathConstants.EVENT_TYPE;

@RestController
@RequestMapping(EVENT_TYPE)
public class EventTypeController extends SynchronizableControllerImpl<
        EventType, Long, EventTypeDataModel, EventTypeRepository, EventTypeServiceImpl> {
    protected EventTypeController(EventTypeServiceImpl service) {
        super(service);
    }
}
