package br.ufrgs.inf.pet.dinoapi.service.user;

import br.ufrgs.inf.pet.dinoapi.constants.UserSettingsConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;
import br.ufrgs.inf.pet.dinoapi.entity.user.UserSettings;
import br.ufrgs.inf.pet.dinoapi.enumerable.ColorThemeEnum;
import br.ufrgs.inf.pet.dinoapi.enumerable.FontSizeEnum;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.user.UserSettingsDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.user.UserSettingsRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.OAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.treatment.TreatmentServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.SynchronizableQueueMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserSettingsServiceImpl extends SynchronizableServiceImpl<UserSettings, Long, Integer, UserSettingsDataModel, UserSettingsRepository> {

    private final TreatmentServiceImpl treatmentService;

    @Autowired
    public UserSettingsServiceImpl(UserSettingsRepository repository, OAuthServiceImpl authService, TreatmentServiceImpl treatmentService,
                                   SynchronizableQueueMessageServiceImpl<Long, Integer, UserSettingsDataModel> synchronizableQueueMessageService,
                                   ClockServiceImpl clockService, LogAPIErrorServiceImpl logAPIErrorService) {
        super(repository, authService, clockService, synchronizableQueueMessageService, logAPIErrorService);
        this.treatmentService = treatmentService;
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
        model.setStep(entity.getStep());

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
            final Optional<Treatment> treatmentSearch = treatmentService.findEntityByIdThatUserCanRead(model.getTreatmentId(), auth);

            treatmentSearch.ifPresent(userSettings::setTreatment);
        }

        userSettings.setLanguage(model.getLanguage());
        userSettings.setColorTheme(model.getColorTheme());
        userSettings.setFontSize(model.getFontSize());
        userSettings.setIncludeEssentialContact(model.getIncludeEssentialContact());
        userSettings.setDeclineGoogleContacts(model.getDeclineGoogleContacts());
        userSettings.setUser(auth.getUser());
        userSettings.setFirstSettingsDone(model.getFirstSettingsDone());
        userSettings.setStep(model.getStep());

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
                        treatmentService.findEntityByIdThatUserCanRead(model.getTreatmentId(), auth);

                treatment.ifPresent(entity::setTreatment);
            }
        }

        entity.setColorTheme(model.getColorTheme());
        entity.setFontSize(model.getFontSize());
        entity.setLanguage(model.getLanguage());
        entity.setIncludeEssentialContact(model.getIncludeEssentialContact());
        entity.setDeclineGoogleContacts(model.getDeclineGoogleContacts());
        entity.setFirstSettingsDone(model.getFirstSettingsDone());
        entity.setStep(model.getStep());
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

    public UserSettings saveOnDatabase(UserSettings userSettings) {
        return this.repository.save(userSettings);
    }

    public UserSettingsDataModel createUserSettingsDataModel(UserSettings userSettings) {
        return this.completeConvertEntityToModel(userSettings);
    }

    private void validSettings(UserSettingsDataModel model) throws ConvertModelToEntityException {
        if (!this.isValidColorTheme(model.getColorTheme())) {
            throw new ConvertModelToEntityException(UserSettingsConstants.INVALID_COLOR_THEME);
        }

        if (!this.isValidFontSize(model.getFontSize())) {
            throw new ConvertModelToEntityException(UserSettingsConstants.INVALID_FONT_SIZE);
        }
    }

    private boolean isValidColorTheme(Integer colorTheme) {
        if (colorTheme == null) {
            return true;
        }

        ColorThemeEnum[] colorThemeEnums = ColorThemeEnum.values();

        for (ColorThemeEnum theme : colorThemeEnums)
            if (theme.getValue() == colorTheme)
                return true;
        return false;
    }

    private boolean isValidFontSize(Integer fontSize) {
        if (fontSize == null) {
            return true;
        }

        FontSizeEnum[] fontSizeEnums = FontSizeEnum.values();

        for (FontSizeEnum size : fontSizeEnums)
            if (size.getValue() == fontSize)
                return true;
        return false;
    }
}
