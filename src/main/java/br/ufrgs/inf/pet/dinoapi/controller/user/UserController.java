package br.ufrgs.inf.pet.dinoapi.controller.user;

import br.ufrgs.inf.pet.dinoapi.model.user.UpdateUserPictureModel;
import org.springframework.http.ResponseEntity;

public interface UserController {

    /**
     * Busca a versão das informações do usuário
     * @return  versão das informações do usuário
     **/
    ResponseEntity<?> getVersion();


    /**
     * Busca as informações do usuário logado
     * @return Retorna as informações do usuário
     **/
    ResponseEntity<?> getUser();

    /**
     * Atualiza a foto do usuário
     * @param photoURL URL da foto do usuário
     * @return nova versão das informações do usuário
     */
    ResponseEntity<?> setUserPhoto(UpdateUserPictureModel model);

}
