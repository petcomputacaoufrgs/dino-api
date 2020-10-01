package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.constants.NoteColumnConstants;
import br.ufrgs.inf.pet.dinoapi.constants.NoteConstants;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import br.ufrgs.inf.pet.dinoapi.model.notes.*;
import br.ufrgs.inf.pet.dinoapi.repository.note.NoteColumnRepository;
import br.ufrgs.inf.pet.dinoapi.repository.note.NoteRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class NoteColumnServiceImpl implements NoteColumnService {

    private final NoteColumnRepository noteColumnRepository;

    private final NoteRepository noteRepository;

    private final NoteVersionServiceImpl noteVersionService;

    private final AuthServiceImpl authService;

    @Autowired
    public NoteColumnServiceImpl(NoteColumnRepository noteColumnRepository, NoteVersionServiceImpl noteVersionService, AuthServiceImpl authService, NoteRepository noteRepository) {
        this.noteColumnRepository = noteColumnRepository;
        this.noteRepository = noteRepository;
        this.noteVersionService = noteVersionService;
        this.authService = authService;
    }

    @Override
    public ResponseEntity<List<NoteColumnResponseModel>> getUserColumns() {
        final User user = authService.getCurrentUser();

        final List<NoteColumn> columns = noteColumnRepository.findAllByUserId(user.getId());

        final List<NoteColumnResponseModel> model = columns.stream().map(NoteColumnResponseModel::new).collect(Collectors.toList());

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> save(NoteColumnSaveRequestModel model) {
        final User user = authService.getCurrentUser();
        NoteColumn noteColumn;
        Long newNoteColumnVersion;

        Integer count = noteColumnRepository.countNoteColumnByUserId(user.getId());

        if (count > NoteColumnConstants.MAX_COLUMNS) {
            return new ResponseEntity<>(NoteColumnConstants.MAX_COLUMNS_MESSAGE, HttpStatus.NOT_FOUND);
        }

        if (model.getId() == null) {
            final Optional<NoteColumn> noteColumnSearch = noteColumnRepository.findByTitleAndUserId(model.getTitle(), user.getId());

            if (noteColumnSearch.isPresent()) {
                noteColumn = noteColumnSearch.get();
            } else {
                final Optional<Integer> maxOrderSearch = noteColumnRepository.findMaxOrderByUserId(user.getId());

                Integer order = 0;

                if (maxOrderSearch.isPresent()) {
                    order = maxOrderSearch.get() + 1;
                }

                noteColumn = new NoteColumn(user, order, new Date(model.getLastOrderUpdate()));
            }
        } else {
            Optional<NoteColumn> noteColumnSearch = noteColumnRepository.findByIdAndUserId(model.getId(), user.getId());

            if (noteColumnSearch != null && noteColumnSearch.isPresent()) {
                noteColumn = noteColumnSearch.get();
            } else {
                return new ResponseEntity<>(NoteColumnConstants.NOTE_COLUMN_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
            }
        }

        noteColumn.setLastUpdate(new Date(model.getLastUpdate()));
        noteColumn.setTitle(model.getTitle());

        newNoteColumnVersion = noteVersionService.updateColumnVersion();

        final NoteColumn savedNoteColumn = noteColumnRepository.save(noteColumn);

        final NoteColumnSaveResponseModel response = new NoteColumnSaveResponseModel(newNoteColumnVersion, savedNoteColumn);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> deleteAll(List<NoteColumnDeleteRequestModel> models) {
        final User user = authService.getCurrentUser();

        final List<Long> ids = models.stream()
                .map(model -> model.getId()).collect(Collectors.toList());

        List<NoteColumn> noteColumns = noteColumnRepository.findAllByIdAndUserId(ids, user.getId());

        if (noteColumns.size() > 0) {
            final Integer deletedNotesCount = noteRepository.countNotesByNoteColumnsIds(ids);

            noteColumnRepository.deleteAll(noteColumns);

            Long newNoteColumnVersion = noteVersionService.updateColumnVersionDelete(
                    noteColumns.stream().map(noteColumn -> noteColumn.getId()).collect(Collectors.toList()));

            if (deletedNotesCount > 0) {
                noteVersionService.updateNoteVersion();
            }
            
            return new ResponseEntity<>(newNoteColumnVersion, HttpStatus.OK);
        }

        return new ResponseEntity<>(user.getNoteVersion().getColumnVersion(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> delete(NoteColumnDeleteRequestModel model) {
        final User user = authService.getCurrentUser();

        if (model == null || model.getId() == null) {
            return new ResponseEntity<>(user.getNoteVersion().getColumnVersion(), HttpStatus.OK);
        }

        final Optional<NoteColumn> noteSearch = noteColumnRepository.findByIdAndUserId(model.getId(), user.getId());

        if (noteSearch.isPresent()) {
            final Integer deletedNotesCount = noteRepository.countNotesByNoteColumnId(model.getId());

            NoteColumn noteColumn = noteSearch.get();

            noteColumnRepository.delete(noteColumn);

            Long newNoteVersion = noteVersionService.updateColumnVersionDelete(noteColumn.getId());

            if (deletedNotesCount > 0) {
                noteVersionService.updateNoteVersion();
            }

            return new ResponseEntity<>(newNoteVersion, HttpStatus.OK);
        }

        return new ResponseEntity<>(user.getNoteVersion().getColumnVersion(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateAll(List<NoteColumnSaveRequestModel> models) {
        final User user = authService.getCurrentUser();
        final List<NoteColumn> noteColumns = new ArrayList<>();

        final List<NoteColumnSaveRequestModel> changedNoteColumns = new ArrayList<>();
        final List<NoteColumnSaveRequestModel> newNoteColumns = new ArrayList<>();
        final List<Long> idsToUpdate = new ArrayList<>();
        final List<String> titles = new ArrayList<>();

        models.forEach(model -> {
            if (model.getId() != null) {
                changedNoteColumns.add(model);
                idsToUpdate.add(model.getId());
            } else {
                newNoteColumns.add(model);
            }
            titles.add(model.getTitle());
        });

        final List<NoteColumn> databaseNoteColumns = noteColumnRepository.findAllByIdAndUserId(idsToUpdate, user.getId());

        if (databaseNoteColumns.size() > 2*NoteColumnConstants.MAX_COLUMNS) {
            return new ResponseEntity<>(NoteColumnConstants.MAX_COLUMNS_MESSAGE, HttpStatus.BAD_REQUEST);
        }

        noteColumns.addAll(this.updateSavedColumns(changedNoteColumns, databaseNoteColumns));

        noteColumns.addAll(this.createNewColumns(newNoteColumns, user));

        noteColumnRepository.saveAll(noteColumns);

        Long newNoteColumnVersion = noteVersionService.updateColumnVersion();

        NoteColumnUpdateAllResponseModel model = new NoteColumnUpdateAllResponseModel(noteColumns, newNoteColumnVersion);

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateOrder(List<NoteColumnOrderRequestModel> models) {
        final User user = authService.getCurrentUser();

        final List<String> titlesWithoutId = models.stream().filter(model -> model.getId() == null).map(model -> model.getColumnTitle()).collect(Collectors.toList());

        final List<Long> ids = new ArrayList<>();

        final List<Integer> orders = new ArrayList<>();

        final List<NoteColumn> savedNoteColumns = new ArrayList<>();

        final List<Long> lastOrderUpdates = new ArrayList<>();

        if (titlesWithoutId.size() > 0) {
            savedNoteColumns.addAll(noteColumnRepository.findByTitlesAndUserId(titlesWithoutId, user.getId()));
        }

        models.stream().forEach(model -> {
            if (model.getId() == null) {
                List<NoteColumn> savedColumn = savedNoteColumns.stream()
                        .filter(savedNote ->
                                savedNote.getTitle().equals(model.getColumnTitle())).collect(Collectors.toList());

                if (savedColumn.size() > 0) {
                    model.setId(savedColumn.get(0).getId());
                } else {
                    NoteColumn noteColumn = new NoteColumn(user, model.getOrder(), new Date(model.getLastOrderUpdate()));
                    noteColumn = noteColumnRepository.save(noteColumn);
                    model.setId(noteColumn.getId());
                }
            }
        });

        models.stream().sorted(Comparator.comparing(NoteColumnOrderRequestModel::getId)).forEach(m -> {
            ids.add(m.getId());
            orders.add(m.getOrder());
            lastOrderUpdates.add(m.getLastOrderUpdate());
        });

        final Set<Integer> uniqueOrders = new HashSet<>(orders);

        if (orders.size() != uniqueOrders.size()) {
            return new ResponseEntity<>(NoteColumnConstants.SAME_ORDER_MESSAGE, HttpStatus.BAD_REQUEST);
        }

        final List<NoteColumn> noteColumns = noteColumnRepository.findAllByIdOrderByIdAsc(ids, user.getId());

        if (noteColumns.size() != ids.size()) {
            return new ResponseEntity<>(
                    NoteColumnConstants.COLUMN_NOT_FOUND_IN_ORDER_MESSAGE_PT1
                            + noteColumns.size()
                            + NoteColumnConstants.COLUMN_NOT_FOUND_IN_ORDER_MESSAGE_PT2
                            + ids.size()
                            + NoteColumnConstants.COLUMN_NOT_FOUND_IN_ORDER_MESSAGE_PT3,
                    HttpStatus.OK);
        }

        IntStream.range(0, noteColumns.size())
                .forEach(i -> {
                    NoteColumn noteColumn = noteColumns.get(i);
                    noteColumn.setOrder(orders.get(i));
                    noteColumn.setLastOrderUpdate(new Date(lastOrderUpdates.get(i)));
                });


        noteColumnRepository.saveAll(noteColumns);

        noteVersionService.updateColumnOrder(noteColumns);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public List<NoteColumn> findAllByUserIdAndTitles(List<String> titles, Long userId) {
        return noteColumnRepository.findByTitlesAndUserId(titles, userId);
    }

    @Override
    public NoteColumn findOneOrCreateByUserAndTitle(String title, User user) {
        Optional<NoteColumn> search = noteColumnRepository.findByTitleAndUserId(title, user.getId());

        if (search.isPresent()) {
            return search.get();
        } else {
            Integer maxOrder = this.getMaxOrder(user.getId());

            return new NoteColumn(user, maxOrder, new Date());
        }
    }

    @Override
    public Integer getMaxOrder(Long userId) {
        Optional<Integer> maxOrderSearch = noteColumnRepository.findMaxOrderByUserId(userId);

        if (maxOrderSearch.isPresent()) {
            return maxOrderSearch.get();
        }

        return 0;
    }

    @Override
    public NoteColumn save(NoteColumn noteColumn) {
        return noteColumnRepository.save(noteColumn);
    }

    private List<NoteColumn> updateSavedColumns(List<NoteColumnSaveRequestModel> models, List<NoteColumn> columns) {
        final List<NoteColumn> updatedColumns = new ArrayList<>();
        final Date now = new Date();

        for (NoteColumnSaveRequestModel model : models)  {
            Optional<NoteColumn> noteColumnSearch = columns.stream().filter(n -> n.getId() == model.getId()).findFirst();

            if (noteColumnSearch.isPresent()) {
                NoteColumn noteColumn = noteColumnSearch.get();

                noteColumn.setTitle(model.getTitle());

                noteColumn.setLastUpdate(now);

                updatedColumns.add(noteColumn);
            }
        }

        return updatedColumns;
    }

    private List<NoteColumn> createNewColumns(List<NoteColumnSaveRequestModel> models, User user) {
        final List<NoteColumn> newNoteColumns = new ArrayList<>();
        final Date now = new Date();

        for(NoteColumnSaveRequestModel model : models) {
            NoteColumn noteColumn = new NoteColumn();
            noteColumn.setLastUpdate(new Date(model.getLastUpdate()));
            noteColumn.setLastOrderUpdate(new Date(model.getLastOrderUpdate()));
            noteColumn.setOrder(model.getOrder());
            noteColumn.setTitle(model.getTitle());
            noteColumn.setUser(user);

            newNoteColumns.add(noteColumn);
        }

        return newNoteColumns;
    }

}
