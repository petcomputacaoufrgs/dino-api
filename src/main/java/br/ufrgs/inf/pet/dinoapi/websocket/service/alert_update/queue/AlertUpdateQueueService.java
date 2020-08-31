package br.ufrgs.inf.pet.dinoapi.websocket.service.alert_update.queue;

import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;

public interface AlertUpdateQueueService {

    void sendUpdateMessage(Long newVersion, WebSocketDestinationsEnum pathEnum);

    void sendUpdateIdMessage(Long newId, WebSocketDestinationsEnum pathEnum);
}
