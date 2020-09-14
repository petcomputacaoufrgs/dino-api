package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import br.ufrgs.inf.pet.dinoapi.model.notes.*;
import br.ufrgs.inf.pet.dinoapi.repository.note.NoteColumnRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.service.alert_update.queue.AlertUpdateQueueServiceImpl;
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

    private final NoteVersionServiceImpl noteVersionService;

    private final AuthServiceImpl authService;

    @Autowired
    public NoteColumnServiceImpl(NoteColumnRepository noteColumnRepository, NoteVersionServiceImpl noteVersionService, AuthServiceImpl authService) {
        this.noteColumnRepository = noteColumnRepository;
        this.noteVersionService = noteVersionService;
        this.authService = authService;
    }

    @Override
    public ResponseEntity<List<NoteColumnResponseModel>> getUserColumns() {
        final User user = authService.getCurrentAuth().getUser();

        final List<NoteColumn> columns = user.getNoteColumns();

        final List<NoteColumnResponseModel> model = columns.stream().map(NoteColumnResponseModel::new).collect(Collectors.toList());

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> save(NoteColumnSaveRequestModel model) {
        final User user = authService.getCurrentAuth().getUser();

        NoteColumn noteColumn;

        if (model.getId() == null && model.getOldTitle() == null) {
            final Optional<NoteColumn> noteColumnSearch = noteColumnRepository.findByTitleAndUserId(model.getTitle(), user.getId());

            if (noteColumnSearch.isPresent()) {
                noteColumn = noteColumnSearch.get();
            } else {
                final Optional<Integer> maxOrderSearch = noteColumnRepository.findMaxOrderByUserId(user.getId());

                Integer order = 0;

                if (maxOrderSearch.isPresent()) {
                    order = maxOrderSearch.get() + 1;
                }

                noteColumn = new NoteColumn(user, order);
            }
        } else {
            Optional<NoteColumn> noteColumnSearch = Optional.empty();

            if (model.getId() != null) {
                noteColumnSearch = noteColumnRepository.findByIdAndUserId(model.getId(), user.getId());
            } else if (model.getTitle() != null) {
                noteColumnSearch = noteColumnRepository.findByTitleAndUserId(model.getOldTitle(), user.getId());
            }

            if (noteColumnSearch != null && noteColumnSearch.isPresent()) {
                noteColumn = noteColumnSearch.get();
            } else {
                return new ResponseEntity<>("Note Column not found", HttpStatus.NOT_FOUND);
            }
        }

        noteColumn.setLastUpdate(new Date(model.getLastUpdate()));
        noteColumn.setTitle(model.getTitle());

        Long newNoteColumnVersion = noteVersionService.updateColumnVersion();

        final NoteColumn savedNoteColumn = noteColumnRepository.save(noteColumn);

        final NoteColumnSaveResponseModel response = new NoteColumnSaveResponseModel(newNoteColumnVersion, savedNoteColumn);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> deleteAll(List<NoteColumnDeleteRequestModel> models) {
        final User user = authService.getCurrentAuth().getUser();

        final List<Long> validIds = models.stream()
                .filter(model -> model.getId() != null)
                .map(model -> model.getId()).collect(Collectors.toList());

        int deletedItems = 0;

        if (validIds.size() > 0) {
            deletedItems = noteColumnRepository.deleteAllByIdAndUserId(validIds, user.getId());

            if (deletedItems > 0) {
                Long newNoteVersion = noteVersionService.updateColumnVersion();

                return new ResponseEntity<>(newNoteVersion, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(user.getNoteVersion().getColumnVersion(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> delete(NoteColumnDeleteRequestModel model) {
        final User user = authService.getCurrentAuth().getUser();

        if (model == null || model.getId() == null) {
            return new ResponseEntity<>(user.getNoteVersion().getColumnVersion(), HttpStatus.OK);
        }

        final int deletedItems = noteColumnRepository.deleteByIdAndUserId(model.getId(), user.getId());

        if (deletedItems > 0) {
            Long newNoteVersion = noteVersionService.updateColumnVersion();

            return new ResponseEntity<>(newNoteVersion, HttpStatus.OK);
        }

        return new ResponseEntity<>(user.getNoteVersion().getColumnVersion(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> updateAll(List<NoteColumnSaveRequestModel> models) {
        final User user = authService.getCurrentAuth().getUser();
        final List<NoteColumn> noteColumns = new ArrayList<>();

        final List<NoteColumnSaveRequestModel> changedNoteColumns = new ArrayList<>();
        final List<NoteColumnSaveRequestModel> newNoteColumns = new ArrayList<>();
        final List<Long> idsToUpdate = new ArrayList<>();
        final List<Integer> orders = new ArrayList<>();
        final List<String> titles = new ArrayList<>();

        models.forEach(model -> {
            if (model.getId() != null) {
                changedNoteColumns.add(model);
                idsToUpdate.add(model.getId());
            } else {
                newNoteColumns.add(model);
            }
            orders.add(model.getOrder());
            titles.add(model.getTitle());
        });

        final List<NoteColumn> databaseNoteColumns = noteColumnRepository.findAllByIdAndUserId(idsToUpdate, user.getId());

        noteColumns.addAll(this.updateSavedNotes(changedNoteColumns, databaseNoteColumns));

        noteColumns.addAll(this.createNewNotes(newNoteColumns, user));

        noteColumnRepository.saveAll(noteColumns);

        Long newNoteVersion = noteVersionService.updateColumnVersion();

        return new ResponseEntity<>(newNoteVersion, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateOrder(List<NoteColumnOrderRequestModel> models) {
        final User user = authService.getCurrentAuth().getUser();

        final List<String> titlesWithoutId = models.stream().filter(model -> model.getId() == null).map(model -> model.getColumnTitle()).collect(Collectors.toList());

        final List<Long> ids = new ArrayList<>();

        final List<NoteColumn> savedNotes = new ArrayList<>();

        if (titlesWithoutId.size() > 0) {
            savedNotes.addAll(noteColumnRepository.findByTitlesAndUserId(titlesWithoutId, user.getId()));
        }

        models.stream().forEach(m -> {
            if (m.getId() == null && !m.getColumnTitle().isBlank()) {
                List<NoteColumn> savedNoteSearch = savedNotes.stream().filter(sn -> sn.getTitle().equals(m.getColumnTitle())).collect(Collectors.toList());

                if (savedNoteSearch.size() > 0) {
                    m.setId(savedNotes.get(0).getId());
                }
            }

            ids.add(m.getId());
        });

        final List<Integer> orders = models.stream()
                .sorted(Comparator.comparing(NoteColumnOrderRequestModel::getId))
                .map(m -> m.getOrder())
                .collect(Collectors.toList());

        final Set<Integer> uniqueOrders = new HashSet<>(orders);

        if (orders.size() != uniqueOrders.size()) {
            return new ResponseEntity<>("Não podem haver itens com ordens iguais.", HttpStatus.BAD_REQUEST);
        }

        final List<NoteColumn> noteColumns = noteColumnRepository.findAllByIdOrderByIdAsc(ids, user.getId());

        if (noteColumns.size() != ids.size()) {
            return new ResponseEntity<>(
                    "Nem todos os itens foram encontrados, encontramos " + noteColumns.size() + " item de um total de  " + ids.size() + " buscados.",
                    HttpStatus.OK);
        }

        IntStream.range(0, noteColumns.size())
                .forEach(i ->
                        noteColumns.get(i).setOrder(orders.get(i))
                );


        noteColumnRepository.saveAll(noteColumns);

        Long newNoteVersion = noteVersionService.updateColumnVersion();

        return new ResponseEntity<>(newNoteVersion, HttpStatus.OK);
    }

    @Override
    public List<NoteColumn> findAllByUserIdAndTitle(List<String> titles, Long userId) {
        return noteColumnRepository.findByTitlesAndUserId(titles, userId);
    }

    @Override
    public NoteColumn findOneByUserIdAndTitle(String title, Long userId) {
        Optional<NoteColumn> search = noteColumnRepository.findByTitleAndUserId(title, userId);

        if (search.isPresent()) {
            return search.get();
        }

        return null;
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

    private List<NoteColumn> updateSavedNotes(List<NoteColumnSaveRequestModel> models, List<NoteColumn> notes) {
        final List<NoteColumn> updatedNotes = new ArrayList<>();
        final Date now = new Date();

        for (NoteColumnSaveRequestModel model : models)  {
            Optional<NoteColumn> noteColumnSearch = notes.stream().filter(n -> n.getId() == model.getId()).findFirst();

            if (noteColumnSearch.isPresent()) {
                NoteColumn noteColumn = noteColumnSearch.get();

                noteColumn.setTitle(model.getTitle());

                noteColumn.setLastUpdate(now);

                noteColumn.setOrder(model.getOrder());

                updatedNotes.add(noteColumn);
            }
        }

        return updatedNotes;
    }

    private List<NoteColumn> createNewNotes(List<NoteColumnSaveRequestModel> models, User user) {
        final List<NoteColumn> newNoteColumns = new ArrayList<>();
        final Date now = new Date();

        for(NoteColumnSaveRequestModel model : models) {
            NoteColumn noteColumn = new NoteColumn();
            noteColumn.setLastUpdate(now);
            noteColumn.setOrder(model.getOrder());
            noteColumn.setTitle(model.getTitle());
            noteColumn.setUser(user);

            newNoteColumns.add(noteColumn);
        }

        return newNoteColumns;
    }

}
