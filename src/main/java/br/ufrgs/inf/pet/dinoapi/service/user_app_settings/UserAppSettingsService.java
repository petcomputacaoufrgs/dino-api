package br.ufrgs.inf.pet.dinoapi.service.user_app_settings;

import br.ufrgs.inf.pet.dinoapi.model.user_app_settings.UserAppSettingsResponseAndRequestModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserAppSettingsService {

    ResponseEntity<?> getUserAppSettings();

    ResponseEntity<?> saveUserAppSettings(UserAppSettingsResponseAndRequestModel userAppSettingsModel);

    ResponseEntity<?> getUserAppSettingsVersion();

}
