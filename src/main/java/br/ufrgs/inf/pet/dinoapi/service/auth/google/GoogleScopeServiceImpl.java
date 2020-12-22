package br.ufrgs.inf.pet.dinoapi.service.auth.google;

import br.ufrgs.inf.pet.dinoapi.constants.GoogleAuthConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleScope;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleScopeDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.auth.google.GoogleScopeRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.synchronizable.SynchronizableQueueMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GoogleScopeServiceImpl extends SynchronizableServiceImpl<GoogleScope, Long, Integer, GoogleScopeDataModel, GoogleScopeRepository> {
    @Autowired
    public GoogleScopeServiceImpl(GoogleScopeRepository repository, AuthServiceImpl authService,
                                  SynchronizableQueueMessageServiceImpl<Long, Integer, GoogleScopeDataModel> synchronizableQueueMessageService) {
        super(repository, authService, synchronizableQueueMessageService);
    }

    @Override
    public GoogleScopeDataModel convertEntityToModel(GoogleScope entity) {
        final GoogleScopeDataModel model = new GoogleScopeDataModel();
        model.setName(entity.getName());
        return model;
    }

    @Override
    public GoogleScope convertModelToEntity(GoogleScopeDataModel model) throws ConvertModelToEntityException {
        final GoogleAuth googleAuth = this.getUser().getGoogleAuth();

        if (googleAuth != null) {
            final GoogleScope entity = new GoogleScope();
            entity.setName(model.getName());
            entity.setGoogleAuth(googleAuth);

            return entity;
        } else {
            throw new ConvertModelToEntityException(GoogleAuthConstants.GOOGLE_AUTH_NOT_FOUND);
        }
    }

    @Override
    public void updateEntity(GoogleScope entity, GoogleScopeDataModel model) throws ConvertModelToEntityException {
        entity.setName(model.getName());
    }

    @Override
    public Optional<GoogleScope> getEntityByIdAndUser(Long id, User user) {
        return this.repository.findByIdAndUserId(id, user.getId());
    }

    @Override
    public List<GoogleScope> getEntitiesByUserId(User user) {
        return this.repository.findAllByUserId(user.getId());
    }

    @Override
    public List<GoogleScope> getEntitiesByIdsAndUserId(List<Long> ids, User user) {
        return this.repository.findAllByIdsAndUserId(ids, user.getId());
    }

    @Override
    public List<GoogleScope> getEntitiesByUserIdExceptIds(User user, List<Long> ids) {
        return this.repository.findAllByUserIdExceptIds(user.getId(), ids);
    }

    @Override
    public WebSocketDestinationsEnum getUpdateWebSocketDestination() {
        return WebSocketDestinationsEnum.AUTH_SCOPE_UPDATE;
    }

    @Override
    public WebSocketDestinationsEnum getDeleteWebSocketDestination() {
        return WebSocketDestinationsEnum.AUTH_SCOPE_DELETE;
    }

    public List<GoogleScope> getEntitiesByName(User user, List<String> name) {
        return this.repository.findAllByName(user.getId(), name);
    }

    public List<GoogleScopeDataModel> saveAllScopes(List<String> scopes) {
        final List<GoogleScopeDataModel> model = this.convertScopeStringInDataModel(scopes);
        return this.internalSaveAll(model);
    }

    private List<GoogleScopeDataModel> convertScopeStringInDataModel(List<String> scopes) {
        final LocalDateTime now = LocalDateTime.now();

        return scopes.stream().map(scope -> {
            final GoogleScopeDataModel data = new GoogleScopeDataModel();
            data.setName(scope);
            data.setLastUpdate(now);
            return data;
        }).collect(Collectors.toList());
    }
}
