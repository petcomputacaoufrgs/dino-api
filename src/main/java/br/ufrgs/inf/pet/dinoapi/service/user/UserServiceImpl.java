package br.ufrgs.inf.pet.dinoapi.service.user;

import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.model.user.UserDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.ContactRepository;
import br.ufrgs.inf.pet.dinoapi.repository.user.UserRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.contact.PhoneServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.synchronizable.SynchronizableQueueMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl extends SynchronizableServiceImpl<User, Long, Integer, UserDataModel, UserRepository> {

    private final ContactRepository contactRepository;

    private final PhoneServiceImpl phoneServiceImpl;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthServiceImpl authService,
                           ContactRepository contactRepository, PhoneServiceImpl phoneServiceImpl,
                           SynchronizableQueueMessageServiceImpl<Long, Integer, UserDataModel> synchronizableQueueMessageService) {
        super(userRepository, authService, synchronizableQueueMessageService);
        this.contactRepository = contactRepository;
        this.phoneServiceImpl = phoneServiceImpl;
    }

    @Override
    public UserDataModel convertEntityToModel(User entity) {
        final UserDataModel userDataModel = new UserDataModel();
        userDataModel.setEmail(entity.getEmail());
        userDataModel.setName(entity.getName());
        userDataModel.setPictureURL(entity.getPictureURL());

        return userDataModel;
    }

    @Override
    public User convertModelToEntity(UserDataModel model, User user) {
        user.setPictureURL(model.getPictureURL());
        return user;
    }

    @Override
    public void updateEntity(User user, UserDataModel model, User loginUser) {
        if (!user.getPictureURL().equals(model.getPictureURL())) {
            user.setPictureURL(model.getPictureURL());
        }
    }

    @Override
    public Optional<User> getEntityByIdAndUser(Long id, User user) {
        return Optional.of(user);
    }

    @Override
    public List<User> getEntitiesByUserId(User user) {
        List<User> users = new ArrayList<>();
        users.add(user);
        return users;
    }

    @Override
    public List<User> getEntitiesByIdsAndUserId(List<Long> ids, User user) {
        List<User> users = new ArrayList<>();
        users.add(user);
        return users;
    }

    @Override
    public List<User> getEntitiesByUserIdExceptIds(User user, List<Long> ids) {
        List<User> users = new ArrayList<>();
        users.add(user);
        return users;
    }

    @Override
    public WebSocketDestinationsEnum getUpdateWebSocketDestination() {
        return WebSocketDestinationsEnum.USER_UPDATE;
    }

    @Override
    public WebSocketDestinationsEnum getDeleteWebSocketDestination() {
        return WebSocketDestinationsEnum.USER_DELETE;
    }


    @Override
    public boolean shouldDelete(User user, SynchronizableDeleteModel<Long> model) {
        return false;
    }

    public User create(String name, String email, String pictureUrl) {
        User user = this.findUserByEmail(email);

        if (user == null) {
            user = this.save(new User(name, email, pictureUrl));
            //this.createDefaultUserData(user);
            return user;
        }
        return null;
    }

    public User update(String name, String email, String pictureUrl, User user) {
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
                final User savedUser = this.save(user);

                final UserDataModel model = new UserDataModel();
                model.setPictureURL(user.getPictureURL());
                model.setEmail(user.getEmail());
                model.setName(user.getName());
                model.setLastUpdate(LocalDateTime.now());
                model.setId(savedUser.getId());

                this.sendUpdateMessage(model, savedUser);

                return savedUser;
            }

            return user;
        }

        return null;
    }

    public User findUserByEmail(String email) {
        if (email != null) {
            final Optional<User> queryResult = this.repository.findByEmail(email);
            if (queryResult.isPresent()) {
                return queryResult.get();
            }
        }

        return null;
    }

    private User save(User user) {
        return this.repository.save(user);
    }

    /*
    private void createDefaultUserData(User user) {

        ContactsConstants.DEFAULT_CONTACTS.forEach(model -> {
            Contact contact = contactRepository.save(new Contact(model, user));

            contact.setPhones(phoneServiceImpl.savePhones(model.getPhones(), contact));
        });
    }
     */
}
