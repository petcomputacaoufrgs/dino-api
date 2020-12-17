package br.ufrgs.inf.pet.dinoapi.service.note;

import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import br.ufrgs.inf.pet.dinoapi.exception.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryItemDataModel;
import br.ufrgs.inf.pet.dinoapi.model.note.NoteColumnDataModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.repository.note.NoteColumnRepository;
import br.ufrgs.inf.pet.dinoapi.repository.note.NoteRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.GenericQueueMessageServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.synchronizable.SynchronizableQueueMessageServiceImpl;
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
    public NoteColumn convertModelToEntity(NoteColumnDataModel model, User user) throws ConvertModelToEntityException {
        final NoteColumn noteColumn = new NoteColumn();
        noteColumn.setOrder(model.getOrder());
        noteColumn.setTitle(model.getTitle());
        noteColumn.setUser(user);

        return noteColumn;
    }

    @Override
    public void updateEntity(NoteColumn entity, NoteColumnDataModel model) throws ConvertModelToEntityException {
        entity.setTitle(model.getTitle());
        entity.setOrder(model.getOrder());
    }

    @Override
    public Optional<NoteColumn> getEntityByIdAndUser(Long id, User user) {
        return this.repository.findByIdAndUserId(id, user.getId());
    }

    @Override
    public List<NoteColumn> getEntitiesByUserId(User user) {
        return this.repository.findAllByUserId(user.getId());
    }

    @Override
    public List<NoteColumn> getEntitiesByIdsAndUserId(List<Long> ids, User user) {
        return this.repository.findAllByIdAndUserId(ids, user.getId());
    }

    @Override
    public List<NoteColumn> getEntitiesByUserIdExceptIds(User user, List<Long> ids) {
        return this.repository.findAllByUserIdExceptIds(user.getId(), ids);
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
