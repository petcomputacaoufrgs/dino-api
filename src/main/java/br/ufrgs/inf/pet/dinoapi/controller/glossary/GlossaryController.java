package br.ufrgs.inf.pet.dinoapi.controller.glossary;

import br.ufrgs.inf.pet.dinoapi.entity.GlossaryItem;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossarySaveRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryUpdateRequestModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GlossaryController {

    /**
     * Recebe os items do glossário, valida suas informações e salva em banco.
     * Se já houver um item com o mesmo título não há nenhuma ação.
     * Não é possível adicionar um novo registro com o mesmo título de um já existente, caso isto ocorra não há ação.
     *
     * @param glossarySaveRequestModel - Model do tipo {@link GlossarySaveRequestModel}
     * @return Model do tipo {@link GlossaryResponseModel} com os itens salvos
     */
    ResponseEntity<GlossaryResponseModel> save(GlossarySaveRequestModel glossarySaveRequestModel);

    /**
     * Recebe itens do glossário, verifica sua existencia e atualiza seus dados.
     *
     * @param glossaryUpdateRequestModel - Model do tipo {@link GlossaryUpdateRequestModel}
     * @return Model do tipo {@link GlossaryResponseModel} com os dados atualizados ou mensagem do erro
     */
    ResponseEntity<?> update(GlossaryUpdateRequestModel glossaryUpdateRequestModel);

    /**
     * Retorna todos os itens do glossário existentes
     *
     * @return Lista do tipo {@link GlossaryItem} apenas com os itens ativos do glossário
     */
    ResponseEntity<List<GlossaryItem>> get();

    /**
     * Retorna a versão atual do glossário
     *
     * @return Retorna a versão atual do glossário
     */
    ResponseEntity<Long> getVersion();

}
