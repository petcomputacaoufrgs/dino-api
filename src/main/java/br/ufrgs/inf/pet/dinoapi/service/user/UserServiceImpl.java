package br.ufrgs.inf.pet.dinoapi.service.user;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.contacts.ContactSaveModel;
import br.ufrgs.inf.pet.dinoapi.model.user.UpdateUserPictureRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.user.UserResponseModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.EssentialContactRepository;
import br.ufrgs.inf.pet.dinoapi.repository.user.UserRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.ContactServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.alert_update.AlertUpdateQueueServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AuthServiceImpl authService;

    private final AlertUpdateQueueServiceImpl alertUpdateQueueServiceImpl;

    private final EssentialContactRepository essentialContactRepository;

    private final ContactServiceImpl contactServiceImpl;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthServiceImpl authService, AlertUpdateQueueServiceImpl alertUpdateQueueServiceImpl,
                           EssentialContactRepository essentialContactRepository, ContactServiceImpl contactServiceImpl) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.alertUpdateQueueServiceImpl = alertUpdateQueueServiceImpl;
        this.essentialContactRepository = essentialContactRepository;
        this.contactServiceImpl = contactServiceImpl;
    }

    @Override
    public ResponseEntity<?> getVersion() {
        final User currentUser = authService.getCurrentUser();

        if (currentUser != null) {
            return new ResponseEntity<>(currentUser.getVersion(), HttpStatus.OK);
        }

        return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> getUser() {
        final User currentUser = authService.getCurrentUser();

        if (currentUser != null) {
            final UserResponseModel model = new UserResponseModel();
            model.setEmail(currentUser.getEmail());
            model.setName(currentUser.getName());
            model.setPictureURL(currentUser.getPictureURL());

            return new ResponseEntity<>(model, HttpStatus.OK);
        }

        return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> setUserPhoto(UpdateUserPictureRequestModel model) {
        User currentUser = authService.getCurrentUser();

        if (model.getPictureURL().isBlank()) {
            return new ResponseEntity<>("A URL de foto de perfil não pode ser vazia.", HttpStatus.BAD_REQUEST);
        }

        if (currentUser != null) {
            currentUser.setPictureURL(model.getPictureURL());
            currentUser.updateVersion();
            currentUser = userRepository.save(currentUser);
            alertUpdateQueueServiceImpl.sendUpdateMessage(currentUser.getVersion(), WebSocketDestinationsEnum.ALERT_USER_UPDATE);

            return new ResponseEntity<>(currentUser.getVersion(), HttpStatus.OK);
        }

        return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.BAD_REQUEST);
    }

    @Override
    public User create(String name, String email, String pictureUrl) {
        User user = this.findUserByEmail(email);

        if (user == null) {
            user = this.save(new User(name, email, pictureUrl));
            this.createDefaultUserData(user);
            return user;
        }
        return null;
    }

    @Override
    public User update(String name, String email, String pictureUrl) {
        User user = this.findUserByEmail(email);
        boolean updated = false;

        if (user != null) {
            if (!user.getEmail().equals(email)) {
                user.setEmail(email);
                updated = true;
            }
            if (!user.getName().equals(name)) {
                user.setName(name);
                updated = true;
            }
            if(!user.getPictureURL().equals(pictureUrl)) {
                user.setPictureURL(pictureUrl);
                updated = true;
            }
            if (updated) {
                user.updateVersion();
                alertUpdateQueueServiceImpl.sendUpdateMessage(user.getVersion(), WebSocketDestinationsEnum.ALERT_USER_UPDATE);
                return this.save(user);
            }

            return user;
        }

        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        if (email != null) {
            final Optional<User> queryResult = userRepository.findByEmail(email);
            if (queryResult.isPresent()) {
                return queryResult.get();
            }
        }

        return null;
    }

    private User save(User user) {
        return userRepository.save(user);
    }

    private void createDefaultUserData(User user) {

        List<EssentialContact> defaultContacts = essentialContactRepository.findByFaqIdIsNull();

        defaultContacts.forEach(dContact ->
            contactServiceImpl.saveContactOnRepository(
                    new ContactSaveModel(dContact.getContact()), user)
        );
    }

}
