package br.ufrgs.inf.pet.dinoapi.websocket.service.alert_update.topic;

import br.ufrgs.inf.pet.dinoapi.websocket.model.alert_update.AlertUpdateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;

@Service
public class AlertUpdateTopicServiceImpl implements AlertUpdateTopicService {

        protected final SimpMessagingTemplate simpMessagingTemplate;

        @Autowired
        public AlertUpdateTopicServiceImpl(SimpMessagingTemplate simpMessagingTemplate) {
            this.simpMessagingTemplate = simpMessagingTemplate;
        }

        public void sendUpdateMessage(Long newVersion, WebSocketDestinationsEnum pathEnum) {
            final AlertUpdateModel model = new AlertUpdateModel();
            model.setNewVersion(newVersion);
            this.simpMessagingTemplate.convertAndSend(pathEnum.getValue(), model);
        }

}
