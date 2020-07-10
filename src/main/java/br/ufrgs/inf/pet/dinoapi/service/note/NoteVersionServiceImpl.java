package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.Note;
import br.ufrgs.inf.pet.dinoapi.entity.NoteVersion;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.repository.NoteVersionRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.dino.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.service.note.NoteWebSocketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NoteVersionServiceImpl implements NoteVersionService {

    private final Long DEFAULT_VERSION = 0l;

    @Autowired
    AuthServiceImpl authService;

    @Autowired
    NoteVersionRepository noteVersionRepository;

    @Autowired
    NoteWebSocketServiceImpl noteWebSocketService;

    @Override
    public ResponseEntity<Long> getVersion() {
        final NoteVersion noteVersion = this.getNoteVersion();

        return new ResponseEntity<>(noteVersion.getVersion(), HttpStatus.OK);
    }

    @Override
    public Long updateVersion() {
        NoteVersion noteVersion = this.getNoteVersion();

        if (noteVersion.getVersion() != this.DEFAULT_VERSION) {
            noteVersion.setVersion(noteVersion.getVersion() + 1);
            noteVersion.setLastUpdate(new Date());
            noteVersion = noteVersionRepository.save(noteVersion);

            noteWebSocketService.sendNoteUpdateMessage(noteVersion.getVersion());
        }

        return noteVersion.getVersion();
    }

    private NoteVersion getNoteVersion() {
        final User user = authService.getCurrentAuth().getUser();

        NoteVersion noteVersion = user.getNoteVersion();

        if (noteVersion == null) {
            noteVersion = new NoteVersion();
            noteVersion.setVersion(this.DEFAULT_VERSION);
            noteVersion.setUser(user);
            noteVersion.setLastUpdate(new Date());

            noteVersion = noteVersionRepository.save(noteVersion);
        }

        return noteVersion;
    }
}
