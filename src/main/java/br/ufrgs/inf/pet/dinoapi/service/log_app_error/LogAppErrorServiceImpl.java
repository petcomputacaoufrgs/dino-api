package br.ufrgs.inf.pet.dinoapi.service.log_app_error;

import br.ufrgs.inf.pet.dinoapi.entity.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.LogAppError;
import br.ufrgs.inf.pet.dinoapi.model.log_app_error.LogAppErroListRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.log_app_error.LogAppErrorRequestModel;
import br.ufrgs.inf.pet.dinoapi.repository.LogAppErrorRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogAppErrorServiceImpl implements LogAppErrorService {

    private final AuthServiceImpl authService;

    private final LogAppErrorRepository logAppErrorRepository;

    @Autowired
    public LogAppErrorServiceImpl(AuthServiceImpl authService, LogAppErrorRepository logAppErrorRepository) {
        this.authService = authService;
        this.logAppErrorRepository = logAppErrorRepository;
    }

    @Override
    public ResponseEntity<?> save(LogAppErrorRequestModel model, HttpServletRequest httpServletRequest) {
        Auth auth = authService.getCurrentAuth();

        final String userAgent = httpServletRequest.getHeader("User-Agent");

        LogAppError error = this.generateError(auth, model, userAgent);

        logAppErrorRepository.save(error);

        return new ResponseEntity<>("Log saved.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> saveAll(LogAppErroListRequestModel model, HttpServletRequest httpServletRequest) {
        Auth auth = authService.getCurrentAuth();

        final String userAgent = httpServletRequest.getHeader("User-Agent");

        List<LogAppError> items = model.getItems().stream().map(item ->
                this.generateError(auth, item, userAgent)).collect(Collectors.toList());

        logAppErrorRepository.saveAll(items);

        return new ResponseEntity<>("Logs saved.", HttpStatus.OK);
    }

    private LogAppError generateError(Auth auth, LogAppErrorRequestModel model, String userAgent) {
        LogAppError error = new LogAppError();
        error.setAuth(auth);
        error.setError(model.getError());
        error.setFile(model.getFile());
        error.setTitle(model.getTitle());
        error.setDate(new Date(model.getDate()));
        error.setUserAgent(userAgent);
        return error;
    }

}
