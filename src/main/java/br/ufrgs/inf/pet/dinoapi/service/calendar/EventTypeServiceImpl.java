package br.ufrgs.inf.pet.dinoapi.service.calendar;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.calendar.EventType;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.calendar.EventTypeDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.calendar.EventTypeRepository;
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
public class EventTypeServiceImpl extends SynchronizableServiceImpl<EventType, Long, EventTypeDataModel, EventTypeRepository> {
    public EventTypeServiceImpl(EventTypeRepository repository, AuthServiceImpl authService, ClockServiceImpl clock, SynchronizableQueueMessageService<Long, EventTypeDataModel> synchronizableQueueMessageService, LogAPIErrorServiceImpl logAPIErrorService) {
        super(repository, authService, clock, synchronizableQueueMessageService, logAPIErrorService);
    }

    @Override
    public EventTypeDataModel convertEntityToModel(EventType entity) {
        return null;
    }

    @Override
    public EventType convertModelToEntity(EventTypeDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        return null;
    }

    @Override
    public void updateEntity(EventType entity, EventTypeDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {

    }

    @Override
    public Optional<EventType> findEntityByIdThatUserCanRead(Long aLong, Auth auth) throws AuthNullException {
        return Optional.empty();
    }

    @Override
    public Optional<EventType> findEntityByIdThatUserCanEdit(Long aLong, Auth auth) throws AuthNullException {
        return Optional.empty();
    }

    @Override
    public List<EventType> findEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        return null;
    }

    @Override
    public List<EventType> findEntitiesByIdThatUserCanEdit(List<Long> longs, Auth auth) throws AuthNullException {
        return null;
    }

    @Override
    public List<EventType> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> longs) throws AuthNullException {
        return null;
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return null;
    }
}
