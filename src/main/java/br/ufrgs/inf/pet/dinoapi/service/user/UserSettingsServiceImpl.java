package br.ufrgs.inf.pet.dinoapi.service.user;

import br.ufrgs.inf.pet.dinoapi.constants.UserSettingsConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.entity.user.UserSettings;
import br.ufrgs.inf.pet.dinoapi.enumerable.*;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.user.UserSettingsDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.treatment.TreatmentRepository;
import br.ufrgs.inf.pet.dinoapi.repository.user.UserSettingsRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.async.AsyncGoogleContactService;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.SynchronizableQueueMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserSettingsServiceImpl extends SynchronizableServiceImpl<UserSettings, Long, UserSettingsDataModel, UserSettingsRepository> {
    private final TreatmentRepository treatmentRepository;
    private final AsyncGoogleContactService asyncGoogleContactService;

    @Autowired
    public UserSettingsServiceImpl(UserSettingsRepository repository, AuthServiceImpl authService,
                                   SynchronizableQueueMessageService<Long, UserSettingsDataModel> synchronizableQueueMessageService,
                                   ClockServiceImpl clockService, LogAPIErrorServiceImpl logAPIErrorService,
                                   TreatmentRepository treatmentRepository, AsyncGoogleContactService asyncGoogleContactService) {
        super(repository, authService, clockService, synchronizableQueueMessageService, logAPIErrorService);
        this.treatmentRepository = treatmentRepository;
        this.asyncGoogleContactService = asyncGoogleContactService;
    }

    @Override
    public UserSettingsDataModel convertEntityToModel(UserSettings entity) {
        final UserSettingsDataModel model = new UserSettingsDataModel();
        model.setLanguage(entity.getLanguage());
        model.setColorTheme(entity.getColorTheme());
        model.setFontSize(entity.getFontSize());
        model.setIncludeEssentialContact(entity.getIncludeEssentialContact());
        model.setDeclineGoogleContacts(entity.getDeclineGoogleContacts());
        model.setFirstSettingsDone(entity.getFirstSettingsDone());
        model.setParentsAreaPassword(entity.getParentsAreaPassword());

        if (entity.getTreatment() != null) {
            model.setTreatmentId(entity.getTreatment().getId());
        }

        return model;
    }

    @Override
    public UserSettings convertModelToEntity(UserSettingsDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }

        final UserSettings userSettings = new UserSettings();
        this.validSettings(model);

        if (model.getTreatmentId() != null) {
            final Optional<Treatment> treatmentSearch = treatmentRepository.findById(model.getTreatmentId());

            treatmentSearch.ifPresent(userSettings::setTreatment);
        }

        userSettings.setLanguage(model.getLanguage());
        userSettings.setColorTheme(model.getColorTheme());
        userSettings.setFontSize(model.getFontSize());
        userSettings.setUser(auth.getUser());
        userSettings.setParentsAreaPassword(model.getParentsAreaPassword());
        userSettings.setFirstSettingsDone(model.getFirstSettingsDone());
        modelToEntity(userSettings, model, auth);
        return userSettings;
    }

    @Override
    public void updateEntity(UserSettings entity, UserSettingsDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }

        this.validSettings(model);

        if (model.getTreatmentId() != null) {
            if (entity.getTreatment() == null || !entity.getTreatment().getId().equals(model.getTreatmentId())) {
                final Optional<Treatment> treatment =
                        treatmentRepository.findById(model.getTreatmentId());

                treatment.ifPresent(entity::setTreatment);
            }
        } else {
            entity.setTreatment(null);
        }

        entity.setColorTheme(model.getColorTheme());
        entity.setFontSize(model.getFontSize());
        entity.setLanguage(model.getLanguage());
        entity.setIncludeEssentialContact(model.getIncludeEssentialContact());
        modelToEntity(entity, model, auth);
    }

    private void modelToEntity(UserSettings entity, UserSettingsDataModel model, Auth auth) {
        entity.setFirstSettingsDone(model.getFirstSettingsDone());

        final String permission = auth.getUser().getPermission();
        final boolean hasUserPermission = permission.equals(PermissionEnum.USER.getValue());
        if(hasUserPermission) {
            final boolean acceptedGoogleContacts = entity.getDeclineGoogleContacts() && !model.getDeclineGoogleContacts();
            entity.setShouldSyncGoogleContacts(acceptedGoogleContacts);
            entity.setDeclineGoogleContacts(model.getDeclineGoogleContacts());
            entity.setIncludeEssentialContact(model.getIncludeEssentialContact());
        } else {
            entity.setDeclineGoogleContacts(true);
        }
    }

    @Override
    public Optional<UserSettings> findEntityByIdThatUserCanRead(Long id, Auth auth) throws AuthNullException {
        return this.findByIdAndUserId(id, auth);
    }

    @Override
    public Optional<UserSettings> findEntityByIdThatUserCanEdit(Long id, Auth auth) throws AuthNullException {
        return this.findByIdAndUserId(id, auth);
    }

    private Optional<UserSettings> findByIdAndUserId(Long id, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }

        return this.repository.findByIdAndUserId(id, auth.getUser().getId());
    }

    @Override
    public List<UserSettings> findEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }

        return this.repository.findAllByUserId(auth.getUser().getId());
    }

    @Override
    public List<UserSettings> findEntitiesByIdThatUserCanEdit(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }

        return this.repository.findAllByIdsAndUserId(ids, auth.getUser().getId());
    }

    @Override
    public List<UserSettings> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }

        return this.repository.findAllByUserIdExcludingIds(auth.getUser().getId(), ids);
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.USER_SETTINGS;
    }

    @Override
    protected void afterDataUpdated(UserSettings entity, Auth auth) {
        if (entity.shouldSyncGoogleContacts()) {
            final User user = auth.getUser();
            user.setUserAppSettings(entity);
            entity.setShouldSyncGoogleContacts(false);
            this.repository.save(entity);
            asyncGoogleContactService.updateUserGoogleContacts(user);
        }
    }

    public UserSettings saveOnDatabase(UserSettings userSettings) {
        return this.repository.save(userSettings);
    }

    public UserSettingsDataModel createUserSettingsDataModel(UserSettings userSettings) {
        return this.completeConvertEntityToModel(userSettings);
    }

    public List<UserSettings> findAllByTreatment(Treatment treatment) {
        return this.repository.findAllByTreatmentId(treatment.getId());
    }

    public void saveAllDirectly(List<UserSettings> userSettings) {
        this.repository.saveAll(userSettings);
    }

    public boolean saveContactsOnGoogleAPI(User user) {
        final UserSettings settings = user.getUserAppSettings();
        return !settings.getDeclineGoogleContacts();
    }

    private void validSettings(UserSettingsDataModel model) throws ConvertModelToEntityException {
        if (this.isInvalidIntEnum(model.getColorTheme(), ColorThemeEnum.values())) {
            throw new ConvertModelToEntityException(UserSettingsConstants.INVALID_COLOR_THEME);
        }

        if (this.isInvalidIntEnum(model.getFontSize(), FontSizeEnum.values())) {
            throw new ConvertModelToEntityException(UserSettingsConstants.INVALID_FONT_SIZE);
        }

        if (this.isInvalidIntEnum(model.getLanguage(), LanguageEnum.values())) {
            throw new ConvertModelToEntityException(UserSettingsConstants.INVALID_LANGUAGE);
        }
    }

    private <T extends IntEnumInterface> boolean isInvalidIntEnum(Integer selected, T[] enumerables) {
        if (selected == null) {
            return false;
        }

        return Arrays.stream(enumerables).noneMatch(enumerable -> selected.equals(enumerable.getValue()));
    }
}
