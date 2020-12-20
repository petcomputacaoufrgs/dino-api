package br.ufrgs.inf.pet.dinoapi.controller.user;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.user.UserSettings;
import br.ufrgs.inf.pet.dinoapi.model.user.UserSettingsDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.user.UserSettingsRepository;
import br.ufrgs.inf.pet.dinoapi.service.user.UserSettingsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user_settings/")
public class UserSettingsControllerImpl extends SynchronizableControllerImpl<UserSettings, Long, Integer,
        UserSettingsDataModel, UserSettingsRepository, UserSettingsServiceImpl> {

    @Autowired
    protected UserSettingsControllerImpl(UserSettingsServiceImpl service) {
        super(service);
    }
}
