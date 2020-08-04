package br.ufrgs.inf.pet.dinoapi.websocket.service.user;

import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.model.note.NoteAlertUpdateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserWebSocketServiceImpl implements  UserWebSocketService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public UserWebSocketServiceImpl(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void sendUpdateMessage(User user) {
        final NoteAlertUpdateModel model = new NoteAlertUpdateModel();
        model.setNewVersion(user.getVersion());
        simpMessagingTemplate.convertAndSendToUser(user.getEmail(), WebSocketDestinationsEnum.ALERT_USER_UPDATE.getValue(), model);
    }
}
