package br.ufrgs.inf.pet.dinoapi.websocket.service.glossary;

import br.ufrgs.inf.pet.dinoapi.websocket.model.glossary.GlossaryWebSocketUpdateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class GlossaryWebSocketServiceImpl implements GlossaryWebSocketService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private final String WS_MESSAGE_TRANSFER_DESTINATION = "/topic/glossary";

    @Override
    public void sendGlossaryUpdateMessage(Long newVersion) {
        GlossaryWebSocketUpdateModel model = new GlossaryWebSocketUpdateModel();
        model.setNewVersion(newVersion);
        simpMessagingTemplate.convertAndSend(WS_MESSAGE_TRANSFER_DESTINATION, model);
    }
}
