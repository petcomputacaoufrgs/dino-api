package br.ufrgs.inf.pet.dinoapi.controller.user_app_settings;

import br.ufrgs.inf.pet.dinoapi.model.user_app_settings.UserAppSettingsRequest;
import org.springframework.http.ResponseEntity;

public interface UserAppSettingsController {

    /**
     * Busca as configurações de app do usuário logado.
     * @return Configurações deste usuário ou código de erro.
     */
    ResponseEntity<?> getUserAppSettings();

    /**
     * Salva as configurações do usuário.
     * @return Código de sucesso ou erro.
     */
    ResponseEntity<?> saveUserAppSettings(UserAppSettingsRequest userAppSettingsRequest);

    /**
     * Retorna a versão da configuração do usuário logado.
     * Versão da configuração ou erro.
     * @return
     */
    ResponseEntity<?> getUserAppSettingsVersion();
}
