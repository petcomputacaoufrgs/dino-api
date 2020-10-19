package br.ufrgs.inf.pet.dinoapi.controller.note;

import br.ufrgs.inf.pet.dinoapi.model.note.delete.NoteColumnDeleteAllRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.delete.NoteColumnDeleteRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.get.NoteColumnResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.note.order.NoteColumnOrderAllRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.save.NoteColumnSaveRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.save.NoteSaveResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.note.sync.column.NoteColumnSyncRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.note.sync.column.NoteColumnSyncResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NoteColumnController {
    /**
     * Retorna todas as colunas do usuário
     *
     * @return Lista de Anotações do usuário no modelo {@link NoteColumnResponseModel}
     **/
    ResponseEntity<List<NoteColumnResponseModel>> getUserColumns();

    /**
     * Salva uma nova coluna
     *
     * @param model Model {@link NoteColumnSaveRequestModel} com os dadso necessários
     * @return Em caso de sucesso retorna a model {@link NoteSaveResponseModel}
     * em caso de falha retorna a mensagem com o erro
     **/
    ResponseEntity<?> save(NoteColumnSaveRequestModel model);

    /**
     * Remove ums lista de colunas do usuário
     *
     * @param model Model do tipo {@link NoteColumnDeleteAllRequestModel}
     * @return Em caso de sucesso retorna a nova versão das colunas do usuário
     */
    ResponseEntity<Long> deleteAll(NoteColumnDeleteAllRequestModel model);

    /**
     * Remove permanentemente uma coluna
     *
     * @param model do tipo {@link NoteColumnDeleteRequestModel}
     * @return Em caso de sucesso retorna a nova versão das colunas
     **/
    ResponseEntity<Long> delete(NoteColumnDeleteRequestModel model);

    /**
     * Sincroniza colunas não salvas individualmente com o servidor
     *
     * @param model Model do tipo {@link NoteColumnSyncResponse}
     * @return Em caso de sucesso retorna um objeto do tipo {@link NoteColumnSyncResponse}
     */
    ResponseEntity<NoteColumnSyncResponse> sync(NoteColumnSyncRequestModel model);

    /**
     *  Atualiza a ordem das colunas
     *
     * @param model do tipo {@link NoteColumnOrderAllRequestModel}
     * @return Em caso de sucesso retorna a nova versão das colunas, em caso de erro retorna a mensagem do erro
     **/
    ResponseEntity<?> updateOrder(NoteColumnOrderAllRequestModel model);

    /**
     * Retorna a versão das anotações do usuário
     *
     * @return Versão das anotações
     **/
    ResponseEntity<Long> getVersion();
}
