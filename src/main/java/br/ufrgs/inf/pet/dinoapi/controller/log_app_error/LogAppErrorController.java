package br.ufrgs.inf.pet.dinoapi.controller.log_app_error;

import br.ufrgs.inf.pet.dinoapi.model.log_app_error.LogAppErroListRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.log_app_error.LogAppErrorRequestModel;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpServletRequest;

public interface LogAppErrorController {
    /**
     * Save a app log
     * @param model Log data model
     * @param httpServletRequest Http request with client info
     * @return Void http response with status OK
     */
    ResponseEntity<Void> save(LogAppErrorRequestModel model, HttpServletRequest httpServletRequest);

    /**
     * Save a list of app logs
     * @param model Data model with all logs
     * @param httpServletRequest http request with client info
     * @return Void http response with status OK
     */
    ResponseEntity<Void> saveAll(LogAppErroListRequestModel model, HttpServletRequest httpServletRequest);

}
