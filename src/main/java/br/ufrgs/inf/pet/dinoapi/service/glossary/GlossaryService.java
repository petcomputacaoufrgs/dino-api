package br.ufrgs.inf.pet.dinoapi.service.glossary;

import br.ufrgs.inf.pet.dinoapi.entity.GlossaryItem;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossarySaveModel;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryUpdateModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Service para gerenciar os dados do glossário
 *
 * @author joao.silva
 */
public interface GlossaryService {
    /**
     * Recebe os items do glossário, valida suas informações e salva em banco.
     * Se já houver um item com o mesmo título não há nenhuma ação.
     * Não é possível adicionar um novo registro com o mesmo título de um já existente, caso isto ocorra não há ação.
     *
     * @param glossarySaveModel - Model com os dados para a criação de items do glossário
     * @return model com a versão do glossário atual e os itens salvos
     */
    ResponseEntity<GlossaryResponseModel> save(GlossarySaveModel glossarySaveModel);

    /**
     * Recebe itens do glossário, verifica sua existencia e atualiza seus dados.
     *
     * @param glossaryUpdateModel - Model com os dados para atualização de itens do glossário
     * @return lista com os itens atualizados com sucesso ou erro
     */
    ResponseEntity<?> update(GlossaryUpdateModel glossaryUpdateModel);

    /**
     * Retorna todos os itens do glossário existentes
     *
     * @return retorna todos os dados do glossario ativos (exists)
     */
    ResponseEntity<List<GlossaryItem>> get();
}
