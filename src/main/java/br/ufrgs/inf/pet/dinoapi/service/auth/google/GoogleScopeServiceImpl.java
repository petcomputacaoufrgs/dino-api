package br.ufrgs.inf.pet.dinoapi.service.auth.google;

import br.ufrgs.inf.pet.dinoapi.constants.AuthConstants;
import br.ufrgs.inf.pet.dinoapi.constants.GoogleAuthConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleScope;
import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.enumerable.GoogleScopeURLEnum;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleScopeDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.auth.google.GoogleScopeRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.SynchronizableQueueMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GoogleScopeServiceImpl extends SynchronizableServiceImpl<GoogleScope, Long, GoogleScopeDataModel, GoogleScopeRepository> {
    @Autowired
    public GoogleScopeServiceImpl(GoogleScopeRepository repository, AuthServiceImpl authService,
                                  ClockServiceImpl clockService, LogAPIErrorServiceImpl logAPIErrorService,
                                  SynchronizableQueueMessageService<Long, GoogleScopeDataModel> synchronizableQueueMessageService) {
        super(repository, authService, clockService, synchronizableQueueMessageService, logAPIErrorService);
    }

    @Override
    public GoogleScopeDataModel convertEntityToModel(GoogleScope entity) {
        final GoogleScopeDataModel model = new GoogleScopeDataModel();
        model.setName(entity.getName());
        return model;
    }

    @Override
    public GoogleScope convertModelToEntity(GoogleScopeDataModel model, Auth auth) throws ConvertModelToEntityException {
        if (auth != null) {
            final User user = auth.getUser();

            final GoogleAuth googleAuth = user.getGoogleAuth();

            if (googleAuth != null) {
                final GoogleScope entity = new GoogleScope();
                entity.setName(model.getName());
                entity.setGoogleAuth(googleAuth);

                return entity;
            }

            throw new ConvertModelToEntityException(GoogleAuthConstants.GOOGLE_AUTH_NOT_FOUND);
        }

        throw new ConvertModelToEntityException(AuthConstants.INVALID_AUTH);
    }

    @Override
    public void updateEntity(GoogleScope entity, GoogleScopeDataModel model, Auth auth) throws ConvertModelToEntityException {
        entity.setName(model.getName());
    }

    @Override
    public Optional<GoogleScope> findEntityByIdThatUserCanRead(Long id, Auth auth) throws AuthNullException {
        return this.findEntityById(id, auth);
    }

    @Override
    public Optional<GoogleScope> findEntityByIdThatUserCanEdit(Long id, Auth auth) throws AuthNullException {
        return this.findEntityById(id, auth);
    }

    private Optional<GoogleScope> findEntityById(Long id, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }

        return this.repository.findByIdAndUserId(id, auth.getUser().getId());
    }

    @Override
    public List<GoogleScope> findEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }

        return this.repository.findAllByUserId(auth.getUser().getId());
    }

    @Override
    public List<GoogleScope> findEntitiesByIdThatUserCanEdit(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByIdsAndUserId(ids, auth.getUser().getId());
    }

    @Override
    public List<GoogleScope> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByUserIdExcludingIds(auth.getUser().getId(), ids);
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.GOOGLE_SCOPE;
    }

    public void deleteAllScopes(List<GoogleScope> scopes, Auth auth) {
        final List<Long> deletedIds = scopes.stream().map(SynchronizableEntity::getId).collect(Collectors.toList());

        repository.deleteAll(scopes);

        this.sendDeleteMessage(deletedIds, auth);
    }

    public List<GoogleScopeDataModel> saveAllScopes(Set<String> scopes, Auth auth) throws AuthNullException, ConvertModelToEntityException {
        final List<GoogleScopeDataModel> model = this.convertScopeStringInDataModel(scopes);
        return this.internalSaveAll(model, auth);
    }

    public List<GoogleScope> getEntitiesByName(User user, List<String> name) {
        return this.repository.findAllByName(user.getId(), name);
    }

    public List<GoogleScopeDataModel> createGoogleScopeDataModels(List<GoogleScope> googleScopes) {
        return this.completeConvertEntitiesToModels(googleScopes);
    }

    public boolean hasGoogleScope(User user, GoogleScopeURLEnum googleScopeURLEnum) {
        final Optional<GoogleScope> contactGoogleScopeSearch =
                this.repository.findByName(user.getId(), googleScopeURLEnum.getValue());

        return contactGoogleScopeSearch.isPresent();
    }

    private List<GoogleScopeDataModel> convertScopeStringInDataModel(Set<String> scopes) {
        return scopes.stream().map(scope -> {
            final GoogleScopeDataModel data = new GoogleScopeDataModel();
            data.setName(scope);
            data.setLastUpdate(clock.getUTCZonedDateTime());
            return data;
        }).collect(Collectors.toList());
    }
}
