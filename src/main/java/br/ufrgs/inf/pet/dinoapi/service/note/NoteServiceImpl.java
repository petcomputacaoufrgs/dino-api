package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.Note;
import br.ufrgs.inf.pet.dinoapi.entity.NoteTag;
import br.ufrgs.inf.pet.dinoapi.entity.NoteVersion;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.model.notes.*;
import br.ufrgs.inf.pet.dinoapi.repository.NoteRepository;
import br.ufrgs.inf.pet.dinoapi.repository.NoteTagRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
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

    private final NoteRepository noteRepository;

    private final NoteTagRepository noteTagRepository;

    private final NoteVersionServiceImpl noteVersionService;

    private final AuthServiceImpl authService;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository, NoteTagRepository noteTagRepository, NoteVersionServiceImpl noteVersionService, AuthServiceImpl authService) {
        this.noteRepository = noteRepository;
        this.noteTagRepository = noteTagRepository;
        this.noteVersionService = noteVersionService;
        this.authService = authService;
    }

    @Override
    public ResponseEntity<List<NoteResponseModel>> getUserNotes() {
        final User user = authService.getCurrentAuth().getUser();

        final List<Note> notes = user.getNotes();

        final List<NoteResponseModel> model = notes.stream().map(NoteResponseModel::new).collect(Collectors.toList());

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> saveNewNote(NoteSaveRequestRequestModel model) {

        if (model.getQuestion().isBlank()) {
            return new ResponseEntity<>("Pergunta deve conter um ou mais caracteres excluindo espaços em branco.", HttpStatus.BAD_REQUEST);
        }

        final User user = authService.getCurrentAuth().getUser();

        final List<Note> noteSearch = noteRepository.findByQuestionAndUserId(model.getQuestion(), user.getId());

        if (noteSearch.size() > 0) {
            return new ResponseEntity<>("Pergunta já cadastrada", HttpStatus.BAD_REQUEST);
        }

        final List<NoteTag> tags = this.createNewTags(model);

        final Date date = new Date(model.getLastUpdate());

        final Optional<Integer> maxOrderSearch = noteRepository.findMaxOrderByUserId(user.getId());

        Integer order = 0;

        if (maxOrderSearch.isPresent()) {
            order = maxOrderSearch.get() + 1;
        }

        final Note note = new Note(date, order, model.getQuestion(), tags, user);

        Long newNoteVersion = noteVersionService.updateVersion();

        final Note savedNote = noteRepository.save(note);

        final NoteSaveResponseModel response = new NoteSaveResponseModel(newNoteVersion, savedNote.getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> deleteAll(List<NoteDeleteRequestModel> models) {
        final User user = authService.getCurrentAuth().getUser();

        final List<Long> validIds = models.stream()
                .filter(model -> model.getId() != null)
                .map(model -> model.getId()).collect(Collectors.toList());

        int deletedItems = 0;

        if (validIds.size() > 0) {
            deletedItems = noteRepository.deleteAllByIdAndUserId(validIds, user.getId());

            if (deletedItems > 0) {
                Long newNoteVersion = noteVersionService.updateVersion();

                return new ResponseEntity<>(newNoteVersion, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(user.getNoteVersion().getVersion(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> deleteNote(NoteDeleteRequestModel model) {

        final User user = authService.getCurrentAuth().getUser();

        if (model == null || model.getId() == null) {
            return new ResponseEntity<>(user.getNoteVersion().getVersion(), HttpStatus.OK);
        }

        final int deletedItems = noteRepository.deleteByIdAndUserId(model.getId(), user.getId());


        if (deletedItems > 0) {
            Long newNoteVersion = noteVersionService.updateVersion();

            return new ResponseEntity<>(newNoteVersion, HttpStatus.OK);
        }

        return new ResponseEntity<>(user.getNoteVersion().getVersion(), HttpStatus.OK);
    }

    public ResponseEntity<Long> saveAll(List<NoteSaveRequestRequestModel> models) {
        final User user = authService.getCurrentAuth().getUser();

        final List<Note> newNotes = this.createNewNotes(models, user, new Date());

        noteRepository.saveAll(newNotes);

        Long newNoteVersion = noteVersionService.updateVersion();

        return new ResponseEntity<>(newNoteVersion, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> updateAll(List<NoteUpdateRequestModel> models) {
        final User user = authService.getCurrentAuth().getUser();

        final List<Long> idsToUpdate = models.stream()
                .filter(m -> m.getId() != null)
                .map(m -> m.getId())
                .collect(Collectors.toList());

        List<Note> notes = new ArrayList<>();

        if (idsToUpdate.size() > 0) {
           notes = noteRepository.findAllByIdAndUserId(idsToUpdate, user.getId());
        }

        notes.forEach(note -> {
            Optional<NoteUpdateRequestModel> modelSearch = models.stream().filter(m -> m.getId() == note.getId()).findFirst();

            if (modelSearch.isPresent()) {
                NoteUpdateRequestModel model = modelSearch.get();

                note.setAnswered(model.getAnswered());

                note.setAnswer(model.getAnswer());

                note.setQuestion(model.getQuestion());

                note.setLastUpdate(new Date());

                this.updateTags(note, model);
            }
        });

        final List<Note> newNotes = this.createNewNotes(models, user, new Date());

        notes.addAll(newNotes);

        noteRepository.saveAll(notes);

        Long newNoteVersion = noteVersionService.updateVersion();

        return new ResponseEntity<>(newNoteVersion, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateNotesOrder(List<NoteOrderRequestModel> models) {
        final User user = authService.getCurrentAuth().getUser();

        models = models.stream().filter(model -> model.getId() != null && model.getOrder() != null).collect(Collectors.toList());

        final List<Long> ids = models.stream()
                .map(m -> m.getId())
                .collect(Collectors.toList());

        final List<Integer> orders = models.stream()
                .sorted(Comparator.comparing(NoteOrderRequestModel::getId))
                .map(m -> m.getOrder())
                .collect(Collectors.toList());

        final Set<Integer> uniqueOrders = new HashSet<>(orders);

        if (orders.size() != uniqueOrders.size()) {
            return new ResponseEntity<>("Não podem haver itens com ordens iguais.", HttpStatus.OK);
        }

        final List<Note> notes = noteRepository.findAllByIdOrderByIdAsc(ids, user.getId());

        if (notes.size() != ids.size()) {
            return new ResponseEntity<>(
                    "Nem todos os itens foram encontrados, encontramos " + notes.size() + " item de um total de  " + ids.size() + " buscados.",
                    HttpStatus.OK);
        }

        IntStream.range(0, notes.size())
                .forEach(i ->
                        notes.get(i).setOrder(orders.get(i))
                );


        noteRepository.saveAll(notes);

        Long newNoteVersion = noteVersionService.updateVersion();

        return new ResponseEntity<>(newNoteVersion, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateNoteQuestion(NoteQuestionRequestModel model) {

        if (model.getQuestion().isBlank()) {
            return new ResponseEntity<>("Pergunta deve conter um ou mais caracteres excluindo espaços em branco.", HttpStatus.BAD_REQUEST);
        }

        final User user = authService.getCurrentAuth().getUser();

        final Optional<Note> noteSearch = noteRepository.findOneByIdAndUserId(model.getId(), user.getId());

        if (noteSearch.isEmpty()) {
            return new ResponseEntity<>("Anotação não encontrada", HttpStatus.BAD_REQUEST);
        }

        final Note note = noteSearch.get();

        Boolean changed = false;

        final Boolean questionChanged = !model.getQuestion().equalsIgnoreCase(note.getQuestion());

        if (questionChanged) {
            final List<Note> notesSearch = noteRepository.findByQuestionAndUserId(model.getQuestion(), user.getId());

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

        final Date date = new Date(model.getLastUpdate());

        if (changed) {
            note.setLastUpdate(date);

            noteRepository.save(note);

            Long newNoteVersion = noteVersionService.updateVersion();

            return new ResponseEntity<>(newNoteVersion, HttpStatus.OK);
        }

        return new ResponseEntity<>(user.getNoteVersion().getVersion(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateNoteAnswer(NoteAnswerRequestModel model) {

        final User user = authService.getCurrentAuth().getUser();

        final Optional<Note> noteSearch = noteRepository.findOneByIdAndUserId(model.getId(), user.getId());

        if (noteSearch.isEmpty()) {
            return new ResponseEntity<>("Anotação não encontrada", HttpStatus.BAD_REQUEST);
        }

        final Note note = noteSearch.get();

        final NoteVersion version = user.getNoteVersion();

        final Boolean answerChanged = note.getAnswer() != null ? !note.getAnswer().equalsIgnoreCase(model.getAnswer()) : true;

        if (answerChanged) {
            note.setAnswer(model.getAnswer());

            final Boolean noteNotAnswered = !note.getAnswered();

            if (noteNotAnswered) {
                note.setAnswered(true);
            }

            noteRepository.save(note);

            Long newNoteVersion = noteVersionService.updateVersion();

            return new ResponseEntity<>(newNoteVersion, HttpStatus.OK);
        }

        return new ResponseEntity<>(version.getVersion(), HttpStatus.OK);
    }

    private List<Note> createNewNotes(List<? extends NoteQuestionRequestModel> models, User user, Date date) {
        final List<Note> savedNotes = new ArrayList<>();

        final List<NoteQuestionRequestModel> notesToCreate = models.stream()
                .filter(m -> m.getId() == null)
                .collect(Collectors.toList());

        final List<String> questions = notesToCreate.stream().map(n -> n.getQuestion()).collect(Collectors.toList());

        List<String> questionsAlreadySaved = new ArrayList<>();

        if (questions.size() > 0) {
            questionsAlreadySaved = noteRepository.findAllQuestionsByQuestionsAndUserId(questions, user.getId());
        }

        final Optional<Integer> maxOrderSearch = noteRepository.findMaxOrderByUserId(user.getId());

        Integer order = 0;

        if (maxOrderSearch.isPresent()) {
            order = maxOrderSearch.get() + 1;
        }

        for(NoteQuestionRequestModel model : notesToCreate) {
            final Boolean alreadySaved = questionsAlreadySaved.stream().anyMatch(q -> q.equalsIgnoreCase(model.getQuestion()));

            if (alreadySaved) {
                break;
            }

            if (model.getQuestion().isBlank()) {
                break;
            }

            List<NoteTag> tags = createNewTags(model);

            Note note = new Note();
            note.setAnswered(model.getAnswered());
            note.setLastUpdate(date);
            note.setOrder(order++);
            note.setQuestion(model.getQuestion());
            note.setTags(tags);
            note.setUser(user);

            savedNotes.add(note);
        }

        return savedNotes;
    }

    private List<NoteTag> createNewTags(NoteQuestionRequestModel model) {
        List<NoteTag> tags = new ArrayList<>();

        if (model.getTagNames() != null && model.getTagNames().size() > 0) {
            tags = noteTagRepository.findAllByName(model.getTagNames());

            final List<String> tagNames = tags.stream().map(tag -> tag.getName()).collect(Collectors.toList());

            List<NoteTag> newTags = model.getTagNames().stream()
                    .filter(name -> !tagNames.contains(name))
                    .map(name -> {
                        NoteTag tag = new NoteTag(name);

                        return tag;
                    })
                    .collect(Collectors.toList());

            if (newTags.size() > 0) {
                newTags = Lists.newArrayList((noteTagRepository.saveAll(newTags)));
                tags.addAll(newTags);
            }
        }

        return tags;
    }

    private boolean updateTags(Note note, NoteQuestionRequestModel model) {
        boolean changed = false;

        final List<NoteTag> removedTags = note.getTags().stream()
                .filter(tag -> model.getTagNames().stream().allMatch(tagName -> !tagName.equalsIgnoreCase(tag.getName())))
                .collect(Collectors.toList());

        if (removedTags.size() > 0) {
            note.deleteTags(removedTags);
            changed = true;
        }

        final List<String> newTagNames = model.getTagNames().stream()
                .filter(tagName -> note.getTags().stream().allMatch(tag -> !tag.getName().equalsIgnoreCase(tagName)))
                .collect(Collectors.toList());

        if (newTagNames.size() > 0) {
            final List<NoteTag> newTagsAlreadyExists = noteTagRepository.findAllByName(newTagNames);

            note.addTags(newTagsAlreadyExists);

            final List<NoteTag> newTagsNotExists = newTagNames.stream()
                    .filter(tagName -> newTagsAlreadyExists.stream().allMatch(tag -> !tag.getName().equalsIgnoreCase(tagName)))
                    .map(tagName -> {
                        NoteTag tag = new NoteTag();
                        tag.setName(tagName);

                        return tag;
                    })
                    .collect(Collectors.toList());

            final List<NoteTag> newCreatedTags = Lists.newArrayList(noteTagRepository.saveAll(newTagsNotExists));

            note.addTags(newCreatedTags);

            changed = true;
        }

        return changed;
    }
}
