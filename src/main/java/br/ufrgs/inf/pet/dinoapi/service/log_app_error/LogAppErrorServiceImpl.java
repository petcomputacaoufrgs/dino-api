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
    public ResponseEntity<?> save(LogAppErrorRequestModel model) {
        Auth auth = authService.getCurrentAuth();

        LogAppError error = new LogAppError();
        error.setAuth(auth);
        error.setError(model.getError());
        error.setFile(model.getFile());
        error.setTitle(model.getTitle());
        error.setDate(new Date(model.getDate()));

        logAppErrorRepository.save(error);

        return new ResponseEntity<>("Log saved.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> saveAll(LogAppErroListRequestModel model) {
        Auth auth = authService.getCurrentAuth();
        List<LogAppError> items = model.getItems().stream().map(item ->{
            LogAppError error = new LogAppError();
            error.setAuth(auth);
            error.setError(item.getError());
            error.setFile(item.getFile());
            error.setTitle(item.getTitle());
            error.setDate(new Date(item.getDate()));

            return error;
        }).collect(Collectors.toList());

        logAppErrorRepository.saveAll(items);

        return new ResponseEntity<>("Logs saved.", HttpStatus.OK);
    }

}
