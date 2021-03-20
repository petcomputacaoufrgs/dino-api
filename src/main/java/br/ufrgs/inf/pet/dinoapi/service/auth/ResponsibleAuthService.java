package br.ufrgs.inf.pet.dinoapi.service.auth;

import br.ufrgs.inf.pet.dinoapi.model.auth.responsible.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public interface ResponsibleAuthService {
    ResponseEntity<ResponsibleRequestRecoverResponseModel> requestRecoverCode();

    ResponseEntity<ResponsibleVerityRecoverCodeResponseModel> verifyRecoverCode(VerifyResponsibleRecoverCodeModel model);

    ResponseEntity<SetResponsibleAuthResponseModel> recoverAuth(ResponsibleRecoverPasswordModel model);

    ResponseEntity<SetResponsibleAuthResponseModel> changeAuth(SetResponsibleAuthModel model);

    ResponseEntity<SetResponsibleAuthResponseModel> createAuth(SetResponsibleAuthModel model);

    void deleteOldData(LocalDateTime deadline);
}
