package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.Note;
import br.ufrgs.inf.pet.dinoapi.entity.NoteTag;
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
    public ResponseEntity<?> saveNote(NoteSaveRequestModel model) {

        if (model.getQuestion().isBlank()) {
            return new ResponseEntity<>("Pergunta deve conter um ou mais caracteres excluindo espaços em branco.", HttpStatus.BAD_REQUEST);
        }

        final User user = authService.getCurrentAuth().getUser();

        Note note;

        if (model.getId() == null) {
            final List<Note> noteSearch = noteRepository.findByQuestionAndUserId(model.getQuestion(), user.getId());

            if (noteSearch.size() > 0) {
                note = noteSearch.get(0);
            } else {
                final Optional<Integer> maxOrderSearch = noteRepository.findMaxOrderByUserId(user.getId());

                Integer order = 0;

                if (maxOrderSearch.isPresent()) {
                    order = maxOrderSearch.get() + 1;
                }

                note = new Note(user, order);
            }
        } else {
            final Optional<Note> noteSearch = noteRepository.findById(model.getId());

            if (noteSearch.isPresent()) {
                note = noteSearch.get();
            } else {
                return new ResponseEntity<>("Note not found", HttpStatus.NOT_FOUND);
            }
        }

        final List<NoteTag> tags = this.createNotSavedTags(model);

        note.setLastUpdate(new Date(model.getLastUpdate()));
        note.setQuestion(model.getQuestion());
        note.setAnswer(model.getAnswer());
        note.setTags(tags);

        Long newNoteVersion = noteVersionService.updateVersion();

        final Note savedNote = noteRepository.save(note);

        final NoteSaveResponseModel response = new NoteSaveResponseModel(newNoteVersion, savedNote);

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

    @Override
    public ResponseEntity<Long> updateAll(List<NoteSaveRequestModel> models) {
        final User user = authService.getCurrentAuth().getUser();
        final List<Note> notes = new ArrayList<>();

        final List<NoteSaveRequestModel> changedNotes = new ArrayList<>();
        final List<NoteSaveRequestModel> newNotes = new ArrayList<>();
        final List<Long> idsToUpdate = new ArrayList<>();
        final List<Integer> orders = new ArrayList<>();
        final List<String> questions = new ArrayList<>();

        models.forEach(model -> {
            if (model.getId() != null) {
                changedNotes.add(model);
                idsToUpdate.add(model.getId());
            } else {
                newNotes.add(model);
            }
            orders.add(model.getOrder());
            questions.add(model.getQuestion());
        });

        final List<Note> databaseNotes = noteRepository.findAllByIdAndUserId(idsToUpdate, user.getId());

        notes.addAll(this.updateSavedNotes(changedNotes, databaseNotes));

        notes.addAll(this.createNewNotes(newNotes, user));

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
            return new ResponseEntity<>("Não podem haver itens com ordens iguais.", HttpStatus.BAD_REQUEST);
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

    private List<Note> updateSavedNotes(List<NoteSaveRequestModel> models, List<Note> notes) {
        final List<Note> updatedNotes = new ArrayList<>();
        final Date now = new Date();

        for (NoteSaveRequestModel model : models)  {
            Optional<Note> noteSearch = notes.stream().filter(n -> n.getId() == model.getId()).findFirst();

            if (noteSearch.isPresent()) {
                Note note = noteSearch.get();

                note.setAnswer(model.getAnswer());

                note.setQuestion(model.getQuestion());

                note.setLastUpdate(now);

                note.setOrder(model.getOrder());

                this.updateTags(note, model);

                updatedNotes.add(note);
            }
        }

        return updatedNotes;
    }

    private List<Note> createNewNotes(List<NoteSaveRequestModel> models, User user) {
        final List<Note> newNotes = new ArrayList<>();
        final Date now = new Date();

        for(NoteSaveRequestModel model : models) {
            List<NoteTag> tags = createNotSavedTags(model);

            Note note = new Note();
            note.setLastUpdate(now);
            note.setOrder(model.getOrder());
            note.setQuestion(model.getQuestion());
            note.setTags(tags);
            note.setUser(user);

            newNotes.add(note);
        }

        return newNotes;
    }

    private List<NoteTag> createNotSavedTags(NoteSaveRequestModel model) {
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

    private boolean updateTags(Note note, NoteSaveRequestModel model) {
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
