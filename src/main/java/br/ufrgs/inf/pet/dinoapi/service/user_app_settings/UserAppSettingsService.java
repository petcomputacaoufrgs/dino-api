package br.ufrgs.inf.pet.dinoapi.service.user_app_settings;

import br.ufrgs.inf.pet.dinoapi.model.user_app_settings.UserAppSettingsModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserAppSettingsService {

    /**
     * Busca as configurações do usuário logado atual
     *
     * @return Entidade com as configurações do usuário salvas e o status da requisição
     *
     * @author joao.silva
     */
    ResponseEntity<?> getUserAppSettings();

    /**
     * Salva os configurações vindas da Model
     *
     * @param userAppSettingsModel Model com os dados para serem salvos
     * @return Entidade com status de sucesso ou erro e versão atualizada
     */
    ResponseEntity<?> saveUserAppSettings(UserAppSettingsModel userAppSettingsModel);

    /**
     * Retorna a versão da configuração do usuário logado.
     * Versão da configuração ou erro.
     * @return
     */
    ResponseEntity<?> getUserAppSettingsVersion();

}
