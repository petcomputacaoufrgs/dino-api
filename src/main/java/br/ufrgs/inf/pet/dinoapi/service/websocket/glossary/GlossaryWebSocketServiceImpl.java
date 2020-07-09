package br.ufrgs.inf.pet.dinoapi.service.websocket.glossary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class GlossaryWebSocketServiceImpl implements GlossaryWebSocketService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void sendMessages() {
        ObjectMapper mapper = new ObjectMapper();
        for (String userName : userNames) {
            String json = null;
            try {
                json = mapper.writeValueAsString("Hello " + userName + " at " + new Date().toString());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            simpMessagingTemplate.convertAndSend(WS_MESSAGE_TRANSFER_DESTINATION, json);
        }
    }

    @Override
    public void sendGlossaryUpdateMessage() {

    }
}
