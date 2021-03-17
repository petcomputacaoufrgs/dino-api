package br.ufrgs.inf.pet.dinoapi.service.auth;

import br.ufrgs.inf.pet.dinoapi.model.user.CreateResponsibleAuthModel;
import br.ufrgs.inf.pet.dinoapi.model.user.CreateResponsibleAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.user.RecoverPasswordDataModel;
import org.springframework.http.ResponseEntity;

public interface ResponsibleAuthService {
    ResponseEntity<Void> requestRecoverCode();

    ResponseEntity<Boolean> verifyRecoverCode(RecoverPasswordDataModel model);

    ResponseEntity<CreateResponsibleAuthResponseModel> changeAuth(RecoverPasswordDataModel model);

    ResponseEntity<CreateResponsibleAuthResponseModel> createResponsibleAuth(CreateResponsibleAuthModel model);
}
