package br.ufrgs.inf.pet.dinoapi.service.log_app_error;

import br.ufrgs.inf.pet.dinoapi.entity.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.LogAppError;
import br.ufrgs.inf.pet.dinoapi.model.log_app_error.LogAppErroListModel;
import br.ufrgs.inf.pet.dinoapi.model.log_app_error.LogAppErrorModel;
import br.ufrgs.inf.pet.dinoapi.repository.LogAppErrorRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogAppErrorServiceImpl implements LogAppErrorService {

    @Autowired
    AuthServiceImpl authService;

    @Autowired
    LogAppErrorRepository logAppErrorRepository;

    @Override
    public ResponseEntity<?> save(LogAppErrorModel model) {
        Auth auth = authService.getCurrentAuth();

        LogAppError error = new LogAppError();
        error.setAuth(auth);
        error.setError(model.getError());
        error.setFile(model.getFile());
        error.setTitle(model.getTitle());

        logAppErrorRepository.save(error);

        return new ResponseEntity<>("Log saved.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> saveAll(LogAppErroListModel model) {
        Auth auth = authService.getCurrentAuth();
        List<LogAppError> items = model.getItems().stream().map(item ->{
            LogAppError error = new LogAppError();
            error.setAuth(auth);
            error.setError(item.getError());
            error.setFile(item.getFile());
            error.setTitle(item.getTitle());

            return error;
        }).collect(Collectors.toList());

        logAppErrorRepository.saveAll(items);

        return new ResponseEntity<>("Logs saved.", HttpStatus.OK);
    }

}
