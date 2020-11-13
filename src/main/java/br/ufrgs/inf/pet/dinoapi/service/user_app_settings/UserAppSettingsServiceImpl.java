package br.ufrgs.inf.pet.dinoapi.service.user_app_settings;

import br.ufrgs.inf.pet.dinoapi.constants.AppSettingsConstants;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.entity.user.UserAppSettings;
import br.ufrgs.inf.pet.dinoapi.enumerable.ColorTheme;
import br.ufrgs.inf.pet.dinoapi.model.user_app_settings.UserAppSettingsResponseAndRequestModel;
import br.ufrgs.inf.pet.dinoapi.repository.user.UserAppSettingsRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.alert_update.AlertUpdateQueueServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserAppSettingsServiceImpl implements UserAppSettingsService {

    private final AuthServiceImpl authService;

    private final UserAppSettingsRepository userAppSettingsRepository;

    private final AlertUpdateQueueServiceImpl alertUpdateQueueServiceImpl;

    @Autowired
    public UserAppSettingsServiceImpl(AuthServiceImpl authService, UserAppSettingsRepository userAppSettingsRepository, AlertUpdateQueueServiceImpl alertUpdateQueueServiceImpl) {
        this.authService = authService;
        this.userAppSettingsRepository = userAppSettingsRepository;
        this.alertUpdateQueueServiceImpl = alertUpdateQueueServiceImpl;
    }

    @Override
    public ResponseEntity<?> getUserAppSettings() {

        final User user = authService.getCurrentUser();

        if (user == null) {
            return new ResponseEntity<>(AppSettingsConstants.INVALID_USER, HttpStatus.BAD_REQUEST);
        }

        final UserAppSettings userAppSettings = user.getUserAppSettings();

        final UserAppSettingsResponseAndRequestModel model = new UserAppSettingsResponseAndRequestModel();
        model.setColorTheme(userAppSettings.getColorTheme());
        model.setLanguage(userAppSettings.getLanguage());

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> saveUserAppSettings(UserAppSettingsResponseAndRequestModel userAppSettingsModel) {
        if (userAppSettingsModel == null) {
            return new ResponseEntity<>(AppSettingsConstants.NULL_REQUEST_BODY, HttpStatus.BAD_REQUEST);
        }

        final Integer newColorTheme = userAppSettingsModel.getColorTheme();

        final boolean isInvalidColorTheme = !this.isValidColorTheme(newColorTheme);

        if (isInvalidColorTheme) {
            return new ResponseEntity<>(AppSettingsConstants.INVALID_COLOR_THEME, HttpStatus.BAD_REQUEST);
        }

        final User user = authService.getCurrentUser();

        if (user == null) {
            return new ResponseEntity<>(AppSettingsConstants.INVALID_USER, HttpStatus.BAD_REQUEST);
        }

        UserAppSettings userAppSettings = user.getUserAppSettings();

        Boolean changed = false;

        if (userAppSettings == null) {
            userAppSettings = new UserAppSettings(user);
            changed = true;
        }

        final String newLanguage = userAppSettingsModel.getLanguage();
        final String currentLanguage = userAppSettings.getLanguage();

        if (currentLanguage != newLanguage) {
            userAppSettings.setLanguage(newLanguage);
            changed = true;
        }

        final Integer currentColorTheme = userAppSettings.getColorTheme();

        if (newColorTheme != currentColorTheme) {
            userAppSettings.setColorTheme(newColorTheme);
            changed = true;
        }

        if (changed) {
            userAppSettings.updateVersion();

            userAppSettings = userAppSettingsRepository.save(userAppSettings);

            alertUpdateQueueServiceImpl.sendUpdateMessage(userAppSettings.getVersion(), WebSocketDestinationsEnum.ALERT_APP_SETTINGS_UPDATE);
        }

        return new ResponseEntity<>(userAppSettings.getVersion(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getUserAppSettingsVersion() {
        final User user = authService.getCurrentUser();

        if (user == null) {
            return new ResponseEntity<>(AppSettingsConstants.INVALID_USER, HttpStatus.BAD_REQUEST);
        }

        final UserAppSettings userAppSettings = user.getUserAppSettings();

        if (userAppSettings != null) {
            return new ResponseEntity<>(userAppSettings.getVersion(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(0, HttpStatus.OK);
        }
    }

    private boolean isValidColorTheme(Integer colorTheme) {
        ColorTheme[] colorThemes = ColorTheme.values();

        for (ColorTheme theme : colorThemes)
            if (theme.getValue() == colorTheme)
                return true;
        return false;
    }
}
