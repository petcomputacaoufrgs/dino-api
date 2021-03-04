package br.ufrgs.inf.pet.dinoapi.controller.user;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.SynchronizableDataResponseModelImpl;
import br.ufrgs.inf.pet.dinoapi.model.user.RecoverPasswordDataModel;
import br.ufrgs.inf.pet.dinoapi.model.user.UserSettingsDataModel;
import org.springframework.http.ResponseEntity;

public interface RecoverPasswordRequestController {
    /**
     * Send email to authenticated email with code to recover password
     * @return HttpStatus
     */
    ResponseEntity<Void> requestCode();

    /**
     * Validate code
     * @return return true if valid
     */
    ResponseEntity<Boolean> verifyCode(RecoverPasswordDataModel model);

    /**
     * Change password
     * @return if success return UserSettings otherwise return null
     */
    ResponseEntity<SynchronizableDataResponseModelImpl<Long, UserSettingsDataModel>> changePassword(RecoverPasswordDataModel model);
}
