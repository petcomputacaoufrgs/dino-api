package br.ufrgs.inf.pet.dinoapi.controller.note;

import br.ufrgs.inf.pet.dinoapi.entity.NoteTag;
import br.ufrgs.inf.pet.dinoapi.model.notes.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface NoteController {
    /**
     * Retorna todas as anotações do usuário
     *
     * @return Lista de Anotações do usuário no modelo {@link NoteModel}
     **/
    ResponseEntity<List<NoteModel>> getUserNotes();

    /**
     * Salva uma nova anotação
     *
     * @param model Model {@link NoteSaveModel} com os dadso necessários
     * @return Em caso de sucesso retorna a model {@link NoteSaveResponseModel}
     * em caso de falha retorna a mensagem com o erro
     **/
    ResponseEntity<?> saveNewNote(NoteSaveModel model);

    /**
     * Remove ums lista de anotações do usuário
     *
     * @param models Lista da model {@link NoteDeleteModel}
     * @return Em caso de sucesso retorna a nova versão das anotações do usuário
     */
    ResponseEntity<Long> deleteAll(List<NoteDeleteModel> models);

    /**
     * Remove permanentemente uma anotação
     *
     * @param model do tipo {@link NoteDeleteModel}
     * @return Em caso de sucesso retorna a nova versão das anotações
     **/
    ResponseEntity<Long> deleteNote(NoteDeleteModel model);

    /**
     * Salva uma nova lista de anotações
     *
     * @param models Lista do tipo {@link NoteSaveModel}
     * @return Em caso de sucesso retorna a nova versão das anotações
     */
    ResponseEntity<Long> saveAll(List<NoteSaveModel> models);

    /**
     * Atualiza e cria (caso não exista) uma lista de anotações
     *
     * @param models Lista do tipo {@link NoteUpdateModel}
     * @return Em caso de sucesso retorna a nova versão das anotações
     */
    ResponseEntity<Long> updateAll(List<NoteUpdateModel> models);

    /**
     *  Atualiza a ordem das anotações
     *
     * @param models Lista do tipo {@link NoteOrderModel}
     * @return Em caso de sucesso retorna a nova versão das anotações, em caso de erro retorna a mensagem do erro
     **/
    ResponseEntity<?> updateNotesOrder(List<NoteOrderModel> models);

    /**
     * Atualiza a pergunta e as tags da anotação
     *
     * @param model Model do tipo {@link NoteQuestionModel}
     * @return Em caso de sucesso retorna a nova versão das anotações, em caso de erro retorna a mensagem do erro
     **/
    ResponseEntity<?> updateNoteQuestion(NoteQuestionModel model);

    /**
     * Atualiza a resposta
     *
     * @param model Model com os dados para atualizar a resposta
     * @return Em caso de sucesso retorna a nova versão das anotações, em caso de erro retorna a mensagem do erro
     **/
    ResponseEntity<?> updateNoteAnswer(NoteAnswerModel model);

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
