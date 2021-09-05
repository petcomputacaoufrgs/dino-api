package br.ufrgs.inf.pet.dinoapi.service.calendar;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.calendar.Event;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.calendar.EventDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.calendar.EventRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.SynchronizableQueueMessageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl extends SynchronizableServiceImpl<Event, Long, EventDataModel, EventRepository> {

    public EventServiceImpl(EventRepository repository, AuthServiceImpl authService, ClockServiceImpl clock, SynchronizableQueueMessageService<Long, EventDataModel> synchronizableQueueMessageService, LogAPIErrorServiceImpl logAPIErrorService) {
        super(repository, authService, clock, synchronizableQueueMessageService, logAPIErrorService);
    }

    @Override
    public EventDataModel convertEntityToModel(Event entity) {
        return null;
    }

    @Override
    public Event convertModelToEntity(EventDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        return null;
    }

    @Override
    public void updateEntity(Event entity, EventDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {

    }

    @Override
    public Optional<Event> findEntityByIdThatUserCanRead(Long aLong, Auth auth) throws AuthNullException {
        return Optional.empty();
    }

    @Override
    public Optional<Event> findEntityByIdThatUserCanEdit(Long aLong, Auth auth) throws AuthNullException {
        return Optional.empty();
    }

    @Override
    public List<Event> findEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        return null;
    }

    @Override
    public List<Event> findEntitiesByIdThatUserCanEdit(List<Long> longs, Auth auth) throws AuthNullException {
        return null;
    }

    @Override
    public List<Event> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> longs) throws AuthNullException {
        return null;
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return null;
    }
}
