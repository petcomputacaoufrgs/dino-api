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
        final EventTypeDataModel model = new EventTypeDataModel();
        model.setTitle(entity.getTitle());
        model.setIcon(entity.getIcon());
        model.setColor(entity.getColor());

        return model;
    }

    @Override
    public EventType convertModelToEntity(EventTypeDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        if (auth != null) {
            final EventType entity = new EventType();
            entity.setTitle(model.getTitle());
            entity.setColor(model.getColor());
            entity.setIcon(model.getIcon());
            entity.setUser(auth.getUser());

            return entity;
        } else throw new AuthNullException();
    }

    @Override
    public void updateEntity(EventType entity, EventTypeDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        if (auth != null) {
            entity.setTitle(model.getTitle());
            entity.setColor(model.getColor());
            entity.setIcon(model.getIcon());
        } else throw new AuthNullException();
    }

    public Optional<EventType> findEntityByIdAndUserId(Long id, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findByIdAndUserId(id, auth.getUser().getId());
    }

    @Override
    public Optional<EventType> findEntityByIdThatUserCanRead(Long id, Auth auth) throws AuthNullException {
        return this.findEntityByIdAndUserId(id, auth);
    }

    @Override
    public Optional<EventType> findEntityByIdThatUserCanEdit(Long id, Auth auth) throws AuthNullException {
        return this.findEntityByIdAndUserId(id, auth);
    }

    @Override
    public List<EventType> findEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByUserId(auth.getUser().getId());
    }

    @Override
    public List<EventType> findEntitiesByIdThatUserCanEdit(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByIdsAndUserId(ids, auth.getUser().getId());
    }

    @Override
    public List<EventType> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByUserIdExcludingIds(auth.getUser().getId(), ids);
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.EVENT_TYPE;
    }
}
