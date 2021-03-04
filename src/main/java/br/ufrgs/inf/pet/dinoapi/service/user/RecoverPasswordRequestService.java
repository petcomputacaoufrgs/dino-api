package br.ufrgs.inf.pet.dinoapi.service.user;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.SynchronizableDataResponseModelImpl;
import br.ufrgs.inf.pet.dinoapi.model.user.RecoverPasswordDataModel;
import br.ufrgs.inf.pet.dinoapi.model.user.UserSettingsDataModel;
import org.springframework.http.ResponseEntity;

public interface RecoverPasswordRequestService {
    ResponseEntity<Void> requestCode();

    ResponseEntity<Boolean> verifyCode(RecoverPasswordDataModel model);

    ResponseEntity<SynchronizableDataResponseModelImpl<Long, UserSettingsDataModel>> changePassword(RecoverPasswordDataModel model);
}
