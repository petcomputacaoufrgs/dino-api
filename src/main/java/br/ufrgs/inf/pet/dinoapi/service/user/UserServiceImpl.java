package br.ufrgs.inf.pet.dinoapi.service.user;

import br.ufrgs.inf.pet.dinoapi.configuration.properties.AppConfig;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Staff;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.enumerable.PermissionEnum;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.model.user.UserDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.user.UserRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.auth.StaffServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.utils.Pair;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.SynchronizableQueueMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl extends SynchronizableServiceImpl<User, Long, UserDataModel, UserRepository> implements  UserService {

    private final StaffServiceImpl staffService;

    private final AppConfig appConfig;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthServiceImpl authService,
                           ClockServiceImpl clockService, LogAPIErrorServiceImpl logAPIErrorService,
                           @Lazy StaffServiceImpl staffService, AppConfig appConfig,
                           SynchronizableQueueMessageService<Long, UserDataModel> synchronizableQueueMessageService) {
        super(userRepository, authService, clockService, synchronizableQueueMessageService, logAPIErrorService);

        this.staffService = staffService;
        this.appConfig = appConfig;
    }

    @Override
    public UserDataModel convertEntityToModel(User entity) {
        final UserDataModel userDataModel = new UserDataModel();
        userDataModel.setEmail(entity.getEmail());
        userDataModel.setName(entity.getName());
        userDataModel.setPictureURL(entity.getPictureURL());
        userDataModel.setPermission(entity.getPermission());

        return userDataModel;
    }

    @Override
    public User convertModelToEntity(UserDataModel model, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        final User user = auth.getUser();
        user.setPermission(model.getPermission());
        user.setPictureURL(model.getPictureURL());
        return user;
    }

    @Override
    public void updateEntity(User user, UserDataModel model, Auth auth) {
        if (!user.getPictureURL().equals(model.getPictureURL())) {
            user.setPictureURL(model.getPictureURL());
        }
        if(!user.getPermission().equals(model.getPermission())) {
            user.setPermission(model.getPermission());
        }
    }

    @Override
    public Optional<User> findEntityByIdThatUserCanRead(Long id, Auth auth) throws AuthNullException {
        return this.findByUser(auth);
    }

    @Override
    public Optional<User> findEntityByIdThatUserCanEdit(Long id, Auth auth) throws AuthNullException {
        return this.findByUser(auth);
    }

    private Optional<User> findByUser(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return Optional.of(auth.getUser());
    }

    @Override
    public List<User> findEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        List<User> users = new ArrayList<>();
        users.add(auth.getUser());
        return users;
    }

    @Override
    public List<User> findEntitiesByIdThatUserCanEdit(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }

        return Collections.singletonList(auth.getUser());
    }

    @Override
    public List<User> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        List<User> users = new ArrayList<>();
        users.add(auth.getUser());
        return users;
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.USER;
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
        User user = new User();

        final Pair<PermissionEnum, Staff> result = this.getUserPermission(email);
        final PermissionEnum permission = result.getFirst();
        user.setPermission(permission.getValue());
        user = this.saveUser(name, email, pictureUrl, user);

        if (permission.equals(PermissionEnum.STAFF)) {
            final Staff staff = result.getSecond();
            staffService.updateStaffUser(staff, user);
        }

        return user;
    }

    public Optional<User> findUserByEmail(String email) {
        return this.repository.findByEmail(email);
    }

    public Optional<User> findUserById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public ResponseEntity<Boolean> deleteAccount() {
        try {
            final User user = authService.getCurrentAuth().getUser();
            this.repository.delete(user);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            this.logAPIError(e);
        }

        return new ResponseEntity<>(false, HttpStatus.OK);
    }

    private User saveUser(String name, String email, String pictureUrl, User user) {
        user.setPictureURL(pictureUrl);
        user.setEmail(email);
        user.setName(name);

        return this.repository.save(user);
    }

    private Pair<PermissionEnum, Staff> getUserPermission(String email) {
        final Staff staffSearch = staffService.findStaffByEmail(email);

        if(staffSearch != null) {
            return new Pair<>(PermissionEnum.STAFF, staffSearch);
        } else if (email.equals(appConfig.getAdminEmail())) {
            return new Pair<>(PermissionEnum.ADMIN, null);
        } else {
            return new Pair<>(PermissionEnum.USER, null);
        }
    }

    private void sendUpdateMessage(User user, Auth auth) {
        final UserDataModel model = this.completeConvertEntityToModel(user);

        this.sendUpdateMessage(model, auth);
    }

    public void saveDirectly(User user) {
        this.repository.save(user);
    }
}
