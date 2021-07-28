package br.ufrgs.inf.pet.dinoapi.controller.kids_space;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.kids_space.KidsSpaceSettings;
import br.ufrgs.inf.pet.dinoapi.model.kids_space.KidsSpaceSettingsModel;
import br.ufrgs.inf.pet.dinoapi.repository.kids_space.KidsSpaceSettingsRepository;
import br.ufrgs.inf.pet.dinoapi.service.kids_space.KidsSpaceSettingsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.ufrgs.inf.pet.dinoapi.constants.PathConstants.KIDS_SPACE_SETTINGS;

@RestController
@RequestMapping(KIDS_SPACE_SETTINGS)
public class KidsSpaceSettingsController extends SynchronizableControllerImpl<KidsSpaceSettings, Long,
        KidsSpaceSettingsModel, KidsSpaceSettingsRepository, KidsSpaceSettingsServiceImpl> {

    @Autowired
    protected KidsSpaceSettingsController(KidsSpaceSettingsServiceImpl service) {
        super(service);
    }
}
