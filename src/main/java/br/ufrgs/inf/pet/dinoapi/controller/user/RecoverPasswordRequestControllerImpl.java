package br.ufrgs.inf.pet.dinoapi.controller.user;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.SynchronizableDataResponseModelImpl;
import br.ufrgs.inf.pet.dinoapi.model.user.RecoverPasswordDataModel;
import br.ufrgs.inf.pet.dinoapi.model.user.UserSettingsDataModel;
import br.ufrgs.inf.pet.dinoapi.service.user.RecoverPasswordRequestService;
import br.ufrgs.inf.pet.dinoapi.service.user.RecoverPasswordRequestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/recover_password/")
public class RecoverPasswordRequestControllerImpl implements RecoverPasswordRequestController {

    private final RecoverPasswordRequestService recoverPasswordRequestService;

    @Autowired
    public RecoverPasswordRequestControllerImpl(RecoverPasswordRequestServiceImpl recoverPasswordRequestService) {
        this.recoverPasswordRequestService = recoverPasswordRequestService;
    }

    @Override
    @PutMapping("request/")
    public ResponseEntity<Void> requestCode(){
        return recoverPasswordRequestService.requestCode();
    }

    @Override
    @PutMapping("verify/")
    public ResponseEntity<Boolean> verifyCode(@Valid @RequestBody RecoverPasswordDataModel model) {
        return recoverPasswordRequestService.verifyCode(model);
    }

    @Override
    @PutMapping("change/")
    public ResponseEntity<SynchronizableDataResponseModelImpl<Long, UserSettingsDataModel>> changePassword(@Valid @RequestBody RecoverPasswordDataModel model) {
        return recoverPasswordRequestService.changePassword(model);
    }
}