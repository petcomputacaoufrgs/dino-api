package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.NoteTag;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.repository.NoteTagRepository;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NoteTagServiceImpl implements NoteTagService {

    @Autowired
    NoteTagRepository noteTagRepository;

    @Autowired
    UserServiceImpl userService;

    @Override
    public ResponseEntity<List<NoteTag>> getTags() {
        User user = userService.getCurrentUser();

        Set<Long> noteIds = user.getNotes().stream().map(n -> n.getId()).collect(Collectors.toSet());

        List<NoteTag> notes = noteTagRepository.findAllByNotes(noteIds);

        return new ResponseEntity<>(notes, HttpStatus.OK);
    }
}
