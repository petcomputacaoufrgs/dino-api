package br.ufrgs.inf.pet.dinoapi.websocket.service.alert_update.queue;

import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.model.alert_update.AlertUpdateModel;
import br.ufrgs.inf.pet.dinoapi.websocket.service.alert_update.topic.AlertUpdateTopicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AlertUpdateQueueServiceImpl extends AlertUpdateTopicServiceImpl implements AlertUpdateQueueService {

    private final AuthServiceImpl authService;

    @Autowired
    public AlertUpdateQueueServiceImpl(SimpMessagingTemplate simpMessagingTemplate, AuthServiceImpl authService) {
        super(simpMessagingTemplate);
        this.authService = authService;
    }

    @Override
    public void sendUpdateMessage(Long newVersion, WebSocketDestinationsEnum pathEnum) {
        final AlertUpdateModel model = new AlertUpdateModel();
        model.setNewVersion(newVersion);
        final UserDetails principal = authService.getPrincipal();
        this.simpMessagingTemplate.convertAndSendToUser(principal.getUsername(), pathEnum.getValue(), model); //email / canal / model -> message
    }
}
