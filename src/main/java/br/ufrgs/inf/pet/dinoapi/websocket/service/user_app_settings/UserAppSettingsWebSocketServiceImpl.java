package br.ufrgs.inf.pet.dinoapi.websocket.service.user_app_settings;

import br.ufrgs.inf.pet.dinoapi.service.auth.dino.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.constants.WebSocketDestinations;
import br.ufrgs.inf.pet.dinoapi.websocket.model.user_app_settings.UserAppSettingsWebSocketAlertUpdateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserAppSettingsWebSocketServiceImpl implements UserAppSettingsWebSocketService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private AuthServiceImpl authService;

    @Override
    public void sendUserAppSettingsUpdateMessage(Long newVersion) {
        final UserAppSettingsWebSocketAlertUpdateModel model = new UserAppSettingsWebSocketAlertUpdateModel();
        model.setNewVersion(newVersion);
        final UserDetails principal = authService.getPrincipal();
        simpMessagingTemplate.convertAndSendToUser(principal.getUsername(), WebSocketDestinations.ALERT_APP_SETTINGS_UPDATE, model);
    }
}
