package br.ufrgs.inf.pet.dinoapi.controller.user_app_settings;

import br.ufrgs.inf.pet.dinoapi.model.user_app_settings.UserAppSettingsResponseAndRequestModel;
import org.springframework.http.ResponseEntity;

public interface UserAppSettingsController {

    /**
     * Busca as configurações de app do usuário logado
     *
     * @return Model do tipo {@link UserAppSettingsResponseAndRequestModel} ou mensagem de erro
     */
    ResponseEntity<?> getUserAppSettings();

    /**
     * Salva as configurações do usuário
     *
     * @return Nova versão das configurações ou mensagem de erro
     */
    ResponseEntity<?> saveUserAppSettings(UserAppSettingsResponseAndRequestModel userAppSettingsModel);

    /**
     * Retorna a versão da configuração do usuário logado, se não houver nada salvo o valor padrão é 0
     *
     * @return Versão da configuração ou mensagem de erro.
     */
    ResponseEntity<?> getUserAppSettingsVersion();
}
