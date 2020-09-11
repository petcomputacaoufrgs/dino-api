package br.ufrgs.inf.pet.dinoapi.controller.note;

import br.ufrgs.inf.pet.dinoapi.entity.notes.NoteTag;
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
     * @param model Model {@link NoteSaveRequestRequestModel} com os dadso necessários
     * @return Em caso de sucesso retorna a model {@link NoteSaveResponseModel}
     * em caso de falha retorna a mensagem com o erro
     **/
    ResponseEntity<?> saveNewNote(NoteSaveRequestRequestModel model);

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
     * Salva uma nova lista de anotações
     *
     * @param models Lista do tipo {@link NoteSaveRequestRequestModel}
     * @return Em caso de sucesso retorna a nova versão das anotações
     */
    ResponseEntity<Long> saveAll(List<NoteSaveRequestRequestModel> models);

    /**
     * Atualiza e cria (caso não exista) uma lista de anotações
     *
     * @param models Lista do tipo {@link NoteUpdateRequestModel}
     * @return Em caso de sucesso retorna a nova versão das anotações
     */
    ResponseEntity<Long> updateAll(List<NoteUpdateRequestModel> models);

    /**
     *  Atualiza a ordem das anotações
     *
     * @param models Lista do tipo {@link NoteOrderRequestModel}
     * @return Em caso de sucesso retorna a nova versão das anotações, em caso de erro retorna a mensagem do erro
     **/
    ResponseEntity<?> updateNotesOrder(List<NoteOrderRequestModel> models);

    /**
     * Atualiza a pergunta e as tags da anotação
     *
     * @param model Model do tipo {@link NoteQuestionRequestModel}
     * @return Em caso de sucesso retorna a nova versão das anotações, em caso de erro retorna a mensagem do erro
     **/
    ResponseEntity<?> updateNoteQuestion(NoteQuestionRequestModel model);

    /**
     * Atualiza a resposta
     *
     * @param model Model com os dados para atualizar a resposta
     * @return Em caso de sucesso retorna a nova versão das anotações, em caso de erro retorna a mensagem do erro
     **/
    ResponseEntity<?> updateNoteAnswer(NoteAnswerRequestModel model);

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
