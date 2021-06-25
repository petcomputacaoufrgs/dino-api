package br.ufrgs.inf.pet.dinoapi.service.contact.async;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.GoogleContact;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.enumerable.PermissionEnum;
import br.ufrgs.inf.pet.dinoapi.service.contact.GoogleContactServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AsyncContactService extends LogUtilsBase {
    private final GoogleContactServiceImpl googleContactService;

    public AsyncContactService(LogAPIErrorServiceImpl logAPIErrorService,
                               GoogleContactServiceImpl googleContactService) {
        super(logAPIErrorService);
        this.googleContactService = googleContactService;
    }

    @Async("contactThreadPoolTaskExecutor")
    public void createContactOnGoogleAPI(Contact entity, Auth auth) {
        final User user = auth.getUser();

        googleContactService.createNewGoogleContact(entity, user);
    }

    @Async("contactThreadPoolTaskExecutor")
    public void updateContactOnGoogleAPI(Contact entity, Auth auth) {
        final User user = auth.getUser();

        final Optional<GoogleContact> googleContactSearch = this.googleContactService.findByContactId(entity.getId());

        if (googleContactSearch.isPresent()) {
            googleContactService.updateGoogleContact(entity, googleContactSearch.get());
        } else {
            googleContactService.createNewGoogleContact(entity, user);
        }
    }

    @Async("contactThreadPoolTaskExecutor")
    public void deleteContactOnGoogleAPI(String resourceName, Auth auth) {
        final User user = auth.getUser();
        final boolean hasUserPermission = user.getPermission().equals(PermissionEnum.USER.getValue());

        if (hasUserPermission) {
            googleContactService.deleteGoogleContact(resourceName, user);
        }
    }
}
