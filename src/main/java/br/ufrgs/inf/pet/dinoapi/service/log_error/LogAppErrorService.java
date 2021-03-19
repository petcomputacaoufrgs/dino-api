package br.ufrgs.inf.pet.dinoapi.service.log_error;

import br.ufrgs.inf.pet.dinoapi.model.log_app_error.LogAppErroListRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.log_app_error.LogAppErrorRequestModel;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

public interface LogAppErrorService {

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

    /**
     * Delete all logs with date before the parameter
     * @param deadline Deadline date for remove logs
     */
    void deleteOldItems(LocalDateTime deadline);
}
