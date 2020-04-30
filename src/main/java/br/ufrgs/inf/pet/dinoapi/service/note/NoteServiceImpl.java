package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.Note;
import br.ufrgs.inf.pet.dinoapi.entity.NoteTag;
import br.ufrgs.inf.pet.dinoapi.entity.NoteVersion;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.model.notes.*;
import br.ufrgs.inf.pet.dinoapi.repository.NoteRepository;
import br.ufrgs.inf.pet.dinoapi.repository.NoteTagRepository;
import br.ufrgs.inf.pet.dinoapi.repository.NoteVersionRepository;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    NoteTagRepository noteTagRepository;

    @Autowired
    NoteVersionRepository noteVersionRepository;

    @Autowired
    UserServiceImpl userService;

    @Override
    public ResponseEntity<List<NoteModel>> getUserNotes() {
        User user = userService.getCurrentUser();

        List<Note> notes = user.getNotes();

        List<NoteModel> model = notes.stream().map(note -> new NoteModel(note)).collect(Collectors.toList());

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<NoteSaveResponseModel> saveNewNote(NoteSaveModel model) {
        User user = userService.getCurrentUser();

        List<NoteTag> tags = new ArrayList<>();

        if (model.getTagIdList().size() > 0) {
            tags = noteTagRepository.findAllById(model.getTagIdList());
        }

        List<NoteTag> userTags = noteTagRepository.findAllByUserId(user.getId());

        List<String> userTagNames = userTags.stream().map(tag -> tag.getName()).collect(Collectors.toList());

        List<NoteTag> newTags = new ArrayList<>();

        if (model.getNewTags() != null && model.getNewTags().size() > 0) {
            newTags = model.getNewTags().stream()
                    .filter(nt -> !userTagNames.contains(nt))
                    .map(nt -> {
                        NoteTag tag = new NoteTag();
                        tag.setName(nt);

                        return tag;
                    }).collect(Collectors.toList());

            newTags = Lists.newArrayList((noteTagRepository.saveAll(newTags)));

            tags.addAll(newTags);
        }

        Note note = new Note();
        note.setAnswered(false);
        note.setLastUpdateDay(model.getLastUpdateDay());
        note.setLastUpdateMonth(model.getLastUpdateMonth());
        note.setLastUpdateYear(model.getLastUpdateYear());
        note.setOrder(model.getOrder());
        note.setQuestion(model.getQuestion());
        note.setTags(tags);
        note.setUser(user);

        NoteVersion version = user.getNoteVersion();

        if (version == null) {
            version = new NoteVersion();
            version.setVersion(0L);
            version.setUser(user);
        } else {
            version.setVersion(version.getVersion() + 1);
        }

        version.setLastUpdate(new Date());

        noteRepository.save(note);
        noteVersionRepository.save(version);

        NoteSaveResponseModel response = new NoteSaveResponseModel(version.getVersion(), newTags);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateNotesOrder(List<NoteOrderModel> models) {
        List<Long> ids = models.stream()
                .map(m -> m.getId())
                .collect(Collectors.toList());

        List<Integer> orders = models.stream()
                .sorted(Comparator.comparing(NoteOrderModel::getId))
                .map(m -> m.getOrder())
                .collect(Collectors.toList());

        List<Note> notes = noteRepository.findAllByIdOrderByIdAsc(ids);

        if (notes.size() != ids.size()) {
            return new ResponseEntity<>(
                    "Nem todos os itens foram encontrados, encontramos " + notes.size() + " item de um total de  " + ids.size() + " buscados.",
                    HttpStatus.OK);
        }

        IntStream.range(0, notes.size())
                .forEach(i ->
                        notes.get(i).setOrder(orders.get(i))
                );

        User user = userService.getCurrentUser();

        NoteVersion version = user.getNoteVersion();

        version.setVersion(version.getVersion() + 1);

        noteRepository.saveAll(notes);
        noteVersionRepository.save(version);

        return new ResponseEntity<>(version.getVersion(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateNoteQuestion(NoteQuestionModel model) {

        Optional<Note> noteSearch = noteRepository.findById(model.getId());

        if (noteSearch.isEmpty()) {
            return new ResponseEntity<>("Anotação não encontrada", HttpStatus.BAD_REQUEST);
        }

        List<Long> tagIds = model.getTagList().stream().map(t -> t.getId()).collect(Collectors.toList());

        List<NoteTag> tags = noteTagRepository.findAllById(tagIds);

        List<NoteTagModel> newTagsModel = model.getTagList().stream().filter(t -> t.getId() == null).collect(Collectors.toList());

        List<NoteTag> newTags = newTagsModel.stream().map(t -> {
            NoteTag tag = new NoteTag();
            tag.setName(t.getName());

            return tag;
        }).collect(Collectors.toList());

        noteTagRepository.saveAll(newTags);

        tags.addAll(newTags);

        User user = userService.getCurrentUser();

        NoteVersion version = user.getNoteVersion();

        version.setVersion(version.getVersion() + 1);

        noteVersionRepository.save(version);

        Note note = noteSearch.get();

        note.setQuestion(model.getQuestion());

        note.setTags(tags);

        noteRepository.save(note);

        return new ResponseEntity<>(version.getVersion(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateNoteAnswer(NoteAnswerModel model) {
        Optional<Note> noteSearch = noteRepository.findById(model.getId());

        if (noteSearch.isEmpty()) {
            return new ResponseEntity<>("Anotação não encontrada", HttpStatus.BAD_REQUEST);
        }

        User user = userService.getCurrentUser();

        NoteVersion version = user.getNoteVersion();

        version.setVersion(version.getVersion() + 1);

        noteVersionRepository.save(version);

        Note note = noteSearch.get();

        note.setAnswer(model.getAnswer());

        noteRepository.save(note);

        return new ResponseEntity<>(version.getVersion(), HttpStatus.OK);
    }
}
