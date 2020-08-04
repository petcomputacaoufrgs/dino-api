package br.ufrgs.inf.pet.dinoapi.controller.log_app_error;

import br.ufrgs.inf.pet.dinoapi.model.log_app_error.LogAppErroListModel;
import br.ufrgs.inf.pet.dinoapi.model.log_app_error.LogAppErrorModel;
import org.springframework.http.ResponseEntity;

public interface LogAppErrorController {

    /**
     * Recebe as informações de um erro e salva no banco de dados
     *
     * @param model - Model com os dados do erro do tipo {@link LogAppErrorModel}
     * @return Mensagem de erro ou sucesso
     */
    ResponseEntity<?> save(LogAppErrorModel model);

    /**
     * Recebe as informações de uma lista de erros e salva no banco de dados
     *
     * @param model - Model com os dados do erro do tipo {@link LogAppErroListModel}
     * @return Mensagem de erro ou sucesso
     */
    ResponseEntity<?> saveAll(LogAppErroListModel model);

}
