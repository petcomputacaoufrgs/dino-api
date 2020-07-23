package br.ufrgs.inf.pet.dinoapi.controller.glossary;

import br.ufrgs.inf.pet.dinoapi.entity.GlossaryItem;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossarySaveModel;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryUpdateModel;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryVersionResponseModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GlossaryController {

    /**
     * Recebe os items do glossário, valida suas informações e salva em banco.
     * Se já houver um item com o mesmo título não há nenhuma ação.
     * Não é possível adicionar um novo registro com o mesmo título de um já existente, caso isto ocorra não há ação.
     *
     * @param glossarySaveModel - Model do tipo {@link GlossarySaveModel}
     * @return Model do tipo {@link GlossaryResponseModel} com os itens salvos
     */
    ResponseEntity<GlossaryResponseModel> save(GlossarySaveModel glossarySaveModel);

    /**
     * Recebe itens do glossário, verifica sua existencia e atualiza seus dados.
     *
     * @param glossaryUpdateModel - Model do tipo {@link GlossaryUpdateModel}
     * @return Model do tipo {@link GlossaryResponseModel} com os dados atualizados ou mensagem do erro
     */
    ResponseEntity<?> update(GlossaryUpdateModel glossaryUpdateModel);

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
