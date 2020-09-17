package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteVersion;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.repository.note.NoteVersionRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.model.alert_update.note.ColumnDeleteModel;
import br.ufrgs.inf.pet.dinoapi.websocket.model.alert_update.note.ColumnOrderItemUpdateModel;
import br.ufrgs.inf.pet.dinoapi.websocket.model.alert_update.note.ColumnOrderUpdateModel;
import br.ufrgs.inf.pet.dinoapi.websocket.model.alert_update.note.ColumnTitleUpdateModel;
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
    public ResponseEntity<Long> getVersion() {
        final NoteVersion noteVersion = this.getNoteVersion();

        return new ResponseEntity<>(noteVersion.getVersion(), HttpStatus.OK);
    }

    @Override
    public Long updateNoteVersion() {
        NoteVersion noteVersion = this.getOrCreateWithoutSaveNoteVersion();

        noteVersion.updateVersion();
        noteVersion.setLastUpdate(new Date());
        noteVersion = noteVersionRepository.save(noteVersion);

        alertUpdateQueueServiceImpl.sendUpdateMessage(noteVersion.getVersion(), WebSocketDestinationsEnum.ALERT_NOTE_UPDATE);

        return noteVersion.getVersion();
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
    public Long updateColumnVersionTitle(String newTitle, String oldTitle) {
        NoteVersion noteVersion = this.getOrCreateWithoutSaveNoteVersion();

        noteVersion.updateColumnVersion();
        noteVersion.setLastColumnUpdate(new Date());
        noteVersion = noteVersionRepository.save(noteVersion);

        ColumnTitleUpdateModel model = new ColumnTitleUpdateModel();
        model.setNewTitle(newTitle);
        model.setOldTitle(oldTitle);
        model.setNewVersion(noteVersion.getColumnVersion());
        model.setLastUpdate(noteVersion.getLastUpdate().getTime());

        try {
            alertUpdateQueueServiceImpl.sendUpdateObjectMessage(model, WebSocketDestinationsEnum.ALERT_NOTE_COLUMN_TITLE_UPDATE);
        } catch (JsonProcessingException e) {
            this.updateNoteVersion();
        }

        return noteVersion.getColumnVersion();
    }

    @Override
    public Long updateColumnVersionOrder(List<NoteColumn> columnList) {
        NoteVersion noteVersion = this.getOrCreateWithoutSaveNoteVersion();

        noteVersion.updateColumnVersion();
        noteVersion.setLastColumnUpdate(new Date());
        noteVersion = noteVersionRepository.save(noteVersion);

        ColumnOrderUpdateModel model = new ColumnOrderUpdateModel();
        model.setItems(columnList.stream().map(ColumnOrderItemUpdateModel::new).collect(Collectors.toList()));
        model.setNewVersion(noteVersion.getColumnVersion());

        try {
            alertUpdateQueueServiceImpl.sendUpdateObjectMessage(model, WebSocketDestinationsEnum.ALERT_NOTE_COLUMN_ORDER_UPDATE);
        } catch (JsonProcessingException e) {
            this.updateNoteVersion();
        }

        return noteVersion.getColumnVersion();
    }

    @Override
    public Long updateColumnVersionDelete(String title) {
        List<String> titleList = new ArrayList<>();
        titleList.add(title);
        return this.updateColumnVersionDelete(titleList);
    }

    @Override
    public Long updateColumnVersionDelete(List<String> titleList) {
        NoteVersion noteVersion = this.getOrCreateWithoutSaveNoteVersion();

        noteVersion.updateColumnVersion();
        noteVersion.setLastColumnUpdate(new Date());
        noteVersion = noteVersionRepository.save(noteVersion);

        ColumnDeleteModel model = new ColumnDeleteModel();
        model.setNewVersion(noteVersion.getColumnVersion());
        model.setTitleList(titleList);

        try {
            alertUpdateQueueServiceImpl.sendUpdateObjectMessage(model, WebSocketDestinationsEnum.ALERT_NOTE_COLUMN_DELETE);
        } catch (JsonProcessingException e) {
            this.updateNoteVersion();
        }

        return noteVersion.getColumnVersion();
    }

    private NoteVersion getNoteVersion() {
        final User user = authService.getCurrentAuth().getUser();

        Optional<NoteVersion> noteVersionSearch = noteVersionRepository.findByUserId(user.getId());

        NoteVersion noteVersion = noteVersionSearch.orElseGet(() -> noteVersionRepository.save(new NoteVersion(user)));

        return noteVersion;
    }

    private NoteVersion getOrCreateWithoutSaveNoteVersion() {
        final User user = authService.getCurrentAuth().getUser();

        Optional<NoteVersion> noteVersionSearch = noteVersionRepository.findByUserId(user.getId());

        NoteVersion noteVersion = noteVersionSearch.orElseGet(() -> new NoteVersion(user));

        return noteVersion;
    }
}
