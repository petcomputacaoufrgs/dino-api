package br.ufrgs.inf.pet.dinoapi.controller.auth;

import br.ufrgs.inf.pet.dinoapi.model.auth.responsible.*;
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
    public ResponseEntity<ResponsibleRequestRecoverResponseModel> requestRecoverCode(){
        return recoverPasswordRequestService.requestRecoverCode();
    }

    @Override
    @PutMapping("verify_recover_code/")
    public ResponseEntity<ResponsibleVerityRecoverCodeResponseModel> verifyRecoverCode(@Valid @RequestBody VerifyResponsibleRecoverCodeModel model) {
        return recoverPasswordRequestService.verifyRecoverCode(model);
    }

    @Override
    @PutMapping("recover_auth/")
    public ResponseEntity<SetResponsibleAuthResponseModel> recoverAuth(@Valid @RequestBody ResponsibleRecoverPasswordModel model) {
        return recoverPasswordRequestService.recoverAuth(model);
    }

    @Override
    @PostMapping("change_auth/")
    public ResponseEntity<SetResponsibleAuthResponseModel> changeAuth(@Valid @RequestBody SetResponsibleAuthModel model) {
        return recoverPasswordRequestService.changeAuth(model);
    }

    @Override
    @PostMapping("create_auth/")
    public ResponseEntity<SetResponsibleAuthResponseModel> createAuth(@Valid @RequestBody SetResponsibleAuthModel model) {
        return recoverPasswordRequestService.createAuth(model);
    }
}