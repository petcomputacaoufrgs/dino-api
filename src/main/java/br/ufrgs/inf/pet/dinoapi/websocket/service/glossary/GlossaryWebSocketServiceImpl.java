package br.ufrgs.inf.pet.dinoapi.websocket.service.glossary;

import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.model.glossary.GlossaryAlertUpdateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class GlossaryWebSocketServiceImpl implements GlossaryWebSocketService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public GlossaryWebSocketServiceImpl(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void sendUpdateMessage(Long newVersion) {
        final GlossaryAlertUpdateModel model = new GlossaryAlertUpdateModel();
        model.setNewVersion(newVersion);
        simpMessagingTemplate.convertAndSend(WebSocketDestinationsEnum.ALERT_GLOSSARY_UPDATE.getValue(), model);
    }
}
