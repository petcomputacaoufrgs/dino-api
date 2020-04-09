package br.ufrgs.inf.pet.dinoapi.controller.glossary;

import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossarySaveModel;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryUpdateModel;
import br.ufrgs.inf.pet.dinoapi.model.glossary_version.GlossaryVersionResponseModel;
import org.springframework.http.ResponseEntity;

/**
 * Controller para gerenciar os dados relacionados aos itens do glossário
 *
 * @author joao.silva
 */
public interface GlossaryController {

    /**
     * Recebe os items do glossário, valida suas informações e salva em banco.
     * Se já houver um item com o mesmo título não há nenhuma ação.
     * Não é possível adicionar um novo registro com o mesmo título de um já existente, caso isto ocorra não há ação.
     *
     * @param glossarySaveModel - Model com os dados para a criação de items do glossário
     * @return lista com os itens salvos com sucesso ou erro
     */
    ResponseEntity<GlossaryResponseModel> save(GlossarySaveModel glossarySaveModel);

    /**
     * Recebe itens do glossário, verifica sua existencia e atualiza seus dados.
     *
     * @param glossaryUpdateModel - Model com os dados para atualização de itens do glossário
     * @return lista com os itens atualizados com sucesso ou erro
     */
    ResponseEntity<GlossaryResponseModel> update(GlossaryUpdateModel glossaryUpdateModel);

    /**
     * Retorna todos os itens do glossário existentes
     *
     * @return retorna todos os dados do glossario ativos (exists)
     */
    ResponseEntity<GlossaryResponseModel> get();

    /**
     * Retorna a versão atual do glossário
     *
     * @return retorna a versão atual na model: {@link GlossaryVersionResponseModel}
     */
    ResponseEntity<?> getVersion();

}
