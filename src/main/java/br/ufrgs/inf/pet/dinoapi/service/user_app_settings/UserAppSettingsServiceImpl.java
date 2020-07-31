package br.ufrgs.inf.pet.dinoapi.service.user_app_settings;

import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.entity.UserAppSettings;
import br.ufrgs.inf.pet.dinoapi.model.user_app_settings.UserAppSettingsModel;
import br.ufrgs.inf.pet.dinoapi.repository.UserAppSettingsRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.dino.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserAppSettingsServiceImpl implements UserAppSettingsService {

    @Autowired
    AuthServiceImpl authService;

    @Autowired
    UserAppSettingsRepository userAppSettingsRepository;

    @Override
    public ResponseEntity<?> getUserAppSettings() {

        final User userDB = authService.getCurrentAuth().getUser();

        if (userDB == null) {
            return new ResponseEntity<>("Usuário inválido", HttpStatus.BAD_REQUEST);
        }

        final UserAppSettings userAppSettings = userDB.getUserAppSettings();

        final UserAppSettingsModel model = new UserAppSettingsModel();

        model.setLanguage(userAppSettings.getLanguage());

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> saveUserAppSettings(UserAppSettingsModel userAppSettingsModel) {
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
            userAppSettings = new UserAppSettings();
            userAppSettings.setUser(userDB);
            userAppSettings.setVersion(0L);
            changed = true;
        }

        final String newLanguage = userAppSettingsModel.getLanguage();
        final String currentLanguage = userAppSettings.getLanguage();

        if (currentLanguage != newLanguage) {
            userAppSettings.setLanguage(newLanguage);
            changed = true;
        }

        if (changed) {
            final Long currentVersion = userAppSettings.getVersion();

            final Long newVersion = currentVersion + 1;

            userAppSettings.setVersion(newVersion);

            userAppSettingsRepository.save(userAppSettings);
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
