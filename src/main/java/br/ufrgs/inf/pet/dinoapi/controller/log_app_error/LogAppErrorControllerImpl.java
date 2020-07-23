package br.ufrgs.inf.pet.dinoapi.controller.log_app_error;

import br.ufrgs.inf.pet.dinoapi.model.log_app_error.LogAppErroListRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.log_app_error.LogAppErrorRequestModel;
import br.ufrgs.inf.pet.dinoapi.service.log_app_error.LogAppErrorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

@Controller
public class LogAppErrorControllerImpl implements LogAppErrorController {

    private final LogAppErrorServiceImpl logAppErrorService;

    @Autowired
    public LogAppErrorControllerImpl(LogAppErrorServiceImpl logAppErrorService) {
        this.logAppErrorService = logAppErrorService;
    }

    @Override
    @PostMapping("log_app_error/")
    public ResponseEntity<?> save(@Valid @RequestBody LogAppErrorRequestModel model) {
        return logAppErrorService.save(model);
    }

    @Override
    @PostMapping("log_app_error/all/")
    public ResponseEntity<?> saveAll(@Valid @RequestBody LogAppErroListRequestModel model) {
        return logAppErrorService.saveAll(model);
    }
}
