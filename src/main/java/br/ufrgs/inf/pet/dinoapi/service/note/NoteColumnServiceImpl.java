package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.model.note.NoteColumnDataModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.repository.note.NoteColumnRepository;
import br.ufrgs.inf.pet.dinoapi.repository.note.NoteRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.OAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.SynchronizableQueueMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteColumnServiceImpl extends SynchronizableServiceImpl<NoteColumn, Long, NoteColumnDataModel, NoteColumnRepository> {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteColumnServiceImpl(NoteColumnRepository noteColumnRepository, OAuthServiceImpl authService, NoteRepository noteRepository,
                                 SynchronizableQueueMessageServiceImpl<Long, NoteColumnDataModel> synchronizableQueueMessageService,
                                 ClockServiceImpl clockService, LogAPIErrorServiceImpl logAPIErrorService) {
        super(noteColumnRepository, authService, clockService, synchronizableQueueMessageService, logAPIErrorService);
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
    public Optional<NoteColumn> findEntityByIdThatUserCanRead(Long id, Auth auth) throws AuthNullException {
        return this.findByIdAndUser(id, auth);
    }

    @Override
    public Optional<NoteColumn> findEntityByIdThatUserCanEdit(Long id, Auth auth) throws AuthNullException {
        return this.findByIdAndUser(id, auth);
    }

    private Optional<NoteColumn> findByIdAndUser(Long id, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findByIdAndUserId(id, auth.getUser().getId());
    }

    @Override
    public List<NoteColumn> findEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByUserId(auth.getUser().getId());
    }

    @Override
    public List<NoteColumn> findEntitiesByIdThatUserCanEdit(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByIdAndUserId(ids, auth.getUser().getId());
    }

    @Override
    public List<NoteColumn> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByUserIdExcludingIds(auth.getUser().getId(), ids);
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.NOTE_COLUMN;
    }

    @Override
    public boolean shouldDelete(NoteColumn noteColumn, SynchronizableDeleteModel<Long> model) {
        Integer notesCount = noteRepository
                .countByNoteColumnAndLastUpdateGreaterOrEqual(noteColumn.getId(), model.getLastUpdate().toLocalDateTime());

        return notesCount == 0;
    }
}
