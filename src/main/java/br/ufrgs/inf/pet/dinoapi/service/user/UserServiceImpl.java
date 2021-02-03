package br.ufrgs.inf.pet.dinoapi.service.user;

import br.ufrgs.inf.pet.dinoapi.constants.AuthConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Staff;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.enumerable.AuthEnum;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.model.user.UserDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.user.UserRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.OAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.auth.StaffServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.SynchronizableQueueMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl extends SynchronizableServiceImpl<User, Long, UserDataModel, UserRepository> {

    StaffServiceImpl staffService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, OAuthServiceImpl authService,
                           ClockServiceImpl clockService, LogAPIErrorServiceImpl logAPIErrorService,
                           @Lazy StaffServiceImpl staffService,
                           SynchronizableQueueMessageService<Long, UserDataModel> synchronizableQueueMessageService) {
        super(userRepository, authService, clockService, synchronizableQueueMessageService, logAPIErrorService);

        this.staffService = staffService;
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
        User user = this.findUserByEmail(email);

        if (user == null) {
            user = new User();
        }

        return this.saveUserPermission(this.saveUser(name, email, pictureUrl, user));
    }

    private User saveUser(String name, String email, String pictureUrl, User user) {
        user.setPictureURL(pictureUrl);
        user.setEmail(email);
        user.setName(name);

        return this.repository.save(user);
    }

    private User saveUserPermission(User user) {

        Staff staffSearch = staffService.findStaffByEmail(user.getEmail());

        if(staffSearch != null) {
            staffService.updateStaffUser(staffSearch, user, authService.getCurrentAuth());
            user.setPermission(AuthEnum.STAFF.getValue());

        } else if (user.getEmail().equals(AuthConstants.CLIENT)) {
            user.setPermission(AuthEnum.CLIENT.getValue());

        } else {
            user.setPermission(AuthEnum.USER.getValue());
        }

        return this.repository.save(user);
    }

    private void sendUpdateMessage(User user, Auth auth) {
        final UserDataModel model = this.completeConvertEntityToModel(user);

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

    public User findUserById(Long id) {
        if (id != null) {
            final Optional<User> queryResult = this.repository.findById(id);
            if (queryResult.isPresent()) {
                return queryResult.get();
            }
        }

        return null;
    }

    public List<User> findUserBySaveEssentialContacts() {
        return this.repository.findUserBySaveEssentialContacts();
    }

    public List<User> findUserBySaveEssentialContactsAndTreatments(List<Treatment> treatments) {
        return this.repository.findUserBySaveEssentialContactsAndTreatments(treatments);
    }
}
