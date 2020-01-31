package br.ufrgs.inf.pet.dinoapi.controller.glossary;

import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryModel;
import org.springframework.http.ResponseEntity;

/**
 * Controller para gerenciar os dados relacionados aos itens do glossário
 *
 * @author joao.silva
 */
public interface GlossaryController {

    /**
     * Ao receber o model do item de glossário valida suas informações e salva em banco.
     * Se já houver um item com o mesmo título e os dados não conterem o id do registro não há nenhuma ação.
     * Para modificar itens existentes é necessário enviar o ID junto!
     * Não é possível adicionar um novo registro com o mesmo título de um já existente, caso isto ocorra não há ação.
     *
     * @param glossaryModel - Model com os dados para a criação de um item do glossário
     * @return lista com os itens salvos com sucesso ou erro
     */
    ResponseEntity<?> save(GlossaryModel glossaryModel);

    /**
     * Retorna a versão atual do glossário
     *
     * @return retorna a versão atual na model: {@link br.ufrgs.inf.pet.dinoapi.model.glossary_version.GlossaryVersionModel}
     */
    ResponseEntity<?> getGlossaryVersion();

    /**
     * Retorna todos os itens do glossário existentes
     *
     * @return retorna todos os dados do glossario
     */
    ResponseEntity<?> getGlossary();

}
