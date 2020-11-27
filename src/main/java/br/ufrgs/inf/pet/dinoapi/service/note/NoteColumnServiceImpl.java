package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.constants.NoteColumnConstants;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import br.ufrgs.inf.pet.dinoapi.model.note.delete.NoteColumnDeleteRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.get.NoteColumnResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.note.order.NoteColumnOrderRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.save.NoteColumnSaveRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.save.NoteColumnSaveResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.note.sync.column.*;
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

        final Integer count = noteColumnRepository.countNoteColumnByUserId(user.getId());

        if (count > NoteColumnConstants.MAX_COLUMNS) {
            return new ResponseEntity<>(NoteColumnConstants.MAX_COLUMNS_MESSAGE, HttpStatus.NOT_FOUND);
        }

        if (model.getId() == null) {
            final Optional<NoteColumn> noteColumnSearch = noteColumnRepository.findByTitleAndUserId(model.getTitle(), user.getId());

            if (noteColumnSearch.isPresent()) {
                noteColumn = noteColumnSearch.get();
            } else {
                final Optional<Integer> maxOrderSearch = noteColumnRepository.findMaxOrderByUserId(user.getId());

                int order = 0;

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
                .map(NoteColumnDeleteRequestModel::getId).collect(Collectors.toList());

        final List<NoteColumn> noteColumns = noteColumnRepository.findAllByIdAndUserId(ids, user.getId());

        if (noteColumns.size() > 0) {
            final Integer deletedNotesCount = noteRepository.countNotesByNoteColumnsIds(ids);

            noteColumnRepository.deleteAll(noteColumns);

            Long newNoteColumnVersion = noteVersionService.updateColumnVersionDelete(
                    noteColumns.stream().map(NoteColumn::getId).collect(Collectors.toList()));

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

            final NoteColumn noteColumn = noteSearch.get();

            noteColumnRepository.delete(noteColumn);

            final Long newNoteVersion = noteVersionService.updateColumnVersionDelete(noteColumn.getId());

            if (deletedNotesCount > 0) {
                noteVersionService.updateNoteVersion();
            }

            return new ResponseEntity<>(newNoteVersion, HttpStatus.OK);
        }

        return new ResponseEntity<>(user.getNoteVersion().getColumnVersion(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<NoteColumnSyncResponse> sync(NoteColumnSyncRequestModel model) {
        final User user = authService.getCurrentUser();

        final List<ChangedTitleColumnModel> changedTitleColumnModels = new ArrayList<>();

        final boolean hasDeletedColumns = this.deleteColumns(model.getDeletedColumns(), user);

        final boolean hasChangedColumns = this.updateSavedColumns(model.getChangedColumns(), user, changedTitleColumnModels);

        final boolean hasNewColumns = this.createNewColumns(model.getNewColumns(), user, changedTitleColumnModels);

        final boolean hasChangedOrder = this.syncColumnsOrder(model.getColumnsOrder(), user);

        Long version;

        if (hasDeletedColumns || hasChangedColumns || hasNewColumns) {
            version = noteVersionService.updateColumnVersion();
        } else {
            version = noteVersionService.getVersion().getColumnVersion();
            if (hasChangedOrder) {
                final List<NoteColumn> columns = noteColumnRepository.findAllByUserId(user.getId());
                noteVersionService.updateColumnOrder(columns);
            }
        }

        final List<NoteColumn> columns = noteColumnRepository.findAllByUserId(user.getId());

        final List<NoteColumnResponseModel> columnsModel = columns.stream().map(NoteColumnResponseModel::new).collect(Collectors.toList());

        final NoteColumnSyncResponse response = new NoteColumnSyncResponse();
        response.setColumns(columnsModel);
        response.setVersion(version);
        response.setChangedTitleColumnModels(changedTitleColumnModels);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateOrder(List<NoteColumnOrderRequestModel> models) {
        final User user = authService.getCurrentUser();

        final List<String> titlesWithoutId = models.stream().filter(model -> model.getId() == null).map(NoteColumnOrderRequestModel::getColumnTitle).collect(Collectors.toList());

        final List<Long> ids = new ArrayList<>();

        final List<Integer> orders = new ArrayList<>();

        final List<NoteColumn> savedNoteColumns = new ArrayList<>();

        final List<Long> lastOrderUpdates = new ArrayList<>();

        if (titlesWithoutId.size() > 0) {
            savedNoteColumns.addAll(noteColumnRepository.findByTitlesAndUserId(titlesWithoutId, user.getId()));
        }

        models.forEach(model -> {
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
        final Optional<NoteColumn> search = noteColumnRepository.findByTitleAndUserId(title, user.getId());

        if (search.isPresent()) {
            return search.get();
        } else {
            final Integer maxOrder = this.getMaxOrder(user.getId());

            return new NoteColumn(user, maxOrder, new Date());
        }
    }

    @Override
    public Integer getMaxOrder(Long userId) {
        final Optional<Integer> maxOrderSearch = noteColumnRepository.findMaxOrderByUserId(userId);

        return maxOrderSearch.orElse(0);
    }

    @Override
    public NoteColumn save(NoteColumn noteColumn) {
        return noteColumnRepository.save(noteColumn);
    }

    @Override
    public Optional<NoteColumn> getNoteColumnByIdAndUser(Long columnId, User user) {
        return noteColumnRepository.findByIdAndUserId(columnId, user.getId());
    }

    @Override
    public List<NoteColumn> findAllByUserAndIds(User user, List<Long> columnIds) {
        return noteColumnRepository.findAllByIdAndUserId(columnIds, user.getId());
    }

    private boolean updateSavedColumns(List<NoteColumnSyncChangedRequestModel> changedColumns, User user, List<ChangedTitleColumnModel> changedTitleColumnModels) {
        final List<NoteColumn> updatedColumns = new ArrayList<>();

        final List<Long> idsToUpdate = changedColumns.stream().map(NoteColumnSyncChangedRequestModel::getId).collect(Collectors.toList());

        final List<NoteColumn> savedColumns = noteColumnRepository.findAllByIdAndUserId(idsToUpdate, user.getId());

        Date changedLastUpdateDate;
        Date changedLastUpdateOrderDate;
        boolean changed;

        final List<String> usedTitles = noteColumnRepository.findAllTitlesByUserId(user.getId());
        final Integer maxOrder = noteColumnRepository.findMaxOrderByUserId(user.getId()).orElse(-1);

        for (NoteColumnSyncChangedRequestModel changedColumn : changedColumns)  {
            changedLastUpdateDate = new Date(changedColumn.getLastUpdate());
            Optional<NoteColumn> savedColumnSearch = savedColumns.stream().filter(n -> n.getId().equals(changedColumn.getId())).findFirst();

            if (savedColumnSearch.isPresent()) {
                NoteColumn savedColumn = savedColumnSearch.get();
                changed = false;

                if (changedLastUpdateDate.after(savedColumn.getLastUpdate())) {

                    if (!changedColumn.getTitle().equals(savedColumn.getTitle())) {
                        boolean success = this.removeTitleConflict(usedTitles, changedColumn, changedTitleColumnModels);
                        if (success) {
                            usedTitles.add(changedColumn.getTitle());
                            savedColumn.setTitle(changedColumn.getTitle());
                        }
                    }
                    savedColumn.setLastUpdate(changedLastUpdateDate);
                    changed = true;
                }
                if (changedColumn.getLastOrderUpdate() != null) {
                    changedLastUpdateOrderDate = new Date(changedColumn.getLastOrderUpdate());
                    if (changedLastUpdateOrderDate.after(savedColumn.getLastOrderUpdate())) {
                        savedColumn.setOrder(changedColumn.getOrder());
                        savedColumn.setLastOrderUpdate(changedLastUpdateOrderDate);

                        changed = true;
                    }
                }

                if (changed) {
                    updatedColumns.add(savedColumn);
                }
            } else {
                NoteColumn noteColumn = this.createNewNoteColumn(usedTitles, changedColumn, maxOrder, user, changedTitleColumnModels);
                if (noteColumn != null) {
                    updatedColumns.add(noteColumn);
                }
            }
        }

        if (updatedColumns.size() > 0) {
            noteColumnRepository.saveAll(updatedColumns);
            return true;
        }

        return false;
    }

    private boolean createNewColumns(List<NoteColumnSaveRequestModel> newColumns, User user, List<ChangedTitleColumnModel> changedTitleColumnModels) {
        final List<NoteColumn> newNoteColumns = new ArrayList<>();

        final List<String> usedTitles = noteColumnRepository.findAllTitlesByUserId(user.getId());

        final Integer maxOrder = noteColumnRepository.findMaxOrderByUserId(user.getId()).orElse(-1);

        for (NoteColumnSaveRequestModel newColumn : newColumns) {
            final NoteColumn noteColumn = this.createNewNoteColumn(usedTitles, newColumn, maxOrder, user, changedTitleColumnModels);
            if (noteColumn != null) {
                newNoteColumns.add(noteColumn);
            }
        }

        if (newNoteColumns.size() > 0) {
            noteColumnRepository.saveAll(newNoteColumns);
            return true;
        }

        return false;
    }

    private NoteColumn createNewNoteColumn(List<String> usedTitles, NoteColumnSaveRequestModel newColumn, Integer maxOrder, User user, List<ChangedTitleColumnModel> changedTitleColumnModels) {
        final boolean success = this.removeTitleConflict(usedTitles, newColumn, changedTitleColumnModels);
        if (success) {
            usedTitles.add(newColumn.getTitle());

            maxOrder++;

            NoteColumn noteColumn = new NoteColumn();
            noteColumn.setLastUpdate(new Date(newColumn.getLastUpdate()));
            noteColumn.setLastOrderUpdate(new Date());
            noteColumn.setOrder(maxOrder);
            noteColumn.setTitle(newColumn.getTitle());
            noteColumn.setUser(user);

           return noteColumn;
        }

        return null;
    }

    private boolean deleteColumns(List<NoteColumnSyncDeleteRequestModel> deletedColumns, User user) {
        final List<Long> deletedIds = deletedColumns.stream().map(NoteColumnDeleteRequestModel::getId).collect(Collectors.toList());
        final List<NoteColumn> savedColumns = noteColumnRepository.findAllByIdAndUserIdWithNotes(deletedIds, user.getId());
        final List<NoteColumn> columnsToDelete = new ArrayList<>();

        savedColumns.forEach(savedColumn -> {
            deletedColumns.stream().filter(deletedColumn -> deletedColumn.getId().equals(savedColumn.getId())).findFirst().ifPresent(deletedModel -> {
                final Date deletedLastUpdate = new Date(deletedModel.getLastUpdate());
                if (!savedColumn.getLastUpdate().after(deletedLastUpdate)) {
                    final boolean columnNoteMoreUpdated = savedColumn.getNotes().stream().anyMatch(note -> note.getLastUpdate().after(deletedLastUpdate));

                    if (!columnNoteMoreUpdated) {
                        columnsToDelete.add(savedColumn);
                    }
                }
            });
        });

        if (columnsToDelete.size() > 0) {
            noteColumnRepository.deleteAll(columnsToDelete);
            return true;
        }

        return false;
    }

    private boolean syncColumnsOrder(List<NoteColumnSyncOrderRequestModel> orderList, User user) {
        final List<Long> ids = orderList.stream().map(NoteColumnSyncOrderRequestModel::getId).collect(Collectors.toList());
        final List<NoteColumn> columns = noteColumnRepository.findAllByIdAndUserId(ids, user.getId());

        if (columns.size() > 0) {
            orderList.forEach(item -> {
                Optional<NoteColumn> columnSearch = columns.stream()
                        .filter(column -> column.getId().equals(item.getId())).findFirst();

                columnSearch.ifPresent(column -> {
                    column.setOrder(item.getOrder());
                    column.setLastOrderUpdate(new Date(item.getLastOrderUpdate()));
                });
            });

            noteColumnRepository.saveAll(columns);
            return true;
        }

        return false;
    }

    private boolean removeTitleConflict(List<String> usedTitles, NoteColumnSaveRequestModel newColumn, List<ChangedTitleColumnModel> changedTitleColumnModels) {
        boolean sameTitle = usedTitles.stream().anyMatch(title -> title.equals(newColumn.getTitle()));

        if (sameTitle) {
            final int titleLength = newColumn.getTitle().length();
            final String resumeTitle = "...";
            int count = 0;
            int extraLength = 0;
            String repeatCounter;
            String newTitle;

            do {
                count++;
                repeatCounter = " ("+count+")";
                extraLength = repeatCounter.length() + resumeTitle.length();

                if (extraLength > NoteColumnConstants.TITLE_MAX) {
                    return false;
                }

                if (titleLength + repeatCounter.length() > NoteColumnConstants.TITLE_MAX) {
                    newTitle = newColumn.getTitle().substring(0, NoteColumnConstants.TITLE_MAX - extraLength) + resumeTitle + repeatCounter;
                } else {
                    newTitle = newColumn.getTitle() + repeatCounter;
                }
                final String finalNewTitle = newTitle;
                sameTitle = usedTitles.stream().anyMatch(title -> title.equals(finalNewTitle));

            } while(sameTitle);

            ChangedTitleColumnModel changedTitleColumnModel = new ChangedTitleColumnModel();
            changedTitleColumnModel.setOldTitle(newColumn.getTitle());
            changedTitleColumnModel.setNewTitle(newTitle);

            changedTitleColumnModels.add(changedTitleColumnModel);

            newColumn.setTitle(newTitle);
        }

        return true;
    }
}
