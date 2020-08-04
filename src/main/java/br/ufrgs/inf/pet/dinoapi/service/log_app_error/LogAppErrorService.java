package br.ufrgs.inf.pet.dinoapi.service.log_app_error;

import br.ufrgs.inf.pet.dinoapi.model.log_app_error.LogAppErroListRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.log_app_error.LogAppErrorRequestModel;
import org.springframework.http.ResponseEntity;

public interface LogAppErrorService {

    ResponseEntity<?> save(LogAppErrorRequestModel model);

    ResponseEntity<?> saveAll(LogAppErroListRequestModel model);
}
