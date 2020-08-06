package br.ufrgs.inf.pet.dinoapi.controller.log_app_error;

import br.ufrgs.inf.pet.dinoapi.model.log_app_error.LogAppErroListRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.log_app_error.LogAppErrorRequestModel;
import org.springframework.http.ResponseEntity;

public interface LogAppErrorController {

    /**
     * Recebe as informações de um erro e salva no banco de dados
     *
     * @param model - Model com os dados do erro do tipo {@link LogAppErrorRequestModel}
     * @return Mensagem de erro ou sucesso
     */
    ResponseEntity<?> save(LogAppErrorRequestModel model);

    /**
     * Recebe as informações de uma lista de erros e salva no banco de dados
     *
     * @param model - Model com os dados do erro do tipo {@link LogAppErroListRequestModel}
     * @return Mensagem de erro ou sucesso
     */
    ResponseEntity<?> saveAll(LogAppErroListRequestModel model);

}
