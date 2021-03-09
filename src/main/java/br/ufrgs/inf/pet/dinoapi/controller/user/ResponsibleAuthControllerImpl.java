package br.ufrgs.inf.pet.dinoapi.controller.user;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.SynchronizableDataResponseModelImpl;
import br.ufrgs.inf.pet.dinoapi.model.user.RecoverPasswordDataModel;
import br.ufrgs.inf.pet.dinoapi.model.user.UserSettingsDataModel;
import br.ufrgs.inf.pet.dinoapi.service.user.ResponsibleAuthService;
import br.ufrgs.inf.pet.dinoapi.service.user.ResponsibleAuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/responsible_auth/")
public class ResponsibleAuthControllerImpl implements ResponsibleAuthController {

    private final ResponsibleAuthService recoverPasswordRequestService;

    @Autowired
    public ResponsibleAuthControllerImpl(ResponsibleAuthServiceImpl recoverPasswordRequestService) {
        this.recoverPasswordRequestService = recoverPasswordRequestService;
    }

    @Override
    @PutMapping("request_recover/")
    public ResponseEntity<Void> requestRecoverCode(){
        return recoverPasswordRequestService.requestRecoverCode();
    }

    @Override
    @PutMapping("verify_recover_code/")
    public ResponseEntity<Boolean> verifyRecoverCode(@Valid @RequestBody RecoverPasswordDataModel model) {
        return recoverPasswordRequestService.verifyRecoverCode(model);
    }

    @Override
    @PutMapping("change_auth/")
    public ResponseEntity<SynchronizableDataResponseModelImpl<Long, UserSettingsDataModel>> changeAuth(@Valid @RequestBody RecoverPasswordDataModel model) {
        return recoverPasswordRequestService.changeAuth(model);
    }

    @Override
    @PostMapping("create_auth/")
    public ResponseEntity<String> createResponsibleAuth(String password) {
        return recoverPasswordRequestService.createResponsibleAuth(password);
    }
}