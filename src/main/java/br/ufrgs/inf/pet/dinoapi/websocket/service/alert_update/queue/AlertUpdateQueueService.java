package br.ufrgs.inf.pet.dinoapi.websocket.service.alert_update.queue;

import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.alert_update.topic.AlertUpdateTopicService;

public interface AlertUpdateQueueService extends AlertUpdateTopicService {

    void sendUpdateIdMessage(Long newId, WebSocketDestinationsEnum pathEnum);
}
