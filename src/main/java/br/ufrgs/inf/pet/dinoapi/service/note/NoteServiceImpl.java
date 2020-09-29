package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.note.Note;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteTag;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.model.notes.*;
import br.ufrgs.inf.pet.dinoapi.repository.note.NoteRepository;
import br.ufrgs.inf.pet.dinoapi.repository.note.NoteTagRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    private final NoteTagRepository noteTagRepository;

    private final NoteVersionServiceImpl noteVersionService;

    private final NoteColumnServiceImpl noteColumnService;

    private final AuthServiceImpl authService;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository, NoteTagRepository noteTagRepository, NoteVersionServiceImpl noteVersionService, AuthServiceImpl authService, NoteColumnServiceImpl noteColumnService) {
        this.noteRepository = noteRepository;
        this.noteTagRepository = noteTagRepository;
        this.noteVersionService = noteVersionService;
        this.authService = authService;
        this.noteColumnService = noteColumnService;
    }

    @Override
    public ResponseEntity<List<NoteResponseModel>> getUserNotes() {
        final User user = authService.getCurrentUser();

        final List<Note> notes = noteRepository.findAllByUserId(user.getId());

        final List<NoteResponseModel> model = notes.stream().map(NoteResponseModel::new).collect(Collectors.toList());

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> saveNote(NoteSaveRequestModel model) {
        if (model.getQuestion().isBlank()) {
            return new ResponseEntity<>("Pergunta deve conter um ou mais caracteres excluindo espaços em branco.", HttpStatus.BAD_REQUEST);
        }

        final User user = authService.getCurrentUser();

        Note note;

        if (model.getId() == null) {
            final Optional<Note> noteSearch = noteRepository.findByQuestionAndUserId(model.getQuestion(), user.getId());

            if (noteSearch.isPresent()) {
                note = noteSearch.get();
            } else {
                NoteColumn noteColumn = noteColumnService.findOneOrCreateByUserAndTitle(model.getColumnTitle(), user);

                Integer order = 0;

                if (noteColumn.getId() != null) {
                    final Optional<Integer> maxOrderSearch = noteRepository.findMaxOrderByUserIdAndColumnId(user.getId(), noteColumn.getId());
                    if (maxOrderSearch.isPresent()) {
                        order = maxOrderSearch.get() + 1;
                    }
                }

                note = new Note(noteColumn, order, new Date(model.getLastOrderUpdate()));
            }
        } else {
            final Optional<Note> noteSearch = noteRepository.findById(model.getId());

            if (noteSearch.isPresent()) {
                note = noteSearch.get();
            } else {
                return new ResponseEntity<>("Note not found", HttpStatus.NOT_FOUND);
            }
        }

        Boolean columnChanged = note.getNoteColumn() == null || (note.getNoteColumn().getTitle() != model.getColumnTitle());

        if (columnChanged) {
            NoteColumn noteColumn = noteColumnService.findOneOrCreateByUserAndTitle(model.getColumnTitle(), user);

            note.setNoteColumn(noteColumn);
        }

        final List<NoteTag> tags = this.createNotSavedTags(model);

        note.setLastUpdate(new Date(model.getLastUpdate()));
        note.setQuestion(model.getQuestion());
        note.setAnswer(model.getAnswer());
        note.setTags(tags);

        Long newNoteVersion = noteVersionService.updateNoteVersion();

        final Note savedNote = noteRepository.save(note);

        final NoteSaveResponseModel response = new NoteSaveResponseModel(newNoteVersion, savedNote);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> deleteAll(List<NoteDeleteRequestModel> models) {
        final User user = authService.getCurrentUser();

        final List<Long> validIds = models.stream()
                .filter(model -> model.getId() != null)
                .map(model -> model.getId()).collect(Collectors.toList());

        if (validIds.size() > 0) {
            final List<Note> notes = noteRepository.findAllByIdAndUserId(validIds, user.getId());

            if (notes.size() > 0) {
                final List<Long> noteIds = notes.stream().map(note -> note.getId()).collect(Collectors.toList());

                noteRepository.deleteAll(notes);

                Long newNoteVersion = noteVersionService.updateNoteVersionDelete(noteIds);

                return new ResponseEntity<>(newNoteVersion, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(user.getNoteVersion().getNoteVersion(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> deleteNote(NoteDeleteRequestModel model) {
        final User user = authService.getCurrentUser();

        if (model == null || model.getId() == null) {
            return new ResponseEntity<>(user.getNoteVersion().getNoteVersion(), HttpStatus.OK);
        }

        final Optional<Note> noteSearch = noteRepository.findByIdAndUserId(model.getId(), user.getId());

        if (noteSearch.isPresent()) {
            Note note = noteSearch.get();
            Long noteId = note.getId();
            noteRepository.delete(note);

            Long newNoteVersion = noteVersionService.updateNoteVersionDelete(noteId);

            return new ResponseEntity<>(newNoteVersion, HttpStatus.OK);
        }

        return new ResponseEntity<>(user.getNoteVersion().getNoteVersion(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<NoteUpdateAllResponseModel> updateAll(List<NoteSaveRequestModel> models) {
        final User user = authService.getCurrentUser();
        final List<Note> notes = new ArrayList<>();

        final List<NoteSaveRequestModel> changedNotes = new ArrayList<>();
        final List<NoteSaveRequestModel> newNotes = new ArrayList<>();
        final List<Long> idsToUpdate = new ArrayList<>();
        final List<String> questions = new ArrayList<>();
        final List<String> columnTitles = new ArrayList<>();

        models.forEach(model -> {
            if (model.getId() != null) {
                changedNotes.add(model);
                idsToUpdate.add(model.getId());
            } else {
                newNotes.add(model);
            }
            questions.add(model.getQuestion());
            columnTitles.add(model.getColumnTitle());
        });

        final List<Note> databaseNotes = noteRepository.findAllByIdAndUserId(idsToUpdate, user.getId());

        final List<NoteColumn> databaseNoteColumns = noteColumnService.findAllByUserIdAndTitles(columnTitles, user.getId());

        Integer maxOrder = noteColumnService.getMaxOrder(user.getId());

        notes.addAll(this.updateSavedNotes(changedNotes, databaseNotes, user, databaseNoteColumns, maxOrder));

        notes.addAll(this.createNewNotes(newNotes, user, databaseNoteColumns, maxOrder));

        noteRepository.saveAll(notes);

        Long newNoteVersion = noteVersionService.updateNoteVersion();

        NoteUpdateAllResponseModel responseModel = new NoteUpdateAllResponseModel(notes, newNoteVersion);

        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateNotesOrder(List<NoteOrderRequestModel> models) {
        final User user = authService.getCurrentUser();

        final List<Long> ids = new ArrayList<>();

        final List<Integer> orders = new ArrayList<>();

        final List<String> columnTitles = new ArrayList<>();

        final List<Long> lastOrderUpdates = new ArrayList<>();

        models.stream().sorted(Comparator.comparing(NoteOrderRequestModel::getId)).forEach(m -> {
            ids.add(m.getId());
            orders.add(m.getOrder());
            columnTitles.add(m.getColumnTitle());
            lastOrderUpdates.add(m.getLastOrderUpdate());
        });

        final List<NoteColumn> columns = noteColumnService.findAllByUserIdAndTitles(columnTitles, user.getId());

        final List<Note> notes = noteRepository.findAllByIdOrderByIdAsc(ids, user.getId());

        if (notes.size() != ids.size()) {
            return new ResponseEntity<>(
                    "Nem todos os itens foram encontrados, encontramos " + notes.size() + " item de um total de  " + ids.size() + " buscados.",
                    HttpStatus.OK);
        }

        Integer noteColumnMaxOrder = noteColumnService.getMaxOrder(user.getId());

        int count = 0;

        final List<Note> updatedNotes = new ArrayList<>();

        for (Note note : notes) {
            final Long lastUpdateInModel = lastOrderUpdates.get(count);

            if (note.getLastOrderUpdate().getTime() < lastUpdateInModel) {
                note.setLastOrderUpdate(new Date(lastUpdateInModel));
                note.setOrder(orders.get(count));

                final int internalCount = count;

                List<NoteColumn> noteColumnSearch = columns.stream().filter(column -> column.getTitle().equalsIgnoreCase(columnTitles.get(internalCount))).collect(Collectors.toList());

                if (noteColumnSearch.size() > 0) {
                    note.setNoteColumn(noteColumnSearch.get(0));
                } else {
                    NoteColumn noteColumn = new NoteColumn(user, noteColumnMaxOrder, new Date());
                    noteColumn = noteColumnService.save(noteColumn);
                    noteColumnMaxOrder++;

                    note.setNoteColumn(noteColumn);
                }

                updatedNotes.add(note);
            }

            count++;
        }

        noteRepository.saveAll(updatedNotes);

        noteVersionService.updateNoteOrder(updatedNotes);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private List<Note> updateSavedNotes(List<NoteSaveRequestModel> models, List<Note> notes, User user, List<NoteColumn> columns, Integer columnMaxOrder) {
        final List<Note> updatedNotes = new ArrayList<>();
        final Date now = new Date();

        for (NoteSaveRequestModel model : models)  {
            Optional<Note> noteSearch = notes.stream().filter(n -> n.getId() == model.getId()).findFirst();

            if (noteSearch.isPresent()) {
                Note note = noteSearch.get();

                if (!model.getColumnTitle().equals(note.getNoteColumn().getTitle())) {
                    final Optional<NoteColumn> noteColumnSearch = columns.stream().filter(column -> column.getTitle().equals(model.getColumnTitle())).findFirst();

                    if (noteColumnSearch.isPresent()) {
                        note.setNoteColumn(noteColumnSearch.get());
                    } else {
                        note.setNoteColumn(noteColumnService.save(new NoteColumn(user, columnMaxOrder, new Date())));
                        columnMaxOrder++;
                    }
                }

                note.setAnswer(model.getAnswer());

                note.setQuestion(model.getQuestion());

                note.setLastUpdate(now);

                this.updateTags(note, model);

                updatedNotes.add(note);
            }
        }

        return updatedNotes;
    }

    private List<Note> createNewNotes(List<NoteSaveRequestModel> models, User user, List<NoteColumn> columns, Integer columnMaxOrder) {
        final List<Note> newNotes = new ArrayList<>();
        final Date now = new Date();

        for(NoteSaveRequestModel model : models) {
            NoteColumn noteColumn;
            final Optional<NoteColumn> noteColumnSearch = columns.stream().filter(column -> column.getTitle().equals(model.getColumnTitle())).findFirst();

            if (noteColumnSearch.isPresent()) {
                noteColumn = noteColumnSearch.get();
            } else {
                noteColumn = noteColumnService.save(new NoteColumn(user, columnMaxOrder, new Date()));
                columnMaxOrder++;
            }

            List<NoteTag> tags = createNotSavedTags(model);

            Note note = new Note();
            note.setLastUpdate(now);
            note.setQuestion(model.getQuestion());
            note.setTags(tags);
            note.setLastOrderUpdate(new Date(model.getLastOrderUpdate()));
            note.setOrder(model.getOrder());
            note.setNoteColumn(noteColumn);

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
