package br.ufrgs.inf.pet.dinoapi.websocket.service.topic.alert_update;

import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.model.alert_update.AlertUpdateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

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

        public void sendUpdateMessage(Long newVersion, Long id, WebSocketDestinationsEnum pathEnum) {
            final AlertUpdateModel model = new AlertUpdateModel();
            model.setNewVersion(newVersion);
            model.setNewId(id);
            this.simpMessagingTemplate.convertAndSend(pathEnum.getValue(), model);
        }
}
