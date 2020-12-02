package br.ufrgs.inf.pet.dinoapi.websocket.service.topic.alert_update;

import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;

public interface AlertUpdateTopicService {
    void sendUpdateMessage(Long newVersion, WebSocketDestinationsEnum pathEnum);

    void sendUpdateMessage(Long newVersion, Long id, WebSocketDestinationsEnum pathEnum);
}
