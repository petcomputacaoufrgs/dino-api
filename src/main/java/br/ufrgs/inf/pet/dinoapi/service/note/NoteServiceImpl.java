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
import br.ufrgs.inf.pet.dinoapi.utils.DatetimeUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public ResponseEntity<?> saveNewNote(NoteSaveModel model) {

        if (model.getQuestion().isBlank()) {
            return new ResponseEntity<>("Pergunta deve conter um ou mais caracteres excluindo espaços em branco.", HttpStatus.BAD_REQUEST);
        }

        User user = userService.getCurrentUser();

        List<Note> noteSearch = noteRepository.findByQuestionAndUserId(model.getQuestion(), user.getId());

        if (noteSearch.size() > 0) {
            return new ResponseEntity<>("Pergunta já cadastrada", HttpStatus.BAD_REQUEST);
        }

        List<NoteTag> tags = new ArrayList<>();

        List<NoteTag> newTags = new ArrayList<>();

        if (model.getTagNames() != null && model.getTagNames().size() > 0) {
            tags = noteTagRepository.findAllByName(model.getTagNames());

            List<String> tagNames = tags.stream().map(tag -> tag.getName()).collect(Collectors.toList());

            newTags = model.getTagNames().stream()
                    .filter(name -> !tagNames.contains(name))
                    .map(name -> {
                        NoteTag tag = new NoteTag();
                        tag.setName(name);

                        return tag;
                    })
                    .collect(Collectors.toList());

            if (newTags.size() > 0) {
                newTags = Lists.newArrayList((noteTagRepository.saveAll(newTags)));
                tags.addAll(newTags);
            }

        }

        LocalDateTime date = DatetimeUtils.convertMillisecondsToLocalDatetime(model.getLastUpdate());

        Optional<Integer> maxOrderSearch = noteRepository.findMaxOrderByUserId(user.getId());

        Integer order = 0;

        if (maxOrderSearch.isPresent()) {
            order = maxOrderSearch.get() + 1;
        }

        Note note = new Note();
        note.setAnswered(false);
        note.setLastUpdate(date);
        note.setOrder(order);
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

        note = noteRepository.save(note);
        noteVersionRepository.save(version);

        NoteSaveResponseModel response = new NoteSaveResponseModel(version.getVersion(), note.getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> deleteAll(List<NoteDeleteModel> models) {
        User user = userService.getCurrentUser();

        List<Long> validIds = models.stream()
                .filter(model -> model.getId() != null)
                .map(model -> model.getId()).collect(Collectors.toList());

        int deletedItems = noteRepository.deleteAllByIdAndUserId(validIds, user.getId());

        NoteVersion version = user.getNoteVersion();

        if (deletedItems > 0) {
            version.setVersion(version.getVersion() + 1);

            noteVersionRepository.save(version);
        }

        return new ResponseEntity<>(version.getVersion(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> deleteNote(NoteDeleteModel model) {

        User user = userService.getCurrentUser();

        NoteVersion version = user.getNoteVersion();

        if (model == null || model.getId() == null) {
            return new ResponseEntity<>(version.getVersion(), HttpStatus.OK);
        }


        int deletedItems = noteRepository.deleteByIdAndUserId(model.getId(), user.getId());


        if (deletedItems > 0) {
            version.setVersion(version.getVersion() + 1);

            noteVersionRepository.save(version);
        }

        return new ResponseEntity<>(version.getVersion(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateAll(List<NoteUpdateModel> models) {
        User user = userService.getCurrentUser();

        List<Long> ids = models.stream()
                .map(m -> m.getId())
                .collect(Collectors.toList());

        List<Note> savedNotes = noteRepository.findAllByIdAndUserId(ids, user.getId());

        LocalDateTime date = LocalDateTime.now();

        savedNotes.forEach(note -> {
            Optional<NoteUpdateModel> modelSearch = models.stream().filter(m -> m.getId() == note.getId()).findFirst();

            if (modelSearch.isPresent()) {
                NoteUpdateModel model = modelSearch.get();

                note.setAnswer(model.getAnswer());

                note.setQuestion(model.getQuestion());

                note.setLastUpdate(date);

                this.updateTags(note, model);
            }
        });


        NoteVersion version = user.getNoteVersion();

        noteRepository.saveAll(savedNotes);

        version.setVersion(version.getVersion() + 1);

        noteVersionRepository.save(version);

        return new ResponseEntity<>(version.getVersion(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateNotesOrder(List<NoteOrderModel> models) {
        User user = userService.getCurrentUser();

        models = models.stream().filter(model -> model.getId() != null && model.getOrder() != null).collect(Collectors.toList());

        List<Long> ids = models.stream()
                .map(m -> m.getId())
                .collect(Collectors.toList());

        List<Integer> orders = models.stream()
                .sorted(Comparator.comparing(NoteOrderModel::getId))
                .map(m -> m.getOrder())
                .collect(Collectors.toList());

        Set<Integer> uniqueOrders = new HashSet<>(orders);

        if (orders.size() != uniqueOrders.size()) {
            return new ResponseEntity<>("Não podem haver itens com ordens iguais.", HttpStatus.OK);
        }

        List<Note> notes = noteRepository.findAllByIdOrderByIdAsc(ids, user.getId());

        if (notes.size() != ids.size()) {
            return new ResponseEntity<>(
                    "Nem todos os itens foram encontrados, encontramos " + notes.size() + " item de um total de  " + ids.size() + " buscados.",
                    HttpStatus.OK);
        }

        IntStream.range(0, notes.size())
                .forEach(i ->
                        notes.get(i).setOrder(orders.get(i))
                );

        NoteVersion version = user.getNoteVersion();

        version.setVersion(version.getVersion() + 1);

        noteRepository.saveAll(notes);
        noteVersionRepository.save(version);

        return new ResponseEntity<>(version.getVersion(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateNoteQuestion(NoteQuestionModel model) {

        if (model.getQuestion().isBlank()) {
            return new ResponseEntity<>("Pergunta deve conter um ou mais caracteres excluindo espaços em branco.", HttpStatus.BAD_REQUEST);
        }

        User user = userService.getCurrentUser();

        Optional<Note> noteSearch = noteRepository.findOneByIdAndUserId(model.getId(), user.getId());

        if (noteSearch.isEmpty()) {
            return new ResponseEntity<>("Anotação não encontrada", HttpStatus.BAD_REQUEST);
        }

        Note note = noteSearch.get();

        Boolean changed = false;

        Boolean questionChanged = ! model.getQuestion().equalsIgnoreCase(note.getQuestion());

        if (questionChanged) {
            List<Note> notesSearch = noteRepository.findByQuestionAndUserId(model.getQuestion(), user.getId());

            if (notesSearch.size() > 0) {
                return new ResponseEntity<>("Pergunta já cadastrada", HttpStatus.BAD_REQUEST);
            }

            note.setQuestion(model.getQuestion());
            changed = true;
        }

        Boolean anyTagUpdated = this.updateTags(note, model);

        if (anyTagUpdated) {
            changed = true;
        }

        LocalDateTime date = DatetimeUtils.convertMillisecondsToLocalDatetime(model.getLastUpdate());

        NoteVersion version = user.getNoteVersion();

        if (changed) {
            note.setLastUpdate(date);

            noteRepository.save(note);

            version.setVersion(version.getVersion() + 1);

            noteVersionRepository.save(version);

        }

        return new ResponseEntity<>(version.getVersion(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateNoteAnswer(NoteAnswerModel model) {

        User user = userService.getCurrentUser();

        Optional<Note> noteSearch = noteRepository.findOneByIdAndUserId(model.getId(), user.getId());

        if (noteSearch.isEmpty()) {
            return new ResponseEntity<>("Anotação não encontrada", HttpStatus.BAD_REQUEST);
        }

        Note note = noteSearch.get();

        NoteVersion version = user.getNoteVersion();

        Boolean answerChanged = note.getAnswer() != null ? !note.getAnswer().equalsIgnoreCase(model.getAnswer()) : true;

        if (answerChanged) {
            note.setAnswer(model.getAnswer());

            Boolean noteNotAnswered = !note.getAnswered();

            if (noteNotAnswered) {
                note.setAnswered(true);
            }

            noteRepository.save(note);

            version.setVersion(version.getVersion() + 1);

            noteVersionRepository.save(version);
        }

        return new ResponseEntity<>(version.getVersion(), HttpStatus.OK);
    }

    private boolean updateTags(Note note, NoteQuestionModel model) {
        boolean changed = false;

        List<NoteTag> removedTags = note.getTags().stream()
                .filter(tag -> model.getTagNames().stream().allMatch(tagName -> !tagName.equalsIgnoreCase(tag.getName())))
                .collect(Collectors.toList());

        if (removedTags.size() > 0) {
            note.deleteTags(removedTags);
            changed = true;
        }

        List<String> newTagNames = model.getTagNames().stream()
                .filter(tagName -> note.getTags().stream().allMatch(tag -> !tag.getName().equalsIgnoreCase(tagName)))
                .collect(Collectors.toList());

        if (newTagNames.size() > 0) {
            List<NoteTag> newTagsAlreadyExists = noteTagRepository.findAllByName(newTagNames);

            note.addTags(newTagsAlreadyExists);

            List<NoteTag> newTagsNotExists = newTagNames.stream()
                    .filter(tagName -> newTagsAlreadyExists.stream().allMatch(tag -> !tag.getName().equalsIgnoreCase(tagName)))
                    .map(tagName -> {
                        NoteTag tag = new NoteTag();
                        tag.setName(tagName);

                        return tag;
                    })
                    .collect(Collectors.toList());

            List<NoteTag> newCreatedTags = Lists.newArrayList(noteTagRepository.saveAll(newTagsNotExists));

            note.addTags(newCreatedTags);

            changed = true;
        }

        return changed;
    }
}
