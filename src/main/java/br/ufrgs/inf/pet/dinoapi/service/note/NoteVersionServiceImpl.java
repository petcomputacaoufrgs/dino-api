package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.NoteVersion;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.repository.NoteVersionRepository;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NoteVersionServiceImpl implements NoteVersionService {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    NoteVersionRepository noteVersionRepository;

    @Override
    public ResponseEntity<Long> getVersion() {
        User user = userService.getCurrentUser();

        NoteVersion noteVersion = user.getNoteVersion();

        if (noteVersion == null) {
            noteVersion = new NoteVersion();
            noteVersion.setVersion(0L);
            noteVersion.setUser(user);
            noteVersion.setLastUpdate(new Date());

            noteVersionRepository.save(noteVersion);
        }

        return new ResponseEntity<>(noteVersion.getVersion(), HttpStatus.OK);
    }
}
