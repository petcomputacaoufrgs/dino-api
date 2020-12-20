package br.ufrgs.inf.pet.dinoapi.service.user;

import br.ufrgs.inf.pet.dinoapi.constants.UserSettingsConstants;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.entity.user.UserSettings;
import br.ufrgs.inf.pet.dinoapi.enumerable.ColorTheme;
import br.ufrgs.inf.pet.dinoapi.enumerable.FontSize;
import br.ufrgs.inf.pet.dinoapi.exception.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.user.UserSettingsDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.user.UserSettingsRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.treatment.TreatmentServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.synchronizable.SynchronizableQueueMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserSettingsServiceImpl extends SynchronizableServiceImpl<UserSettings, Long, Integer, UserSettingsDataModel, UserSettingsRepository> {

    private final TreatmentServiceImpl treatmentService;

    @Autowired
    public UserSettingsServiceImpl(UserSettingsRepository repository, AuthServiceImpl authService, TreatmentServiceImpl treatmentService,
                                   SynchronizableQueueMessageServiceImpl<Long, Integer, UserSettingsDataModel> synchronizableQueueMessageService) {
        super(repository, authService, synchronizableQueueMessageService);
        this.treatmentService = treatmentService;
    }

    @Override
    public UserSettingsDataModel convertEntityToModel(UserSettings entity) {
        final UserSettingsDataModel model = new UserSettingsDataModel();
        model.setLanguage(entity.getLanguage());
        model.setColorTheme(entity.getColorTheme());
        model.setFontSize(entity.getFontSize());
        model.setIncludeEssentialContact(entity.getIncludeEssentialContact());
        model.setSyncGoogleContacts(entity.getSyncGoogleContacts());
        model.setDeclineGoogleContacts(entity.getDeclineGoogleContacts());
        model.setFirstSettingsDone(entity.getFirstSettingsDone());
        model.setSettingsStep(entity.getSettingsStep());

        if (entity.getTreatment() != null) {
            model.setTreatmentId(entity.getTreatment().getId());
        }

        return model;
    }

    @Override
    public UserSettings convertModelToEntity(UserSettingsDataModel model, User user) throws ConvertModelToEntityException {
        final UserSettings userSettings = new UserSettings();

        this.validSettings(model);

        if (model.getTreatmentId() != null) {
            final Optional<Treatment> treatment = treatmentService.getEntityByIdAndUser(model.getTreatmentId(), user);

            if (treatment.isPresent()) {
                userSettings.setTreatment(treatment.get());
            }
        }

        userSettings.setLanguage(model.getLanguage());
        userSettings.setColorTheme(model.getColorTheme());
        userSettings.setFontSize(model.getFontSize());
        userSettings.setIncludeEssentialContact(model.getIncludeEssentialContact());
        userSettings.setSyncGoogleContacts(model.getSyncGoogleContacts());
        userSettings.setDeclineGoogleContacts(model.getDeclineGoogleContacts());
        userSettings.setUser(user);
        userSettings.setFirstSettingsDone(model.getFirstSettingsDone());
        userSettings.setSettingsStep(model.getSettingsStep());

        return userSettings;
    }

    @Override
    public void updateEntity(UserSettings entity, UserSettingsDataModel model, User user) throws ConvertModelToEntityException {
        this.validSettings(model);

        if (model.getTreatmentId() != null) {
            if (entity.getTreatment() == null || !entity.getTreatment().getId().equals(model.getTreatmentId())) {
                final Optional<Treatment> treatment = treatmentService.getEntityByIdAndUser(model.getTreatmentId(), user);

                if (treatment.isPresent()) {
                    entity.setTreatment(treatment.get());
                }
            }
        }

        entity.setColorTheme(model.getColorTheme());
        entity.setFontSize(model.getFontSize());
        entity.setLanguage(model.getLanguage());
        entity.setSyncGoogleContacts(model.getSyncGoogleContacts());
        entity.setIncludeEssentialContact(model.getIncludeEssentialContact());
        entity.setDeclineGoogleContacts(model.getDeclineGoogleContacts());
        entity.setFirstSettingsDone(model.getFirstSettingsDone());
        entity.setSettingsStep(model.getSettingsStep());
    }

    @Override
    public Optional<UserSettings> getEntityByIdAndUser(Long id, User user) {
        return this.repository.findByIdAndUserId(id, user.getId());
    }

    @Override
    public List<UserSettings> getEntitiesByUserId(User user) {
        return this.repository.findAllByUserId(user.getId());
    }

    @Override
    public List<UserSettings> getEntitiesByIdsAndUserId(List<Long> ids, User user) {
        return this.repository.findAllByIdsAndUserId(ids, user.getId());
    }

    @Override
    public List<UserSettings> getEntitiesByUserIdExceptIds(User user, List<Long> ids) {
        return this.repository.findAllByUserIdExceptIds(user.getId(), ids);
    }

    @Override
    public WebSocketDestinationsEnum getUpdateWebSocketDestination() {
        return WebSocketDestinationsEnum.USER_SETTINGS_UPDATE;
    }

    @Override
    public WebSocketDestinationsEnum getDeleteWebSocketDestination() {
        return WebSocketDestinationsEnum.USER_SETTINGS_DELETE;
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
        ColorTheme[] colorThemes = ColorTheme.values();

        for (ColorTheme theme : colorThemes)
            if (theme.getValue() == colorTheme)
                return true;
        return false;
    }

    private boolean isValidFontSize(Integer fontSize) {
        FontSize[] fontSizes = FontSize.values();

        for (FontSize size : fontSizes)
            if (size.getValue() == fontSize)
                return true;
        return false;
    }
}
