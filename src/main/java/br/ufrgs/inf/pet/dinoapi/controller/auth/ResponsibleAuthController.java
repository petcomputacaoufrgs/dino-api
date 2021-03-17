package br.ufrgs.inf.pet.dinoapi.controller.auth;

import br.ufrgs.inf.pet.dinoapi.model.auth.responsible.*;
import org.springframework.http.ResponseEntity;

public interface ResponsibleAuthController {
    /**
     * Send email to authenticated email with code to recover password
     * @return HttpStatus
     */
    ResponseEntity<ResponsibleRequestRecoverResponseModel> requestRecoverCode();

    /**
     * Validate responsible recover code
     * @return return true if valid
     */
    ResponseEntity<ResponsibleVerityRecoverCodeResponseModel> verifyRecoverCode(VerifyResponsibleRecoverCodeModel model);

    /**
     * Recover password
     * @return if success return JWT token with authentication code otherwise return success flag false
     */
    ResponseEntity<SetResponsibleAuthResponseModel> recoverAuth(ResponsibleRecoverPasswordModel model);

    /**
     * Change responsible token
     * Obs.: Should be protected with responsible code auth
     * @param model model with new token
     * @return if success return new token and iv with authentication code otherwise return success flag false
     */
    ResponseEntity<SetResponsibleAuthResponseModel> changeAuth(SetResponsibleAuthModel model);

    /**
     * Create responsible code with JWT token using user password
     * @param model model with user password
     * @return if success return token and iv with authentication code otherwise return success flag false
     */
    ResponseEntity<SetResponsibleAuthResponseModel> createAuth(SetResponsibleAuthModel model);
}
