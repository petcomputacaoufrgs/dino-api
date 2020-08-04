package br.ufrgs.inf.pet.dinoapi.controller.log_app_error;

import br.ufrgs.inf.pet.dinoapi.model.log_app_error.LogAppErroListModel;
import br.ufrgs.inf.pet.dinoapi.model.log_app_error.LogAppErrorModel;
import org.springframework.http.ResponseEntity;

public interface LogAppErrorController {

    /**
     * Recebe as informações de um erro e salva no banco de dados
     *
     * @param model - Model com os dados do erro
     * @return mensagem e status de sucesso ou erro
     */
    ResponseEntity<?> save(LogAppErrorModel model);

    /**
     * Recebe as informações de uma lista de erros e salva no banco de dados
     *
     * @param model - Model com os dados do erro
     * @return mensagem e status de sucesso ou erro
     */
    ResponseEntity<?> saveAll(LogAppErroListModel model);

}
