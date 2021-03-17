package br.ufrgs.inf.pet.dinoapi.controller.auth;

import br.ufrgs.inf.pet.dinoapi.model.user.CreateResponsibleAuthModel;
import br.ufrgs.inf.pet.dinoapi.model.user.CreateResponsibleAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.user.RecoverPasswordDataModel;
import org.springframework.http.ResponseEntity;

public interface ResponsibleAuthController {
    /**
     * Send email to authenticated email with code to recover password
     * @return HttpStatus
     */
    ResponseEntity<Void> requestRecoverCode();

    /**
     * Validate code
     * @return return true if valid
     */
    ResponseEntity<Boolean> verifyRecoverCode(RecoverPasswordDataModel model);

    /**
     * Change password
     * @return if success return JWT token with authentication code otherwise return success flag false
     */
    ResponseEntity<CreateResponsibleAuthResponseModel> changeAuth(RecoverPasswordDataModel model);

    /**
     * Create responsible code with JWT token using user password
     * @param model model with user password
     * @return if success return JWT token with authentication code otherwise return success flag false
     */
    ResponseEntity<CreateResponsibleAuthResponseModel> createResponsibleAuth(CreateResponsibleAuthModel model);
}
