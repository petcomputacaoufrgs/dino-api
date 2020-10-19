package br.ufrgs.inf.pet.dinoapi.websocket.service.queue.generic;

import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface GenericQueueMessageService {
    void sendObjectMessage(Object object, WebSocketDestinationsEnum pathEnum) throws JsonProcessingException;

}
