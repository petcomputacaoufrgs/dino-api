package br.ufrgs.inf.pet.dinoapi.service.user_app_settings;

import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.entity.UserAppSettings;
import br.ufrgs.inf.pet.dinoapi.model.user_app_settings.UserAppSettingsResponseAndRequestModel;
import br.ufrgs.inf.pet.dinoapi.repository.UserAppSettingsRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.alert_update.queue.AlertUpdateQueueServiceImpl;
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

        final User userDB = authService.getCurrentAuth().getUser();

        if (userDB == null) {
            return new ResponseEntity<>("Usuário inválido", HttpStatus.BAD_REQUEST);
        }

        final UserAppSettings userAppSettings = userDB.getUserAppSettings();

        final UserAppSettingsResponseAndRequestModel model = new UserAppSettingsResponseAndRequestModel();

        model.setLanguage(userAppSettings.getLanguage());

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> saveUserAppSettings(UserAppSettingsResponseAndRequestModel userAppSettingsModel) {
        if (userAppSettingsModel == null) {
            return new ResponseEntity<>("Requisição nula", HttpStatus.BAD_REQUEST);
        }

        final User userDB = authService.getCurrentAuth().getUser();

        if (userDB == null) {
            return new ResponseEntity<>("Usuário inválido", HttpStatus.BAD_REQUEST);
        }

        UserAppSettings userAppSettings = userDB.getUserAppSettings();

        Boolean changed = false;

        if (userAppSettings == null) {
            userAppSettings = new UserAppSettings(userDB);
            changed = true;
        }

        final String newLanguage = userAppSettingsModel.getLanguage();
        final String currentLanguage = userAppSettings.getLanguage();

        if (currentLanguage != newLanguage) {
            userAppSettings.setLanguage(newLanguage);
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
        final User userDB = authService.getCurrentAuth().getUser();

        if (userDB == null) {
            return new ResponseEntity<>("Usuário inválido", HttpStatus.BAD_REQUEST);
        }

        final UserAppSettings userAppSettings = userDB.getUserAppSettings();

        if (userAppSettings != null) {
            return new ResponseEntity<>(userAppSettings.getVersion(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(0, HttpStatus.OK);
        }
    }
}
