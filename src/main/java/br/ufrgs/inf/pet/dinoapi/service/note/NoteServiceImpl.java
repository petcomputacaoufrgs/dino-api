package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.constants.NoteConstants;
import br.ufrgs.inf.pet.dinoapi.entity.note.Note;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteTag;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.model.notes.*;
import br.ufrgs.inf.pet.dinoapi.model.notes.sync.note.*;
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
        final User user = authService.getCurrentUser();

        Note note;

        if (model.getId() == null) {
            final Optional<Note> noteSearch = noteRepository.findByQuestionAndUserId(model.getQuestion(), user.getId());

            if (noteSearch.isPresent()) {
                note = noteSearch.get();
            } else {
                NoteColumn noteColumn = noteColumnService.findOneOrCreateByUserAndTitle(model.getColumnTitle(), user);

                if (noteColumn.getNotes().size() >= NoteConstants.MAX_NOTES_PER_COLUMN) {
                    return new ResponseEntity<>(NoteConstants.MAX_NOTES_PER_COLUMN_MESSAGE, HttpStatus.BAD_REQUEST);
                }

                int order = 0;

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
                return new ResponseEntity<>(NoteConstants.NOTE_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
            }
        }

        boolean columnChanged = note.getNoteColumn() == null || (!note.getNoteColumn().getTitle().equals(model.getColumnTitle()));

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
                .map(NoteDeleteRequestModel::getId).collect(Collectors.toList());

        if (validIds.size() > 0) {
            final List<Note> notes = noteRepository.findAllByIdAndUserId(validIds, user.getId());

            if (notes.size() > 0) {
                final List<Long> noteIds = notes.stream().map(Note::getId).collect(Collectors.toList());

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
    public ResponseEntity<NoteSyncResponseModel> sync(NoteSyncRequestModel model) {
        final User user = authService.getCurrentUser();

        final boolean hasDeletedNotes = this.deleteNotes(model.getDeletedNotes(), user);

        final boolean hasChangedNotes = this.updateSavedNotes(model.getChangedNotes(), user);

        final boolean hasNewNotes = this.createNewNotes(model.getNewNotes(), user);

        final boolean hasChangedOrder = this.syncNotesOrder(model.getNotesOrder(), user);

        Long version;

        if (hasDeletedNotes || hasChangedNotes || hasNewNotes) {
            version = noteVersionService.updateNoteVersion();
        } else {
            version = noteVersionService.getVersion().getNoteVersion();
            if (hasChangedOrder) {
                final List<Note> notes = noteRepository.findAllByUserId(user.getId());
                noteVersionService.updateNoteOrder(notes);
            }
        }

        final List<Note> notes = noteRepository.findAllByUserId(user.getId());

        final List<NoteResponseModel> notesModel = notes.stream().map(NoteResponseModel::new).collect(Collectors.toList());

        NoteSyncResponseModel response = new NoteSyncResponseModel();
        response.setNotes(notesModel);
        response.setVersion(version);

        return new ResponseEntity<>(response, HttpStatus.OK);
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
                    NoteConstants.NOTE_NOT_FOUND_IN_ORDER_MESSAGE_PT1
                            + notes.size()
                            + NoteConstants.NOTE_NOT_FOUND_IN_ORDER_MESSAGE_PT2
                            + ids.size()
                            + NoteConstants.NOTE_NOT_FOUND_IN_ORDER_MESSAGE_PT3,
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

    private boolean updateSavedNotes(List<NoteSyncChangedRequestModel> changedNotes, User user) {
        final List<Note> updatedNotes = new ArrayList<>();

        final List<Long> idsToUpdate = changedNotes.stream().map(NoteSyncChangedRequestModel::getId).collect(Collectors.toList());

        final List<Note> savedNotes = noteRepository.findAllByIdAndUserId(idsToUpdate, user.getId());

        final Date now = new Date();

        Date changedLastUpdateDate;
        Date changedLastUpdateOrderDate;
        boolean changed;

        final List<String> usedQuestions = noteRepository.findAllQuestionsByUserId(user.getId());

        for (NoteSyncChangedRequestModel changedNote : changedNotes)  {
            changedLastUpdateDate = new Date(changedNote.getLastUpdate());
            Optional<Note> savedNoteSearch = savedNotes.stream().filter(n -> n.getId().equals(changedNote.getId())).findFirst();

            if (savedNoteSearch.isPresent()) {
                Note savedNote = savedNoteSearch.get();
                changed = false;

                if (changedLastUpdateDate.after(savedNote.getLastUpdate())) {
                    if (!changedNote.getQuestion().equals(savedNote.getQuestion())) {
                        boolean success = this.removeQuestionConflict(usedQuestions, changedNote);
                        if (success) {
                            usedQuestions.add(changedNote.getQuestion());
                            savedNote.setQuestion(changedNote.getQuestion());
                        }
                    }
                    savedNote.setLastUpdate(changedLastUpdateDate);
                    changed = true;
                }
                if (changedNote.getLastOrderUpdate() != null) {
                    changedLastUpdateOrderDate = new Date(changedNote.getLastOrderUpdate());
                    if (changedLastUpdateOrderDate.after(savedNote.getLastOrderUpdate())) {
                        final List<NoteTag> tags = this.createNotSavedTags(changedNote);

                        if (!changedNote.getColumnId().equals(savedNote.getNoteColumn().getId())) {
                            noteColumnService.getNoteColumnByIdAndUser(changedNote.getColumnId(), user).ifPresent(savedNote::setNoteColumn);
                        }

                        savedNote.setOrder(changedNote.getOrder());
                        savedNote.setLastOrderUpdate(changedLastUpdateOrderDate);
                        savedNote.setTags(tags);

                        changed = true;
                    }
                }

                if (changed) {
                    updatedNotes.add(savedNote);
                }
            } else {
                final Optional<NoteColumn> columnSearch = noteColumnService.getNoteColumnByIdAndUser(changedNote.getColumnId(), user);

                if (columnSearch.isPresent()) {
                    final NoteColumn column = columnSearch.get();
                    final List<NoteTag> tags = this.createNotSavedTags(changedNote);

                    final Note newNote = this.createNewNote(usedQuestions, changedNote, column, now, tags);
                    updatedNotes.add(newNote);
                }
            }
        }

        if (updatedNotes.size() > 0) {
            noteRepository.saveAll(updatedNotes);
            return true;
        }

        return false;
    }

    private boolean createNewNotes(List<NoteSyncSaveRequestModel> models, User user) {
        final List<Note> newNotes = new ArrayList<>();
        final Date now = new Date();
        final List<String> usedQuestions = noteRepository.findAllQuestionsByUserId(user.getId());

        for(NoteSyncSaveRequestModel model : models) {
            final Optional<NoteColumn> noteColumnSearch = noteColumnService.getNoteColumnByIdAndUser(model.getColumnId(), user);

            if (noteColumnSearch.isPresent()) {
                List<NoteTag> tags = createNotSavedTags(model);

                Note note = this.createNewNote(usedQuestions, model, noteColumnSearch.get(), now, tags);

                newNotes.add(note);
            }
        }

        if (newNotes.size() > 0) {
            noteRepository.saveAll(newNotes);

            return true;
        }

        return false;
    }

    private Note createNewNote(List<String> usedQuestions, NoteSyncSaveRequestModel newNote, NoteColumn noteColumn, Date now, List<NoteTag> tags) {
        boolean success = this.removeQuestionConflict(usedQuestions, newNote);
        if (success) {
            usedQuestions.add(newNote.getQuestion());

            Integer maxOrder = noteColumn.getNotes().stream().map(Note::getOrder).max(Integer::compare).orElse(-1);

            Note note = new Note();
            note.setLastUpdate(new Date(newNote.getLastUpdate()));
            note.setQuestion(newNote.getQuestion());
            note.setAnswer(newNote.getAnswer());
            note.setTags(tags);
            note.setLastOrderUpdate(now);
            note.setOrder(maxOrder + 1);
            note.setNoteColumn(noteColumn);

            return note;
        }

        return null;
    }

    private boolean deleteNotes(List<NoteSyncDeleteRequest> deletedNotes, User user) {
        final List<Long> deletedIds = deletedNotes.stream().map(NoteDeleteRequestModel::getId).collect(Collectors.toList());
        final List<Note> savedNotes = noteRepository.findAllByIdAndUserId(deletedIds, user.getId());
        final List<Note> notesToDelete = new ArrayList<>();

        savedNotes.forEach(savedNote -> {
            deletedNotes.stream().filter(deletedNote -> deletedNote.getId().equals(savedNote.getId())).findFirst().ifPresent(deletedModel -> {
                final Date deletedLastUpdate = new Date(deletedModel.getLastUpdate());
                if (!savedNote.getLastUpdate().after(deletedLastUpdate)) {
                    notesToDelete.add(savedNote);
                }
            });
        });

        if (notesToDelete.size() > 0) {
            noteRepository.deleteAll(notesToDelete);
            return true;
        }

        return false;
    }

    private boolean syncNotesOrder(List<NoteSyncOrderRequestModel> notesOrder, User user) {
        final List<Long> ids = notesOrder.stream().map(NoteSyncOrderRequestModel::getId).collect(Collectors.toList());
        final List<Note> notes = noteRepository.findAllByIdAndUserId(ids, user.getId());

        final List<Long> columnIds = notesOrder.stream().map(NoteSyncOrderRequestModel::getColumnId).collect(Collectors.toList());
        final List<NoteColumn> columns = noteColumnService.findAllByUserAndIds(user, columnIds);

        if (notes.size() > 0) {
            notesOrder.forEach(item -> {
                Optional<Note> noteSearch = notes.stream()
                        .filter(note -> note.getId().equals(item.getId())).findFirst();

                Optional<NoteColumn> noteColumnSearch = columns.stream()
                        .filter(column -> column.getId().equals(item.getColumnId())).findFirst();

                noteColumnSearch.ifPresent(column -> {
                    noteSearch.ifPresent(note -> {
                        if (!note.getLastOrderUpdate().after(new Date(item.getLastOrderUpdate()))) {
                            note.setOrder(item.getOrder());
                            note.setLastOrderUpdate(new Date(item.getLastOrderUpdate()));
                            note.setNoteColumn(column);
                        }
                    });
                });
            });

            noteRepository.saveAll(notes);
            return true;
        }

        return false;
    }

    private List<NoteTag> createNotSavedTags(NoteSaveBaseModel model) {
        List<NoteTag> tags = new ArrayList<>();

        if (model.getTagNames() != null && model.getTagNames().size() > 0) {
            tags = noteTagRepository.findAllByName(model.getTagNames());

            final List<String> tagNames = tags.stream().map(NoteTag::getName).collect(Collectors.toList());

            List<NoteTag> newTags = model.getTagNames().stream()
                    .filter(name -> !tagNames.contains(name))
                    .map(NoteTag::new)
                    .collect(Collectors.toList());

            if (newTags.size() > 0) {
                newTags = Lists.newArrayList((noteTagRepository.saveAll(newTags)));
                tags.addAll(newTags);
            }
        }

        return tags;
    }

    private boolean removeQuestionConflict(List<String> usedTitles, NoteSyncSaveRequestModel newNote) {
        boolean sameTitle = usedTitles.stream().anyMatch(title -> title.equals(newNote.getQuestion()));

        if (sameTitle) {
            final int titleLength = newNote.getQuestion().length();
            final String resumeTitle = "...";
            int count = 0;
            int extraLength = 0;
            String repeatCounter;
            String newQuestion;

            do {
                count++;
                repeatCounter = " ("+count+")";
                extraLength = repeatCounter.length() + resumeTitle.length();

                if (extraLength > NoteConstants.QUESTION_MAX) {
                    return false;
                }

                if (titleLength + repeatCounter.length() > NoteConstants.QUESTION_MAX) {
                    newQuestion = newNote.getQuestion().substring(0, NoteConstants.QUESTION_MAX - extraLength) + resumeTitle + repeatCounter;
                } else {
                    newQuestion = newNote.getQuestion() + repeatCounter;
                }
                final String finalNewQuestion = newQuestion;
                sameTitle = usedTitles.stream().anyMatch(title -> title.equals(finalNewQuestion));

            } while(sameTitle);

            newNote.setQuestion(newQuestion);
        }

        return true;
    }
}
