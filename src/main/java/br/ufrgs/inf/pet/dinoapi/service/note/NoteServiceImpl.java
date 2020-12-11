package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.constants.NoteConstants;
import br.ufrgs.inf.pet.dinoapi.entity.note.Note;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.note.NoteDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.note.NoteRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.GenericQueueMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteServiceImpl extends SynchronizableServiceImpl<Note, Long, NoteDataModel, NoteRepository> {

    private final NoteColumnServiceImpl noteColumnService;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository, AuthServiceImpl authService,
                           GenericQueueMessageServiceImpl genericQueueMessageService,
                           NoteColumnServiceImpl noteColumnService) {
        super(noteRepository, authService, genericQueueMessageService);
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
    public Note convertModelToEntity(NoteDataModel model, User user) throws ConvertModelToEntityException {
        final Optional<NoteColumn> noteColumn = noteColumnService.getEntityByIdAndUser(model.getColumnId(), user);
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

    @Override
    public void updateEntity(Note entity, NoteDataModel model) throws ConvertModelToEntityException {
        final User user = this.authService.getCurrentUser();
        final Optional<NoteColumn> noteColumn = noteColumnService.getEntityByIdAndUser(model.getColumnId(), user);

        if (noteColumn.isPresent()) {
            entity.setAnswer(model.getAnswer());
            entity.setTags(model.getTags());
            entity.setOrder(model.getOrder());
            entity.setNoteColumn(noteColumn.get());
            entity.setQuestion(model.getQuestion());
        } else {
            throw new ConvertModelToEntityException(NoteConstants.INVALID_COLUMN);
        }
    }

    @Override
    public Optional<Note> getEntityByIdAndUser(Long id, User user) {
        return this.repository.findByIdAndUserId(id, user.getId());
    }

    @Override
    public List<Note> getEntitiesByUserId(User user) {
        return this.repository.findAllByUserId(user.getId());
    }

    @Override
    public List<Note> getEntitiesByIdsAndUserId(List<Long> ids, User user) {
        return this.repository.findAllByIdAndUserId(ids, user.getId());
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
