package br.ufrgs.inf.pet.dinoapi.controller.note;

import br.ufrgs.inf.pet.dinoapi.entity.NoteTag;
import br.ufrgs.inf.pet.dinoapi.model.notes.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface NoteController {
    /**
     * Retorna todas as anotações do usuário
     **/
    ResponseEntity<List<NoteModel>> getUserNotes();

    /**
     * Salva uma nova anotação
     *
     * @param model Model representando a nova entidade Note
     * @return Em caso de sucesso retorna a nova versão das anotações e as novas tags adicionadas
     **/
    ResponseEntity<?> saveNewNote(NoteSaveModel model);

    /**
     * Remove ums lista de anotações do usuário
     * @param models Lista de models para remoção
     * @return Em caso de sucesso retorna a nova versão das anotações do usuário
     */
    ResponseEntity<Long> deleteAll(List<NoteDeleteModel> models);

    /**
     * Remove permanentemente uma anotação
     *
     * @param model objeto com o id da anotação a ser removida
     * @return Em caso de sucesso retorna a nova versão das anotações
     **/
    ResponseEntity<Long> deleteNote(NoteDeleteModel model);

    /**
     * Salva uma nova lista de anotações
     *
     * @param models lista com as informações das anotações a serem salvas
     *
     * @return Em caso de sucesso retorna a nova versão das anotações
     */
    ResponseEntity<?> saveAll(List<NoteSaveModel> models);

    /**
     * Atualiza e cria (caso não exista) uma lista de anotações
     *
     * @param models lista com as informações das anotações a serem atualizadas
     *
     * @return Em caso de sucesso retorna a nova versão das anotações
     */
    ResponseEntity<?> updateAll(List<NoteUpdateModel> models);

    /**
     *  Atualiza a ordem das anotações
     *
     * @param models Lista de models com os dados necessário para a atualização
     * @return Em caso de sucesso retorna a nova versão das anotações
     **/
    ResponseEntity<?> updateNotesOrder(List<NoteOrderModel> models);

    /**
     * Atualiza a pergunta e as tags
     *
     * @param model Model com os dados para atualizar a pergunta e as tags
     * @return Em caso de sucesso retorna a nova versão das anotações
     **/
    ResponseEntity<?> updateNoteQuestion(NoteQuestionModel model);

    /**
     * Atualiza a resposta
     *
     * @param model Model com os dados para atualizar a resposta
     * @return Em caso de sucesso retorna a nova versão das anotações
     **/
    ResponseEntity<?> updateNoteAnswer(NoteAnswerModel model);

    /**
     * Retorna todas as tags salvas relacionadas a anotações do usuário
     *
     * @return Lista com as tags
     **/
    ResponseEntity<List<NoteTag>> getTags();

    /**
     * Retorna a versão das anotações do usuário
     **/
    ResponseEntity<Long> getVersion();
}
