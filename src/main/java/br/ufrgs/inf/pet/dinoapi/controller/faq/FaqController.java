package br.ufrgs.inf.pet.dinoapi.controller.faq;

import br.ufrgs.inf.pet.dinoapi.model.faq.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FaqController {

    /**
     * Requisita uma FAQ pelo seu id utilizando a model {@link FaqIdModel}
     *
     * @return Objeto {@link FaqModel}
     */
    ResponseEntity<FaqModel> get(FaqIdModel model);

    /**
     * Requisita todas as FAQs
     *
     * @return Lista do tipo {@link FaqModel}
     */
    ResponseEntity<List<FaqModel>> getAll();

    /**
     * Salva uma FAQ pela model {@link FaqSaveRequestModel}
     *
     * @return Objeto {@link FaqModel}
     */
    ResponseEntity<FaqModel> save(FaqSaveRequestModel faqSaveRequestModel);

    /**
     * Salva uma lista de itens de uma FAQ pela model {@link FaqListSaveRequestModel}
     * Possui suporte para:
     *  - editar respostas de uma questão
     *  - adicionar novos itens a uma FAQ
     *
     * Não possui suporte para:
     *  - excluir itens de uma FAQ
     *
     * @return Lista do tipo {@link FaqModel}
     */
    ResponseEntity<List<FaqModel>> saveAll(FaqListSaveRequestModel model);

    /**
     * Requisita a lista de FAQs disponíveis
     *
     * @return Lista do tipo {@link FaqOptionModel}
     */
    ResponseEntity<List<FaqOptionModel>> getFaqOptions();

    /**
     * Edita uma FAQ pela model {@link FaqModel}
     *
     * @return Objeto {@link FaqModel}
     */
    ResponseEntity<FaqModel> editFaq(FaqModel model);

    /**
     * Retorna a versão da FAQ do usuário
     *
     * @return Objeto {@link FaqVersionModel}
     */
    ResponseEntity<FaqVersionModel> getFaqUserVersion();

    /**
     * Retorna a FAQ do usuário
     *
     * @return Objeto {@link FaqModel}
     */
    ResponseEntity<FaqModel> getFaqUser();

    /**
     * Salva a FAQ do usuário pelo objeto {@link FaqIdModel}
     *
     * @return ID da FAQ do usuário {@link Long}
     */
    ResponseEntity<Long> saveFaqUser(FaqIdModel model);

    /**
     * Salva uma sugestão de pergunta do usuário pela model {@link UserQuestionSaveRequestModel}
     *
     * @return Status de sucesso ou erro
     */
    ResponseEntity<?> saveFaqUserQuestion(UserQuestionSaveRequestModel model);

}
