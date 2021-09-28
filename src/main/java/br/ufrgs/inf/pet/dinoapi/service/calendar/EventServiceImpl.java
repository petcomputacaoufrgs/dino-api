package br.ufrgs.inf.pet.dinoapi.service.calendar;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.calendar.Event;
import br.ufrgs.inf.pet.dinoapi.entity.calendar.EventType;
import br.ufrgs.inf.pet.dinoapi.entity.calendar.GoogleEvent;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.calendar.EventDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.calendar.EventRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.calendar.google.AsyncGoogleCalendarServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.calendar.google.GoogleCalendarServiceImpl;
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

    private final EventTypeServiceImpl eventTypeServiceImpl;
    private final AsyncGoogleCalendarServiceImpl asyncGoogleCalendarService;
    private final GoogleCalendarServiceImpl googleCalendarService;


    public EventServiceImpl(EventTypeServiceImpl eventTypeServiceImpl,
                            AsyncGoogleCalendarServiceImpl asyncGoogleCalendarService,
                            GoogleCalendarServiceImpl googleCalendarService,
                            EventRepository repository, AuthServiceImpl authService, ClockServiceImpl clock, SynchronizableQueueMessageService<Long, EventDataModel> synchronizableQueueMessageService, LogAPIErrorServiceImpl logAPIErrorService) {
        super(repository, authService, clock, synchronizableQueueMessageService, logAPIErrorService);
        this.eventTypeServiceImpl = eventTypeServiceImpl;
        this.asyncGoogleCalendarService = asyncGoogleCalendarService;
        this.googleCalendarService = googleCalendarService;
    }

    @Override
    public EventDataModel convertEntityToModel(Event entity) {
        final EventDataModel model = new EventDataModel();
        model.setTitle(entity.getTitle());
        model.setDescription(entity.getDescription());
        model.setStart(entity.getStart());
        model.setEnd(entity.getEnd());
        model.setTypeId(entity.getType().getId());

        return model;
    }

    @Override
    public Event convertModelToEntity(EventDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        if (auth != null) {
            final Event entity = new Event();
            entity.setTitle(model.getTitle());
            entity.setDescription(model.getDescription());
            entity.setStart(model.getStart());
            entity.setEnd(model.getEnd());
            entity.setUser(auth.getUser());

            if(model.getTypeId() != null) {
                Optional<EventType> eventTypeSearch = eventTypeServiceImpl.findEntityByIdAndUserId(model.getTypeId(), auth);
                eventTypeSearch.ifPresent(entity::setType);
            }

            return entity;
        } else throw new AuthNullException();
    }

    @Override
    public void updateEntity(Event entity, EventDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        if (auth != null) {
            entity.setTitle(model.getTitle());
            entity.setDescription(model.getDescription());
            entity.setStart(model.getStart());
            entity.setEnd(model.getEnd());
            entity.setUser(auth.getUser());

            if(model.getTypeId() != null && !model.getTypeId().equals(entity.getType().getId())) {
                Optional<EventType> eventTypeSearch = eventTypeServiceImpl.findEntityByIdAndUserId(model.getTypeId(), auth);
                eventTypeSearch.ifPresent(entity::setType);
            }
        } else throw new AuthNullException();
    }

    public Optional<Event> findEntityByIdAndUserId(Long id, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findByIdAndUserId(id, auth.getUser().getId());
    }

    @Override
    public Optional<Event> findEntityByIdThatUserCanRead(Long id, Auth auth) throws AuthNullException {
        return this.findEntityByIdAndUserId(id, auth);
    }

    @Override
    public Optional<Event> findEntityByIdThatUserCanEdit(Long id, Auth auth) throws AuthNullException {
        return this.findEntityByIdAndUserId(id, auth);
    }

    @Override
    public List<Event> findEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByUserId(auth.getUser().getId());
    }

    @Override
    public List<Event> findEntitiesByIdThatUserCanEdit(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByIdsAndUserId(ids, auth.getUser().getId());
    }

    @Override
    public List<Event> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByUserIdExcludingIds(auth.getUser().getId(), ids);
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.EVENT;
    }

    @Override
    protected void afterDataCreated(Event entity, Auth auth) {
        asyncGoogleCalendarService.createEventOnGoogleAPI(entity, auth);
    }

    @Override
    protected void afterDataUpdated(Event entity, Auth auth) {
        asyncGoogleCalendarService.updateEventOnGoogleAPI(entity, auth);
    }

    @Override
    protected void beforeDataDeleted(Event entity, Auth auth) {
        final Optional<GoogleEvent> googleEventSearch = this.googleCalendarService.findByEventId(entity.getId());

        googleEventSearch.ifPresent(googleEvent ->
                asyncGoogleCalendarService.deleteEventOnGoogleAPI(googleEvent, auth));
    }
}
