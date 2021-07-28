package br.ufrgs.inf.pet.dinoapi.controller.user;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.user.UserSettings;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableSaveSyncModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.SynchronizableSyncResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.user.UserSettingsDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.user.UserSettingsRepository;
import br.ufrgs.inf.pet.dinoapi.service.user.UserSettingsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static br.ufrgs.inf.pet.dinoapi.constants.PathConstants.SETTINGS;

@RestController
@RequestMapping(SETTINGS)
public class UserSettingsControllerImpl extends SynchronizableControllerImpl<UserSettings, Long,
        UserSettingsDataModel, UserSettingsRepository, UserSettingsServiceImpl> {

    @Autowired
    protected UserSettingsControllerImpl(UserSettingsServiceImpl service) {
        super(service);
    }
}
