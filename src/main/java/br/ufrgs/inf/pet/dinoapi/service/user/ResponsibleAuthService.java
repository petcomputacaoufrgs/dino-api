package br.ufrgs.inf.pet.dinoapi.service.user;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.SynchronizableDataResponseModelImpl;
import br.ufrgs.inf.pet.dinoapi.model.user.RecoverPasswordDataModel;
import br.ufrgs.inf.pet.dinoapi.model.user.UserSettingsDataModel;
import org.springframework.http.ResponseEntity;

public interface ResponsibleAuthService {
    ResponseEntity<Void> requestRecoverCode();

    ResponseEntity<Boolean> verifyRecoverCode(RecoverPasswordDataModel model);

    ResponseEntity<SynchronizableDataResponseModelImpl<Long, UserSettingsDataModel>> changeAuth(RecoverPasswordDataModel model);

    ResponseEntity<String> createResponsibleAuth(String password);
}
