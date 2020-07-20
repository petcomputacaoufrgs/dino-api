package br.ufrgs.inf.pet.dinoapi.controller.user;

import br.ufrgs.inf.pet.dinoapi.model.user.UpdateUserPictureModel;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControllerImpl implements UserController {

    @Autowired
    UserServiceImpl userService;

    @Override
    @GetMapping("/user/version/")
    public ResponseEntity<?> getVersion() {
        return userService.getVersion();
    }

    @Override
    @GetMapping("/user/")
    public ResponseEntity<?> getUser() {
        return userService.getUser();
    }

    @Override
    @PutMapping("/user/photo/")
    public ResponseEntity<?> setUserPhoto(@RequestBody UpdateUserPictureModel model) {
        return userService.setUserPhoto(model);
    }
}
