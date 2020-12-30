package br.ufrgs.inf.pet.dinoapi.service.user;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.model.user.UserDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.user.UserRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.SynchronizableQueueMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl extends SynchronizableServiceImpl<User, Long, Integer, UserDataModel, UserRepository> {

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthServiceImpl authService,
                           ClockServiceImpl clockService, LogAPIErrorServiceImpl logAPIErrorService,
                           SynchronizableQueueMessageServiceImpl<Long, Integer, UserDataModel> synchronizableQueueMessageService) {
        super(userRepository, authService, clockService, synchronizableQueueMessageService, logAPIErrorService);
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
    public User convertModelToEntity(UserDataModel model, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        final User user = auth.getUser();
        user.setPictureURL(model.getPictureURL());
        return user;
    }

    @Override
    public void updateEntity(User user, UserDataModel model, Auth auth) {
        if (!user.getPictureURL().equals(model.getPictureURL())) {
            user.setPictureURL(model.getPictureURL());
        }
    }

    @Override
    public Optional<User> getEntityByIdAndUserAuth(Long id, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return Optional.of(auth.getUser());
    }

    @Override
    public List<User> getEntitiesByUserAuth(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        List<User> users = new ArrayList<>();
        users.add(auth.getUser());
        return users;
    }

    @Override
    public List<User> getEntitiesByIdsAndUserAuth(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        List<User> users = new ArrayList<>();
        users.add(auth.getUser());
        return users;
    }

    @Override
    public List<User> getEntitiesByUserAuthExceptIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        List<User> users = new ArrayList<>();
        users.add(auth.getUser());
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

    public User save(String name, String email, String pictureUrl, Auth auth) {
        final User savedUser = this.saveUser(name, email, pictureUrl, auth.getUser());

        this.sendUpdateMessage(savedUser, auth);

        return savedUser;
    }

    public User saveNew(String name, String email, String pictureUrl) {
        User user = this.findUserByEmail(email);

        if (user == null) {
            user = new User();
        }

        return this.saveUser(name, email, pictureUrl, user);
    }

    private User saveUser(String name, String email, String pictureUrl, User user) {
        user.setPictureURL(pictureUrl);
        user.setEmail(email);
        user.setName(name);

        return this.repository.save(user);
    }

    private void sendUpdateMessage(User user, Auth auth) {
        final UserDataModel model = this.internalConvertEntityToModel(user);

        this.sendUpdateMessage(model, auth);
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
}
