package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.note.Note;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteVersion;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.repository.note.NoteVersionRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.model.note.*;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.alert_update.AlertUpdateQueueServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.GenericQueueMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteVersionServiceImpl implements NoteVersionService {

    private final AuthServiceImpl authService;

    private final NoteVersionRepository noteVersionRepository;

    private final AlertUpdateQueueServiceImpl alertUpdateQueueServiceImpl;

    private final GenericQueueMessageServiceImpl genericQueueMessageService;

    @Autowired
    public NoteVersionServiceImpl(AuthServiceImpl authService, NoteVersionRepository noteVersionRepository, AlertUpdateQueueServiceImpl alertUpdateQueueServiceImpl, GenericQueueMessageServiceImpl genericQueueMessageService) {
        this.authService = authService;
        this.noteVersionRepository = noteVersionRepository;
        this.alertUpdateQueueServiceImpl = alertUpdateQueueServiceImpl;
        this.genericQueueMessageService = genericQueueMessageService;
    }

    @Override
    public ResponseEntity<Long> getNoteVersion() {
        final NoteVersion noteVersion = this.getVersion();

        return new ResponseEntity<>(noteVersion.getNoteVersion(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> getNoteColumnVersion() {
        final NoteVersion noteVersion = this.getVersion();

        return new ResponseEntity<>(noteVersion.getColumnVersion(), HttpStatus.OK);
    }

    @Override
    public Long updateNoteVersion() {
        NoteVersion noteVersion = this.getOrCreateWithoutSaveNoteVersion();

        noteVersion.updateNoteVersion();
        noteVersion.setLastNoteUpdate(new Date());
        noteVersion = noteVersionRepository.save(noteVersion);

        alertUpdateQueueServiceImpl.sendUpdateMessage(noteVersion.getNoteVersion(), WebSocketDestinationsEnum.ALERT_NOTE_UPDATE);

        return noteVersion.getNoteVersion();
    }

    @Override
    public void updateNoteOrder(List<Note> noteList) {
        final NoteOrderUpdateModel model = new NoteOrderUpdateModel();
        model.setItems(noteList.stream().map(NoteOrderItemUpdateModel::new).collect(Collectors.toList()));

        genericQueueMessageService.sendObjectMessage(model, WebSocketDestinationsEnum.ALERT_NOTE_ORDER_UPDATE);
    }

    @Override
    public Long updateNoteVersionDelete(Long id) {
        final List<Long> idList = new ArrayList<>();
        idList.add(id);
        return this.updateNoteVersionDelete(idList);
    }

    @Override
    public Long updateNoteVersionDelete(List<Long> idList) {
        NoteVersion noteVersion = this.getOrCreateWithoutSaveNoteVersion();

        noteVersion.updateNoteVersion();
        noteVersion.setLastNoteUpdate(new Date());
        noteVersion = noteVersionRepository.save(noteVersion);

        final NoteDeleteModel model = new NoteDeleteModel();
        model.setNewVersion(noteVersion.getNoteVersion());
        model.setIdList(idList);

        genericQueueMessageService.sendObjectMessage(model, WebSocketDestinationsEnum.ALERT_NOTE_DELETE);

        return noteVersion.getNoteVersion();
    }

    @Override
    public Long updateColumnVersion() {
        NoteVersion noteVersion = this.getOrCreateWithoutSaveNoteVersion();

        noteVersion.updateColumnVersion();
        noteVersion.setLastColumnUpdate(new Date());
        noteVersion = noteVersionRepository.save(noteVersion);

        alertUpdateQueueServiceImpl.sendUpdateMessage(noteVersion.getColumnVersion(), WebSocketDestinationsEnum.ALERT_NOTE_COLUMN_UPDATE);

        return noteVersion.getColumnVersion();
    }

    @Override
    public void updateColumnOrder(List<NoteColumn> columnList) {
        final ColumnOrderUpdateModel model = new ColumnOrderUpdateModel();
        model.setItems(columnList.stream().map(ColumnOrderItemUpdateModel::new).collect(Collectors.toList()));

        genericQueueMessageService.sendObjectMessage(model, WebSocketDestinationsEnum.ALERT_NOTE_COLUMN_ORDER_UPDATE);
    }

    @Override
    public Long updateColumnVersionDelete(Long id) {
        final List<Long> idList = new ArrayList<>();
        idList.add(id);
        return this.updateColumnVersionDelete(idList);
    }

    @Override
    public Long updateColumnVersionDelete(List<Long> idList) {
        NoteVersion noteVersion = this.getOrCreateWithoutSaveNoteVersion();

        noteVersion.updateColumnVersion();
        noteVersion.setLastColumnUpdate(new Date());
        noteVersion = noteVersionRepository.save(noteVersion);

        final ColumnDeleteModel model = new ColumnDeleteModel();
        model.setNewVersion(noteVersion.getColumnVersion());
        model.setIdList(idList);

        genericQueueMessageService.sendObjectMessage(model, WebSocketDestinationsEnum.ALERT_NOTE_COLUMN_DELETE);

        return noteVersion.getColumnVersion();
    }

    @Override
    public NoteVersion getVersion() {
        final User user = authService.getCurrentUser();

        final Optional<NoteVersion> noteVersionSearch = noteVersionRepository.findByUserId(user.getId());

        final NoteVersion noteVersion = noteVersionSearch.orElseGet(() -> noteVersionRepository.save(this.createNoteVersion(user)));

        return noteVersion;
    }

    private NoteVersion getOrCreateWithoutSaveNoteVersion() {
        final User user = authService.getCurrentUser();

        final Optional<NoteVersion> noteVersionSearch = noteVersionRepository.findByUserId(user.getId());

        final NoteVersion noteVersion = noteVersionSearch.orElseGet(() -> this.createNoteVersion(user));

        return noteVersion;
    }

    private NoteVersion createNoteVersion(User user) {
        final NoteVersion noteVersion = new NoteVersion();

        noteVersion.setUser(user);
        noteVersion.setLastColumnUpdate(new Date());
        noteVersion.setLastNoteUpdate(new Date());
        noteVersion.setColumnVersion(noteVersion.DEFAULT_VERSION);
        noteVersion.setNoteVersion(noteVersion.DEFAULT_VERSION);

        return noteVersion;
    }
}
