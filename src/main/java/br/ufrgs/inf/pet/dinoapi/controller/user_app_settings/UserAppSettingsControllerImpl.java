package br.ufrgs.inf.pet.dinoapi.controller.user_app_settings;

import br.ufrgs.inf.pet.dinoapi.model.user_app_settings.UserAppSettingsModel;
import br.ufrgs.inf.pet.dinoapi.service.user_app_settings.UserAppSettingsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Implementação de: {@link UserAppSettingsController}
 *
 * @author joao.silva
 */
@RestController
@RequestMapping("/user_app_settings/")
public class UserAppSettingsControllerImpl implements  UserAppSettingsController {

    @Autowired
    UserAppSettingsServiceImpl userAppSettingsService;

    @Override
    @GetMapping
    public ResponseEntity<?> getUserAppSettings() {
        return userAppSettingsService.getUserAppSettings();
    }

    @Override
    @PostMapping
    public ResponseEntity<?> saveUserAppSettings(@RequestBody UserAppSettingsModel userAppSettingsModel) {
        return userAppSettingsService.saveUserAppSettings(userAppSettingsModel);
    }

    @Override
    @GetMapping("version/")
    public ResponseEntity<?> getUserAppSettingsVersion() {
        return userAppSettingsService.getUserAppSettingsVersion();
    }
}
