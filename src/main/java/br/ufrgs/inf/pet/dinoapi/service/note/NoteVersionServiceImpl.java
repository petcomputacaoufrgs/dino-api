package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.note.Note;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteVersion;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.repository.note.NoteVersionRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.model.alert_update.note.*;
import br.ufrgs.inf.pet.dinoapi.websocket.service.alert_update.queue.AlertUpdateQueueServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    @Autowired
    public NoteVersionServiceImpl(AuthServiceImpl authService, NoteVersionRepository noteVersionRepository, AlertUpdateQueueServiceImpl alertUpdateQueueServiceImpl) {
        this.authService = authService;
        this.noteVersionRepository = noteVersionRepository;
        this.alertUpdateQueueServiceImpl = alertUpdateQueueServiceImpl;
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

        noteVersion.updateVersion();
        noteVersion.setLastNoteUpdate(new Date());
        noteVersion = noteVersionRepository.save(noteVersion);

        alertUpdateQueueServiceImpl.sendUpdateMessage(noteVersion.getNoteVersion(), WebSocketDestinationsEnum.ALERT_NOTE_UPDATE);

        return noteVersion.getNoteVersion();
    }

    @Override
    public void updateNoteOrder(List<Note> noteList) {
        NoteOrderUpdateModel model = new NoteOrderUpdateModel();
        model.setItems(noteList.stream().map(NoteOrderItemUpdateModel::new).collect(Collectors.toList()));

        try {
            alertUpdateQueueServiceImpl.sendUpdateObjectMessage(model, WebSocketDestinationsEnum.ALERT_NOTE_ORDER_UPDATE);
        } catch (JsonProcessingException e) {
            this.updateColumnVersion();
        }
    }

    @Override
    public Long updateNoteVersionDelete(Long id) {
        List<Long> idList = new ArrayList<>();
        idList.add(id);
        return this.updateNoteVersionDelete(idList);
    }

    @Override
    public Long updateNoteVersionDelete(List<Long> idList) {
        NoteVersion noteVersion = this.getOrCreateWithoutSaveNoteVersion();

        noteVersion.updateColumnVersion();
        noteVersion.setLastColumnUpdate(new Date());
        noteVersion = noteVersionRepository.save(noteVersion);

        NoteDeleteModel model = new NoteDeleteModel();
        model.setNewVersion(noteVersion.getColumnVersion());
        model.setIdList(idList);

        try {
            alertUpdateQueueServiceImpl.sendUpdateObjectMessage(model, WebSocketDestinationsEnum.ALERT_NOTE_DELETE);
        } catch (JsonProcessingException e) {
            this.updateNoteVersion();
        }

        return noteVersion.getColumnVersion();
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
        ColumnOrderUpdateModel model = new ColumnOrderUpdateModel();
        model.setItems(columnList.stream().map(ColumnOrderItemUpdateModel::new).collect(Collectors.toList()));

        try {
            alertUpdateQueueServiceImpl.sendUpdateObjectMessage(model, WebSocketDestinationsEnum.ALERT_NOTE_COLUMN_ORDER_UPDATE);
        } catch (JsonProcessingException e) {
            this.updateColumnVersion();
        }
    }

    @Override
    public Long updateColumnVersionDelete(Long id) {
        List<Long> idList = new ArrayList<>();
        idList.add(id);
        return this.updateColumnVersionDelete(idList);
    }

    @Override
    public Long updateColumnVersionDelete(List<Long> idList) {
        NoteVersion noteVersion = this.getOrCreateWithoutSaveNoteVersion();

        noteVersion.updateColumnVersion();
        noteVersion.setLastColumnUpdate(new Date());
        noteVersion = noteVersionRepository.save(noteVersion);

        ColumnDeleteModel model = new ColumnDeleteModel();
        model.setNewVersion(noteVersion.getColumnVersion());
        model.setIdList(idList);

        try {
            alertUpdateQueueServiceImpl.sendUpdateObjectMessage(model, WebSocketDestinationsEnum.ALERT_NOTE_COLUMN_DELETE);
        } catch (JsonProcessingException e) {
            this.updateNoteVersion();
        }

        return noteVersion.getColumnVersion();
    }

    private NoteVersion getVersion() {
        final User user = authService.getCurrentAuth().getUser();

        Optional<NoteVersion> noteVersionSearch = noteVersionRepository.findByUserId(user.getId());

        NoteVersion noteVersion = noteVersionSearch.orElseGet(() -> noteVersionRepository.save(this.createNoteVersion(user)));

        return noteVersion;
    }

    private NoteVersion getOrCreateWithoutSaveNoteVersion() {
        final User user = authService.getCurrentAuth().getUser();

        Optional<NoteVersion> noteVersionSearch = noteVersionRepository.findByUserId(user.getId());

        NoteVersion noteVersion = noteVersionSearch.orElseGet(() -> this.createNoteVersion(user));

        return noteVersion;
    }

    private NoteVersion createNoteVersion(User user) {
        NoteVersion noteVersion = new NoteVersion();

        noteVersion.setUser(user);
        noteVersion.setLastColumnUpdate(new Date());
        noteVersion.setLastNoteUpdate(new Date());
        noteVersion.setColumnVersion(noteVersion.DEFAULT_VERSION);
        noteVersion.setNoteVersion(noteVersion.DEFAULT_VERSION);

        return noteVersion;
    }
}
