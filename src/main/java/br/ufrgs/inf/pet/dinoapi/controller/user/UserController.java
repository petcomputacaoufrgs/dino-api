package br.ufrgs.inf.pet.dinoapi.controller.user;

import br.ufrgs.inf.pet.dinoapi.model.user.UpdateUserPictureModel;
import br.ufrgs.inf.pet.dinoapi.model.user.UserModel;
import org.springframework.http.ResponseEntity;

public interface UserController {

    /**
     * Busca a versão das informações do usuário
     *
     * @return Versão das informações do usuário ou mensagem de erro
     **/
    ResponseEntity<?> getVersion();


    /**
     * Busca as informações do usuário logado
     *
     * @return Retorna as informações do usuário na model do tipo {@link UserModel} ou mensagem de erro
     **/
    ResponseEntity<?> getUser();

    /**
     * Atualiza a foto do usuário
     *
     * @param model Model do tipo {@link UpdateUserPictureModel}
     * @return Nova versão das informações do usuário ou mensagem de erro
     */
    ResponseEntity<?> setUserPhoto(UpdateUserPictureModel model);

}
