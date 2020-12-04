package br.ufrgs.inf.pet.dinoapi.service.teste;

import br.ufrgs.inf.pet.dinoapi.entity.teste.TesteEntity;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.teste.TesteDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.teste.TesteRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.GenericQueueMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TesteServiceImpl extends SynchronizableServiceImpl<TesteEntity, Long, TesteDataModel, TesteRepository> {
    @Autowired
    public TesteServiceImpl(TesteRepository repository, AuthServiceImpl authService,
                            GenericQueueMessageServiceImpl genericQueueMessageService) {
        super(repository, authService, genericQueueMessageService);
    }

    @Override
    public TesteDataModel createDataModel(TesteEntity entity) {
        final TesteDataModel model = new TesteDataModel(entity);
        model.setName(entity.getName());

        return model;
    }

    @Override
    public TesteEntity createEntity(TesteDataModel model) {
        final User user = authService.getCurrentUser();
        final TesteEntity entity = new TesteEntity();
        entity.setUser(user);
        entity.setName(model.getName());
        return entity;
    }

    @Override
    public void updateEntity(TesteEntity entity, TesteDataModel model) {
        entity.setName(model.getName());
    }

    @Override
    public Optional<TesteEntity> getEntityByIdAndUserId(Long id, Long userId) {
        return repository.findByIdAndUserId(id, userId);
    }

    @Override
    public List<TesteEntity> getEntitiesByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public List<TesteEntity> getEntitiesByIdsAndUserId(List<Long> ids, Long userId) {
        return repository.findByIdsAndUserId(ids, userId);
    }

    @Override
    public WebSocketDestinationsEnum getUpdateWebsocketDestination() {
        return WebSocketDestinationsEnum.ALERT_APP_SETTINGS_UPDATE;
    }

    @Override
    public WebSocketDestinationsEnum getDeleteWebsocketDestination() {
        return WebSocketDestinationsEnum.ALERT_CONTACT_UPDATE;
    }
}
