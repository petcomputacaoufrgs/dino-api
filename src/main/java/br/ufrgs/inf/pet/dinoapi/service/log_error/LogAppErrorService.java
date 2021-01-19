package br.ufrgs.inf.pet.dinoapi.service.log_error;

import br.ufrgs.inf.pet.dinoapi.model.log_app_error.LogAppErroListRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.log_app_error.LogAppErrorRequestModel;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface LogAppErrorService {

    ResponseEntity<?> save(LogAppErrorRequestModel model, HttpServletRequest httpServletRequest);

    ResponseEntity<?> saveAll(LogAppErroListRequestModel model, HttpServletRequest httpServletRequest);
}
