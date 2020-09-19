package br.ufrgs.inf.pet.dinoapi.service.user;

import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.user.UpdateUserPictureRequestModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User findUserByEmail(String email);

    ResponseEntity<?> getVersion();

    ResponseEntity<?> getUser();

    ResponseEntity<?> setUserPhoto(UpdateUserPictureRequestModel model);

    User create(String name, String email, String pictureUrl);

    User update(String name, String email, String pictureUrl);

}
