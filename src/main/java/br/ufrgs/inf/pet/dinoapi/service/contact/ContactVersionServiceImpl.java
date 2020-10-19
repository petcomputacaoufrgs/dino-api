package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.ContactVersion;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.repository.contact.ContactVersionRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.alert_update.AlertUpdateQueueServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ContactVersionServiceImpl {

    private ContactVersionRepository contactVersionRepository;
    private AuthServiceImpl authService;
    private AlertUpdateQueueServiceImpl alertUpdateQueueServiceImpl;

    @Autowired
    public ContactVersionServiceImpl(ContactVersionRepository contactVersionRepository, AuthServiceImpl authService, AlertUpdateQueueServiceImpl alertUpdateQueueServiceImpl) {
        this.contactVersionRepository = contactVersionRepository;
        this.authService = authService;
        this.alertUpdateQueueServiceImpl = alertUpdateQueueServiceImpl;
    }

    public void updateVersion(User user) {
        ContactVersion version = user.getContactVersion();

        if (version == null) {
            version = new ContactVersion();
            version.setVersion(0L);
            version.setUser(user);
        } else {
            version.setVersion(version.getVersion() + 1);
        }

        alertUpdateQueueServiceImpl.sendUpdateMessage(version.getVersion(), WebSocketDestinationsEnum.ALERT_CONTACT_UPDATE);

        contactVersionRepository.save(version);
    }

    public ResponseEntity<Long> getVersion() {

        User user = authService.getCurrentUser();

        ContactVersion version = user.getContactVersion();

        if (version == null) {
            version = new ContactVersion();
            version.setVersion(0L);
            version.setUser(user);

            contactVersionRepository.save(version);
        }

        return new ResponseEntity<>(version.getVersion(), HttpStatus.OK);
    }

}
