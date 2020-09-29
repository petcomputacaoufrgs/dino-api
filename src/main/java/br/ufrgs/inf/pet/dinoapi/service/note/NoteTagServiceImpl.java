package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.note.Note;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
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
        final User user = authService.getCurrentUser();

        final List<NoteTag> tags= noteTagRepository.findAllByUserId(user.getId());

        return new ResponseEntity<>(tags, HttpStatus.OK);
    }
}
