package br.ufrgs.inf.pet.dinoapi.service.user.async;

import br.ufrgs.inf.pet.dinoapi.entity.user.UserSettings;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.user.UserSettingsDataModel;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import br.ufrgs.inf.pet.dinoapi.service.user.UserSettingsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsyncUserSettingsService extends LogUtilsBase {
    private final UserSettingsServiceImpl userSettingsService;
    private final ClockServiceImpl clockService;

    @Autowired
    public AsyncUserSettingsService(UserSettingsServiceImpl userSettingsService, ClockServiceImpl clockService,
                                    LogAPIErrorServiceImpl logAPIErrorService) {
        super(logAPIErrorService);
        this.userSettingsService = userSettingsService;
        this.clockService = clockService;
    }

    @Async("defaultThreadPoolTaskExecutor")
    public void removeUserSettingsTreatments(List<UserSettings> userSettingsList) {
        userSettingsList.forEach(settings -> {
            final UserSettingsDataModel model = userSettingsService.convertEntityToModel(settings);
            model.setId(settings.getId());
            model.setLastUpdate(clockService.getUTCZonedDateTime());
            try {
                userSettingsService.saveByUser(model, settings.getUser());
            } catch (AuthNullException | ConvertModelToEntityException e) {
                this.logAPIError(e);
            }
        });
    }
}
