package br.ufrgs.inf.pet.dinoapi.controller.note;

import br.ufrgs.inf.pet.dinoapi.model.notes.*;
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
     * @param model Model {@link NoteSaveRequestModel} com os dadso necessários
     * @return Em caso de sucesso retorna a model {@link NoteSaveResponseModel}
     * em caso de falha retorna a mensagem com o erro
     **/
    ResponseEntity<?> save(NoteColumnSaveRequestModel model);

    /**
     * Remove ums lista de colunas do usuário
     *
     * @param models Lista da model {@link NoteColumnDeleteRequestModel}
     * @return Em caso de sucesso retorna a nova versão das colunas do usuário
     */
    ResponseEntity<Long> deleteAll(List<NoteColumnDeleteRequestModel> models);

    /**
     * Remove permanentemente uma coluna
     *
     * @param model do tipo {@link NoteColumnDeleteRequestModel}
     * @return Em caso de sucesso retorna a nova versão das colunas
     **/
    ResponseEntity<Long> delete(NoteColumnDeleteRequestModel model);

    /**
     * Atualiza e cria (caso não exista) uma lista de colunas
     *
     * @param models Lista do tipo {@link NoteColumnSaveRequestModel}
     * @return Em caso de sucesso retorna a nova versão das colunas
     */
    ResponseEntity<Long> updateAll(List<NoteColumnSaveRequestModel> models);

    /**
     *  Atualiza a ordem das colunas
     *
     * @param models Lista do tipo {@link NoteOrderRequestModel}
     * @return Em caso de sucesso retorna a nova versão das colunas, em caso de erro retorna a mensagem do erro
     **/
    ResponseEntity<?> updateOrder(List<NoteColumnOrderRequestModel> models);

    /**
     * Retorna a versão das anotações do usuário
     *
     * @return Versão das anotações
     **/
    ResponseEntity<Long> getVersion();
}
