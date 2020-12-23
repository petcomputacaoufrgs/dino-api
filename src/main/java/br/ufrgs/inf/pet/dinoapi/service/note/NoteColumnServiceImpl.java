package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.constants.AuthConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.note.NoteColumnDataModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.repository.note.NoteColumnRepository;
import br.ufrgs.inf.pet.dinoapi.repository.note.NoteRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.SynchronizableQueueMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class NoteColumnServiceImpl extends SynchronizableServiceImpl<NoteColumn, Long, Integer, NoteColumnDataModel, NoteColumnRepository> {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteColumnServiceImpl(NoteColumnRepository noteColumnRepository, AuthServiceImpl authService, NoteRepository noteRepository,
                                 SynchronizableQueueMessageServiceImpl<Long, Integer, NoteColumnDataModel> synchronizableQueueMessageService) {
        super(noteColumnRepository, authService, synchronizableQueueMessageService);
        this.noteRepository = noteRepository;
    }

    @Override
    public NoteColumnDataModel convertEntityToModel(NoteColumn entity) {
        final NoteColumnDataModel noteColumnDataModel = new NoteColumnDataModel();

        noteColumnDataModel.setTitle(entity.getTitle());
        noteColumnDataModel.setOrder(entity.getOrder());

        return noteColumnDataModel;
    }

    @Override
    public NoteColumn convertModelToEntity(NoteColumnDataModel model, Auth auth) throws AuthNullException {
        if (auth != null) {
            final NoteColumn noteColumn = new NoteColumn();
            noteColumn.setOrder(model.getOrder());
            noteColumn.setTitle(model.getTitle());
            noteColumn.setUser(auth.getUser());

            return noteColumn;
        }

        throw new AuthNullException();
    }

    @Override
    public void updateEntity(NoteColumn entity, NoteColumnDataModel model, Auth auth) {
        entity.setTitle(model.getTitle());
        entity.setOrder(model.getOrder());
    }

    @Override
    public Optional<NoteColumn> getEntityByIdAndUserAuth(Long id, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findByIdAndUserId(id, auth.getUser().getId());
    }

    @Override
    public List<NoteColumn> getEntitiesByUserAuth(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByUserId(auth.getUser().getId());
    }

    @Override
    public List<NoteColumn> getEntitiesByIdsAndUserAuth(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByIdAndUserId(ids, auth.getUser().getId());
    }

    @Override
    public List<NoteColumn> getEntitiesByUserAuthExceptIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByUserIdExceptIds(auth.getUser().getId(), ids);
    }

    @Override
    public WebSocketDestinationsEnum getUpdateWebSocketDestination() {
        return WebSocketDestinationsEnum.NOTE_COLUMN_UPDATE;
    }

    @Override
    public WebSocketDestinationsEnum getDeleteWebSocketDestination() {
        return WebSocketDestinationsEnum.NOTE_COLUMN_DELETE;
    }

    @Override
    public boolean shouldDelete(NoteColumn noteColumn, SynchronizableDeleteModel<Long> model) {
        Integer notesCount = noteRepository.countByNoteColumnAndLastUpdateGreaterOrEqual(noteColumn.getId(), model.getLastUpdate());

        return notesCount == 0;
    }
}
