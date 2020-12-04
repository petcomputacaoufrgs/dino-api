package br.ufrgs.inf.pet.dinoapi.websocket.service.topic;

import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.GenericMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class GenericTopicMessageServiceImpl extends GenericMessageService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public GenericTopicMessageServiceImpl(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void sendObjectMessage(Object object, WebSocketDestinationsEnum pathEnum) {
        this.simpMessagingTemplate.convertAndSend(pathEnum.getValue(), object);
    }
}
