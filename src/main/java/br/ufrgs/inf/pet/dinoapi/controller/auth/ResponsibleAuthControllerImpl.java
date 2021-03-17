package br.ufrgs.inf.pet.dinoapi.controller.auth;

import br.ufrgs.inf.pet.dinoapi.model.user.CreateResponsibleAuthModel;
import br.ufrgs.inf.pet.dinoapi.model.user.CreateResponsibleAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.user.RecoverPasswordDataModel;
import br.ufrgs.inf.pet.dinoapi.service.auth.ResponsibleAuthService;
import br.ufrgs.inf.pet.dinoapi.service.auth.ResponsibleAuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/responsible_auth/")
public class
ResponsibleAuthControllerImpl implements ResponsibleAuthController {

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
    public ResponseEntity<CreateResponsibleAuthResponseModel> changeAuth(@Valid @RequestBody RecoverPasswordDataModel model) {
        return recoverPasswordRequestService.changeAuth(model);
    }

    @Override
    @PostMapping("create_auth/")
    public ResponseEntity<CreateResponsibleAuthResponseModel> createResponsibleAuth(@Valid @RequestBody CreateResponsibleAuthModel model) {
        return recoverPasswordRequestService.createResponsibleAuth(model);
    }
}