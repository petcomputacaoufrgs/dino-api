package br.ufrgs.inf.pet.dinoapi.controller.note;

import br.ufrgs.inf.pet.dinoapi.entity.note.NoteTag;
import br.ufrgs.inf.pet.dinoapi.model.note.delete.NoteDeleteAllRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.delete.NoteDeleteRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.get.NoteResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.note.order.NoteOrderAllRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.save.NoteColumnSaveRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.save.NoteSaveRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.save.NoteSaveResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.note.sync.note.NoteSyncRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.sync.note.NoteSyncResponseModel;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface NoteController {
    /**
     * Retorna todas as anotações do usuário
     *
     * @return Lista de Anotações do usuário no modelo {@link NoteResponseModel}
     **/
    ResponseEntity<List<NoteResponseModel>> getUserNotes();

    /**
     * Salva uma nova anotação
     *
     * @param model Model {@link NoteColumnSaveRequestModel} com os dadso necessários
     * @return Em caso de sucesso retorna a model {@link NoteSaveResponseModel}
     * em caso de falha retorna a mensagem com o erro
     **/
    ResponseEntity<?> saveNote(NoteSaveRequestModel model);

    /**
     * Remove ums lista de anotações do usuário
     *
     * @param model do tipo {@link NoteDeleteAllRequestModel}
     * @return Em caso de sucesso retorna a nova versão das anotações do usuário
     */
    ResponseEntity<Long> deleteAll(NoteDeleteAllRequestModel model);

    /**
     * Remove permanentemente uma anotação
     *
     * @param model do tipo {@link NoteDeleteRequestModel}
     * @return Em caso de sucesso retorna a nova versão das anotações
     **/
    ResponseEntity<Long> deleteNote(NoteDeleteRequestModel model);

    /**
     * Sincroniza colunas não salvas individualmente com o servidor
     *
     * @param model do tipo {@link NoteSyncRequestModel}
     * @return Em caso de sucesso retorna o objeto {@link NoteSyncResponseModel}
     */
    ResponseEntity<NoteSyncResponseModel> sync(NoteSyncRequestModel model);

    /**
     *  Atualiza a ordem das anotações
     *
     * @param model do tipo {@link NoteOrderAllRequestModel}
     * @return Em caso de sucesso retorna a nova versão das anotações, em caso de erro retorna a mensagem do erro
     **/
    ResponseEntity<?> updateNotesOrder(NoteOrderAllRequestModel model);

    /**
     * Retorna todas as tags salvas relacionadas as anotações do usuário
     *
     * @return Lista com as tags do tipo {@link NoteTag}
     **/
    ResponseEntity<List<NoteTag>> getTags();

    /**
     * Retorna a versão das anotações do usuário
     *
     * @return Versão das anotações
     **/
    ResponseEntity<Long> getVersion();
}
