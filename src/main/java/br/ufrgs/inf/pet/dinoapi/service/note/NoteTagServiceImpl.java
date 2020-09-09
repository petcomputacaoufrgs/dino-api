package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.note.Note;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteTag;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.repository.note.NoteTagRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NoteTagServiceImpl implements NoteTagService {

    private final NoteTagRepository noteTagRepository;

    private final AuthServiceImpl authService;

    @Autowired
    public NoteTagServiceImpl(NoteTagRepository noteTagRepository, AuthServiceImpl authService) {
        this.noteTagRepository = noteTagRepository;
        this.authService = authService;
    }

    @Override
    public ResponseEntity<List<NoteTag>> getTags() {
        final User user = authService.getCurrentAuth().getUser();

        final List<Note> userNotes = new ArrayList<>();

        user.getNoteColumns().forEach(nc -> {
            userNotes.addAll(nc.getNotes());
        });

        final Set<Long> noteIds = userNotes.stream().map(n -> n.getId()).collect(Collectors.toSet());

        final List<NoteTag> notes = noteTagRepository.findAllByNotes(noteIds);

        return new ResponseEntity<>(notes, HttpStatus.OK);
    }
}
