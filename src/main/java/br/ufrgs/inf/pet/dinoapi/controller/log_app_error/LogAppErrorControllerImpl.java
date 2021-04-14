package br.ufrgs.inf.pet.dinoapi.controller.log_app_error;

import br.ufrgs.inf.pet.dinoapi.model.log_app_error.LogAppErroListRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.log_app_error.LogAppErrorRequestModel;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAppErrorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class LogAppErrorControllerImpl implements LogAppErrorController {

    private final LogAppErrorServiceImpl logAppErrorService;

    @Autowired
    public LogAppErrorControllerImpl(LogAppErrorServiceImpl logAppErrorService) {
        this.logAppErrorService = logAppErrorService;
    }

    @Override
    @PostMapping("private/log_app_error/")
    public ResponseEntity<?> save(@Valid @RequestBody LogAppErrorRequestModel model, HttpServletRequest httpServletRequest) {
        return logAppErrorService.save(model, httpServletRequest);
    }

    @Override
    @PostMapping("private/log_app_error/all/")
    public ResponseEntity<?> saveAll(@Valid @RequestBody LogAppErroListRequestModel model, HttpServletRequest httpServletRequest) {
        return logAppErrorService.saveAll(model, httpServletRequest);
    }
}
