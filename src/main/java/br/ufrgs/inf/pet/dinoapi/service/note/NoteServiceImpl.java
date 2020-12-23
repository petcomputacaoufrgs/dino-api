package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.constants.NoteConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.note.Note;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.note.NoteDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.note.NoteRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.SynchronizableQueueMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NoteServiceImpl extends SynchronizableServiceImpl<Note, Long, Integer, NoteDataModel, NoteRepository> {

    private final NoteColumnServiceImpl noteColumnService;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository, AuthServiceImpl authService, NoteColumnServiceImpl noteColumnService,
                           SynchronizableQueueMessageServiceImpl<Long, Integer, NoteDataModel> synchronizableQueueMessageService) {
        super(noteRepository, authService, synchronizableQueueMessageService);
        this.noteColumnService = noteColumnService;
    }


    @Override
    public NoteDataModel convertEntityToModel(Note entity) {
        final NoteDataModel noteDataModel = new NoteDataModel();
        noteDataModel.setOrder(entity.getOrder());
        noteDataModel.setQuestion(entity.getQuestion());
        noteDataModel.setAnswer(entity.getAnswer());
        noteDataModel.setColumnId(entity.getNoteColumn().getId());
        noteDataModel.setTags(entity.getTags());

        return noteDataModel;
    }

    @Override
    public Note convertModelToEntity(NoteDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        if (auth != null) {
            final Optional<NoteColumn> noteColumn =
                    noteColumnService.getEntityByIdAndUserAuth(model.getColumnId(), auth);
            if (noteColumn.isPresent()) {
                final Note note = new Note();
                note.setNoteColumn(noteColumn.get());
                note.setQuestion(model.getQuestion());
                note.setOrder(model.getOrder());
                note.setTags(model.getTags());
                note.setAnswer(model.getAnswer());

                return note;
            }

            throw new ConvertModelToEntityException(NoteConstants.INVALID_COLUMN);
        }

        throw new AuthNullException();
    }

    @Override
    public void updateEntity(Note entity, NoteDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        if (auth != null) {
            if (!entity.getNoteColumn().getId().equals(model.getColumnId())) {
                final Optional<NoteColumn> noteColumn =
                        noteColumnService.getEntityByIdAndUserAuth(model.getColumnId(), auth);

                if (noteColumn.isPresent()) {
                    entity.setNoteColumn(noteColumn.get());
                } else {
                    throw new ConvertModelToEntityException(NoteConstants.INVALID_COLUMN);
                }
            }

            entity.setAnswer(model.getAnswer());
            entity.setTags(model.getTags());
            entity.setOrder(model.getOrder());
            entity.setQuestion(model.getQuestion());
        } else {
            throw new AuthNullException();
        }
    }

    @Override
    public Optional<Note> getEntityByIdAndUserAuth(Long id, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findByIdAndUserId(id, auth.getUser().getId());
    }

    @Override
    public List<Note> getEntitiesByUserAuth(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByUserId(auth.getUser().getId());
    }

    @Override
    public List<Note> getEntitiesByIdsAndUserAuth(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByIdsAndUserId(ids, auth.getUser().getId());
    }

    @Override
    public List<Note> getEntitiesByUserAuthExceptIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return this.repository.findAllByUserIdExceptIds(auth.getUser().getId(), ids);
    }

    @Override
    public WebSocketDestinationsEnum getUpdateWebSocketDestination() {
        return WebSocketDestinationsEnum.NOTE_UPDATE;
    }

    @Override
    public WebSocketDestinationsEnum getDeleteWebSocketDestination() {
        return WebSocketDestinationsEnum.NOTE_DELETE;
    }
}
