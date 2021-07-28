package br.ufrgs.inf.pet.dinoapi.controller.user;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.user.UserDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.user.UserRepository;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.ufrgs.inf.pet.dinoapi.constants.PathConstants.USER;
import static br.ufrgs.inf.pet.dinoapi.constants.PathConstants.USER_DELETE_ACCOUNT;

@RestController
@RequestMapping(USER)
public class UserControllerImpl extends SynchronizableControllerImpl<User, Long, UserDataModel,
        UserRepository, UserServiceImpl> implements UserController {

    @Autowired
    public UserControllerImpl(UserServiceImpl userService) {
        super(userService);
    }

    @Override
    @DeleteMapping(USER_DELETE_ACCOUNT)
    public ResponseEntity<Boolean> deleteAccount() {
        return this.service.deleteAccount();
    }
}
