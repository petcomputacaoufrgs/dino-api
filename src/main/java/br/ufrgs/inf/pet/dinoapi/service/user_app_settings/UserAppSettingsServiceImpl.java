package br.ufrgs.inf.pet.dinoapi.service.user_app_settings;

import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.entity.UserAppSettings;
import br.ufrgs.inf.pet.dinoapi.model.user_app_settings.UserAppSettingsModel;
import br.ufrgs.inf.pet.dinoapi.repository.UserAppSettingsRepository;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserAppSettingsServiceImpl implements UserAppSettingsService {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserAppSettingsRepository userAppSettingsRepository;

    @Override
    public ResponseEntity<?> getUserAppSettings() {

        User userDB = userService.getCurrentUser();

        if (userDB == null) {
            return new ResponseEntity<>("Usuário inválido", HttpStatus.BAD_REQUEST);
        }

        UserAppSettings userAppSettings = userDB.getUserAppSettings();

        UserAppSettingsModel model = new UserAppSettingsModel();

        model.setLanguage(userAppSettings.getLanguage());

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> saveUserAppSettings(UserAppSettingsModel userAppSettingsModel) {
        if (userAppSettingsModel == null) {
            return new ResponseEntity<>("Requisição nula", HttpStatus.BAD_REQUEST);
        }

        User userDB = userService.getCurrentUser();

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

        String newLanguage = userAppSettingsModel.getLanguage();
        String currentLanguage = userAppSettings.getLanguage();

        if (currentLanguage != newLanguage) {
            userAppSettings.setLanguage(newLanguage);
            changed = true;
        }

        if (changed) {
            Long currentVersion = userAppSettings.getVersion();

            Long newVersion = currentVersion + 1;

            userAppSettings.setVersion(newVersion);

            userAppSettingsRepository.save(userAppSettings);
        }

        return new ResponseEntity<>(userAppSettings.getVersion(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getUserAppSettingsVersion() {
        User userDB = userService.getCurrentUser();

        if (userDB == null) {
            return new ResponseEntity<>("Usuário inválido", HttpStatus.BAD_REQUEST);
        }

        UserAppSettings userAppSettings = userDB.getUserAppSettings();

        if (userAppSettings != null) {
            return new ResponseEntity<>(userAppSettings.getVersion(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(0, HttpStatus.OK);
        }
    }
}
