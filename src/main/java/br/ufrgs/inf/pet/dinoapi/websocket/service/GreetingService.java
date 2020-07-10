package br.ufrgs.inf.pet.dinoapi.websocket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GreetingService {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private static final String WS_MESSAGE_TRANSFER_DESTINATION = "/topic/greetings";
    private List<String> userNames = new ArrayList<>();

    public void sendMessages() {
        final ObjectMapper mapper = new ObjectMapper();
        for (String userName : userNames) {
            String json = null;
            try {
                json = mapper.writeValueAsString("Hello " + userName + " at " + new Date().toString());
            } catch (JsonProcessingException e) {
                e.printStackTrace(); /*TODO: Fazer Log do Erro*/
            }

            simpMessagingTemplate.convertAndSend(WS_MESSAGE_TRANSFER_DESTINATION, json);
        }
    }

    public void addUserName(String username) throws InterruptedException {
        userNames.add(username);
        sendMessages();
    }
}
