package br.ufrgs.inf.pet.dinoapi.controller.user_app_settings;

import br.ufrgs.inf.pet.dinoapi.model.user_app_settings.UserAppSettingsModel;
import org.springframework.http.ResponseEntity;

public interface UserAppSettingsController {

    /**
     * Busca as configurações de app do usuário logado.
     * @return Configurações deste usuário ou código de erro.
     */
    ResponseEntity<?> getUserAppSettings();

    /**
     * Salva as configurações do usuário.
     * @return Código de sucesso ou erro e versão atualizada.
     */
    ResponseEntity<?> saveUserAppSettings(UserAppSettingsModel userAppSettingsModel);

    /**
     * Retorna a versão da configuração do usuário logado.
     * Se não houver nada salvo o valor default é 0
     *
     * @return Versão da configuração ou erro.
     */
    ResponseEntity<?> getUserAppSettingsVersion();
}
