package br.ufrgs.inf.pet.dinoapi.controller.user_app_settings;

import br.ufrgs.inf.pet.dinoapi.model.user_app_settings.UserAppSettingsResponseAndRequestModel;
import br.ufrgs.inf.pet.dinoapi.service.user_app_settings.UserAppSettingsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user_app_settings/")
public class UserAppSettingsControllerImpl implements  UserAppSettingsController {

    private final UserAppSettingsServiceImpl userAppSettingsService;

    @Autowired
    public UserAppSettingsControllerImpl(UserAppSettingsServiceImpl userAppSettingsService) {
        this.userAppSettingsService = userAppSettingsService;
    }

    @Override
    @GetMapping
    public ResponseEntity<?> getUserAppSettings() {
        return this.userAppSettingsService.getUserAppSettings();
    }

    @Override
    @PostMapping
    public ResponseEntity<?> saveUserAppSettings(@Valid @RequestBody UserAppSettingsResponseAndRequestModel userAppSettingsModel) {
        return this.userAppSettingsService.saveUserAppSettings(userAppSettingsModel);
    }

    @Override
    @GetMapping("version/")
    public ResponseEntity<?> getUserAppSettingsVersion() {
        return this.userAppSettingsService.getUserAppSettingsVersion();
    }
}
