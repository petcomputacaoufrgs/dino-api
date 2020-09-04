package br.ufrgs.inf.pet.dinoapi.controller.note;

import br.ufrgs.inf.pet.dinoapi.entity.NoteTag;
import br.ufrgs.inf.pet.dinoapi.model.notes.*;
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
     * @param model Model {@link NoteSaveRequestModel} com os dadso necessários
     * @return Em caso de sucesso retorna a model {@link NoteSaveResponseModel}
     * em caso de falha retorna a mensagem com o erro
     **/
    ResponseEntity<?> saveNote(NoteSaveRequestModel model);

    /**
     * Remove ums lista de anotações do usuário
     *
     * @param models Lista da model {@link NoteDeleteRequestModel}
     * @return Em caso de sucesso retorna a nova versão das anotações do usuário
     */
    ResponseEntity<Long> deleteAll(List<NoteDeleteRequestModel> models);

    /**
     * Remove permanentemente uma anotação
     *
     * @param model do tipo {@link NoteDeleteRequestModel}
     * @return Em caso de sucesso retorna a nova versão das anotações
     **/
    ResponseEntity<Long> deleteNote(NoteDeleteRequestModel model);

    /**
     * Atualiza e cria (caso não exista) uma lista de anotações
     *
     * @param models Lista do tipo {@link NoteSaveRequestModel}
     * @return Em caso de sucesso retorna a nova versão das anotações
     */
    ResponseEntity<Long> updateAll(List<NoteSaveRequestModel> models);

    /**
     *  Atualiza a ordem das anotações
     *
     * @param models Lista do tipo {@link NoteOrderRequestModel}
     * @return Em caso de sucesso retorna a nova versão das anotações, em caso de erro retorna a mensagem do erro
     **/
    ResponseEntity<?> updateNotesOrder(List<NoteOrderRequestModel> models);

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
