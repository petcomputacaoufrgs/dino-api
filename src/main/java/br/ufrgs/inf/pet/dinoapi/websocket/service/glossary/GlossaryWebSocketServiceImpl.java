package br.ufrgs.inf.pet.dinoapi.websocket.service.glossary;

import br.ufrgs.inf.pet.dinoapi.websocket.constants.WebSocketDestinations;
import br.ufrgs.inf.pet.dinoapi.websocket.model.glossary.GlossaryWebSocketAlertUpdateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class GlossaryWebSocketServiceImpl implements GlossaryWebSocketService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendGlossaryUpdateMessage(Long newVersion) {
        final GlossaryWebSocketAlertUpdateModel model = new GlossaryWebSocketAlertUpdateModel();
        model.setNewVersion(newVersion);
        simpMessagingTemplate.convertAndSend(WebSocketDestinations.ALERT_GLOSSARY_UPDATE, model);
    }
}
