package br.ufrgs.inf.pet.dinoapi.websocket.service.glossary;

import br.ufrgs.inf.pet.dinoapi.websocket.constants.WebSocketDestinations;
import br.ufrgs.inf.pet.dinoapi.websocket.model.glossary.GlossaryAlertUpdateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class GlossaryWebSocketServiceImpl implements GlossaryWebSocketService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendUpdateMessage(Long newVersion) {
        final GlossaryAlertUpdateModel model = new GlossaryAlertUpdateModel();
        model.setNewVersion(newVersion);
        simpMessagingTemplate.convertAndSend(WebSocketDestinations.ALERT_GLOSSARY_UPDATE, model);
    }
}
