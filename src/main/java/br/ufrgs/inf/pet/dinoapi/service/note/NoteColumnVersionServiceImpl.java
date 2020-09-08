package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumnVersion;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteVersion;
import br.ufrgs.inf.pet.dinoapi.repository.note.NoteColumnVersionRepository;
import br.ufrgs.inf.pet.dinoapi.repository.note.NoteVersionRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.alert_update.queue.AlertUpdateQueueServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NoteColumnVersionServiceImpl implements NoteColumnVersionService {

    private final AuthServiceImpl authService;

    private final NoteColumnVersionRepository noteColumnVersionRepository;

    private final AlertUpdateQueueServiceImpl alertUpdateQueueServiceImpl;

    @Autowired
    public NoteColumnVersionServiceImpl(AuthServiceImpl authService, NoteColumnVersionRepository noteColumnVersionRepository, AlertUpdateQueueServiceImpl alertUpdateQueueServiceImpl) {
        this.authService = authService;
        this.noteColumnVersionRepository = noteColumnVersionRepository;
        this.alertUpdateQueueServiceImpl = alertUpdateQueueServiceImpl;
    }

    @Override
    public ResponseEntity<Long> getVersion() {
        final NoteColumnVersion noteColumnVersion = this.getNoteColumnVersion();

        return new ResponseEntity<>(noteColumnVersion.getVersion(), HttpStatus.OK);
    }

    @Override
    public Long updateVersion() {
        NoteColumnVersion noteColumnVersion = this.getNoteColumnVersion();

        noteColumnVersion.updateVersion();
        noteColumnVersion.setLastUpdate(new Date());
        noteColumnVersion = noteColumnVersionRepository.save(noteColumnVersion);

        alertUpdateQueueServiceImpl.sendUpdateMessage(noteColumnVersion.getVersion(), WebSocketDestinationsEnum.ALERT_NOTE_COLUMN_UPDATE);

        return noteColumnVersion.getVersion();
    }

    private NoteColumnVersion getNoteColumnVersion() {
        final User user = authService.getCurrentAuth().getUser();

        NoteColumnVersion noteColumnVersion = user.getNoteColumnVersion();

        if (noteColumnVersion == null) {
            noteColumnVersion = new NoteColumnVersion(user);

            noteColumnVersion = noteColumnVersionRepository.save(noteColumnVersion);
        }

        return noteColumnVersion;
    }
}
