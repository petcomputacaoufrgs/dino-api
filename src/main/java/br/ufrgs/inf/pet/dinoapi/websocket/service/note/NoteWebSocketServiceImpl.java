package br.ufrgs.inf.pet.dinoapi.websocket.service.note;

import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.model.note.NoteAlertUpdateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class NoteWebSocketServiceImpl implements NoteWebSocketService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final AuthServiceImpl authService;

    @Autowired
    public NoteWebSocketServiceImpl(SimpMessagingTemplate simpMessagingTemplate, AuthServiceImpl authService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.authService = authService;
    }

    @Override
    public void sendUpdateMessage(Long newVersion) {
        final NoteAlertUpdateModel model = new NoteAlertUpdateModel();
        model.setNewVersion(newVersion);
        final UserDetails principal = authService.getPrincipal();
        simpMessagingTemplate.convertAndSendToUser(principal.getUsername(), WebSocketDestinationsEnum.ALERT_NOTE_UPDATE.getValue(), model);
    }
}
