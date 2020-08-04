package br.ufrgs.inf.pet.dinoapi.websocket.service.user_app_settings;

import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.model.user_app_settings.UserAppSettingsAlertUpdateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserAppSettingsWebSocketServiceImpl implements UserAppSettingsWebSocketService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final AuthServiceImpl authService;

    @Autowired
    public UserAppSettingsWebSocketServiceImpl(SimpMessagingTemplate simpMessagingTemplate, AuthServiceImpl authService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.authService = authService;
    }


    @Override
    public void sendUpdateMessage(Long newVersion) {
        final UserAppSettingsAlertUpdateModel model = new UserAppSettingsAlertUpdateModel();
        model.setNewVersion(newVersion);
        final UserDetails principal = authService.getPrincipal();
        simpMessagingTemplate.convertAndSendToUser(principal.getUsername(), WebSocketDestinationsEnum.ALERT_APP_SETTINGS_UPDATE.getValue(), model);
    }
}
