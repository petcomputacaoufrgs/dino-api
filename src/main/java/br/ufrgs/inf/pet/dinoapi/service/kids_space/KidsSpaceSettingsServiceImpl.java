package br.ufrgs.inf.pet.dinoapi.service.kids_space;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.kids_space.KidsSpaceSettings;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.kids_space.KidsSpaceSettingsModel;
import br.ufrgs.inf.pet.dinoapi.repository.kids_space.KidsSpaceSettingsRepository;
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

@Service
public class KidsSpaceSettingsServiceImpl extends SynchronizableServiceImpl<KidsSpaceSettings, Long, KidsSpaceSettingsModel, KidsSpaceSettingsRepository> {
    @Autowired
    public KidsSpaceSettingsServiceImpl(KidsSpaceSettingsRepository repository, AuthServiceImpl authService,
                                        ClockServiceImpl clock,
                                        SynchronizableQueueMessageService<Long, KidsSpaceSettingsModel> synchronizableMessageService,
                                        LogAPIErrorServiceImpl logAPIErrorService) {
        super(repository, authService, clock, synchronizableMessageService, logAPIErrorService);
    }

    @Override
    public KidsSpaceSettingsModel convertEntityToModel(KidsSpaceSettings entity) {
        final KidsSpaceSettingsModel model = new KidsSpaceSettingsModel();
        model.setFirstSettingsDone(entity.getFirstSettingsDone());
        model.setColor(entity.getColor());
        model.setHat(entity.getHat());
        return model;
    }

    @Override
    public KidsSpaceSettings convertModelToEntity(KidsSpaceSettingsModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }

        final User user = auth.getUser();
        final KidsSpaceSettings entity = new KidsSpaceSettings();
        entity.setFirstSettingsDone(model.getFirstSettingsDone());
        entity.setColor(model.getColor());
        entity.setHat(model.getHat());
        entity.setUser(user);

        return entity;
    }

    @Override
    public void updateEntity(KidsSpaceSettings entity, KidsSpaceSettingsModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        entity.setFirstSettingsDone(model.getFirstSettingsDone());
        entity.setColor(model.getColor());
        entity.setHat(model.getHat());
    }

    @Override
    public Optional<KidsSpaceSettings> findEntityByIdThatUserCanRead(Long id, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }

        final User user = auth.getUser();
        return this.repository.findByIdAndUserId(id, user.getId());
    }

    @Override
    public Optional<KidsSpaceSettings> findEntityByIdThatUserCanEdit(Long id, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }

        final User user = auth.getUser();
        return this.repository.findByIdAndUserId(id, user.getId());
    }

    @Override
    public List<KidsSpaceSettings> findEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }

        final User user = auth.getUser();
        return this.repository.findAllByUserId(user.getId());
    }

    @Override
    public List<KidsSpaceSettings> findEntitiesByIdThatUserCanEdit(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }

        final User user = auth.getUser();
        return this.repository.findAllByIdsAndUserId(ids, user.getId());
    }

    @Override
    public List<KidsSpaceSettings> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }

        final User user = auth.getUser();
        return this.repository.findAllByUserIdExcludingIds(user.getId(), ids);
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.KIDS_SPACE_SETTINGS;
    }

    public KidsSpaceSettings saveOnDatabase(KidsSpaceSettings kidsSpaceSettings) {
        return this.repository.save(kidsSpaceSettings);
    }

    public KidsSpaceSettingsModel createUserSettingsDataModel(KidsSpaceSettings kidsSpaceSettings) {
        return this.completeConvertEntityToModel(kidsSpaceSettings);
    }
}
