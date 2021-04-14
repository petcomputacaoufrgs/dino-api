package br.ufrgs.inf.pet.dinoapi.controller.user;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.user.UserDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.user.UserRepository;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("private/user/")
public class UserControllerImpl extends SynchronizableControllerImpl<User, Long, UserDataModel,
        UserRepository, UserServiceImpl> {
    @Autowired
    public UserControllerImpl(UserServiceImpl userService) {
        super(userService);
    }
}
