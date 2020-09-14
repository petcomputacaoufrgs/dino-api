package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.note.NoteVersion;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.repository.note.NoteVersionRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.alert_update.queue.AlertUpdateQueueServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

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
        NoteVersion noteVersion = this.getOrCreateWithoutSaveNoteVersion();

        noteVersion.updateVersion();
        noteVersion.setLastUpdate(new Date());
        noteVersion = noteVersionRepository.save(noteVersion);

        alertUpdateQueueServiceImpl.sendUpdateMessage(noteVersion.getVersion(), WebSocketDestinationsEnum.ALERT_NOTE_UPDATE);

        return noteVersion.getVersion();
    }

    @Override
    public Long updateColumnVersion() {
        NoteVersion noteVersion = this.getOrCreateWithoutSaveNoteVersion();

        noteVersion.updateColumnVersion();
        noteVersion.setLastColumnUpdate(new Date());
        noteVersion = noteVersionRepository.save(noteVersion);

        alertUpdateQueueServiceImpl.sendUpdateMessage(noteVersion.getColumnVersion(), WebSocketDestinationsEnum.ALERT_NOTE_COLUMN_UPDATE);

        return noteVersion.getColumnVersion();
    }

    private NoteVersion getNoteVersion() {
        final User user = authService.getCurrentAuth().getUser();

        Optional<NoteVersion> noteVersionSearch = noteVersionRepository.findByUserId(user.getId());

        NoteVersion noteVersion = noteVersionSearch.orElseGet(() -> noteVersionRepository.save(new NoteVersion(user)));

        return noteVersion;
    }

    private NoteVersion getOrCreateWithoutSaveNoteVersion() {
        final User user = authService.getCurrentAuth().getUser();

        Optional<NoteVersion> noteVersionSearch = noteVersionRepository.findByUserId(user.getId());

        NoteVersion noteVersion = noteVersionSearch.orElseGet(() -> new NoteVersion(user));

        return noteVersion;
    }
}
