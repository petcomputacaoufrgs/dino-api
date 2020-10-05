package br.ufrgs.inf.pet.dinoapi.websocket.service.queue.alert_update;

import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;

public interface AlertUpdateQueueService {

    void sendUpdateMessage(Long newVersion, WebSocketDestinationsEnum pathEnum);

    void sendUpdateIdMessage(Long newId, WebSocketDestinationsEnum pathEnum);
}
