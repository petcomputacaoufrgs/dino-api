package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.notes.NoteVersion;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.repository.notes.NoteVersionRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.alert_update.queue.AlertUpdateQueueServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NoteVersionServiceImpl implements NoteVersionService {

    private final AuthServiceImpl authService;

    private final NoteVersionRepository noteVersionRepository;

    private final AlertUpdateQueueServiceImpl alertUpdateQueueServiceImpl;

    @Autowired
    public NoteVersionServiceImpl(AuthServiceImpl authService, NoteVersionRepository noteVersionRepository, AlertUpdateQueueServiceImpl alertUpdateQueueServiceImpl) {
        this.authService = authService;
        this.noteVersionRepository = noteVersionRepository;
        this.alertUpdateQueueServiceImpl = alertUpdateQueueServiceImpl;
    }

    @Override
    public ResponseEntity<Long> getVersion() {
        final NoteVersion noteVersion = this.getNoteVersion();

        return new ResponseEntity<>(noteVersion.getVersion(), HttpStatus.OK);
    }

    @Override
    public Long updateVersion() {
        NoteVersion noteVersion = this.getNoteVersion();

        noteVersion.updateVersion();
        noteVersion.setLastUpdate(new Date());
        noteVersion = noteVersionRepository.save(noteVersion);

        alertUpdateQueueServiceImpl.sendUpdateMessage(noteVersion.getVersion(), WebSocketDestinationsEnum.ALERT_NOTE_UPDATE);

        return noteVersion.getVersion();
    }

    private NoteVersion getNoteVersion() {
        final User user = authService.getCurrentAuth().getUser();

        NoteVersion noteVersion = user.getNoteVersion();

        if (noteVersion == null) {
            noteVersion = new NoteVersion(user);

            noteVersion = noteVersionRepository.save(noteVersion);
        }

        return noteVersion;
    }
}
