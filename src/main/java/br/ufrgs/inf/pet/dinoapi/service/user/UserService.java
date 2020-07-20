package br.ufrgs.inf.pet.dinoapi.service.user;

import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.model.user.UpdateUserPictureModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    /**
     * Busca um usuário por seu email
     *
     * @param email Email do usuário
     *
     * @author joao.silva
     */
    User findUserByEmail(String email);

    /**
     * Busca o usuário por um token de acesso.
     * @param accessToken
     * @return Usuário ao qual o token pertence
     */
    User findByAccessToken(String accessToken);

    /**
     * Busca a versão das informações do usuário
     * @return Versão das informações do usuário
     **/
    ResponseEntity<?> getVersion();


    /**
     * Busca as informações atualizadas do usuário
     * @return informações do usuário
     **/
    ResponseEntity<?> getUser();

    /**
     * Atualiza a foto do usuário
     * @param photoURL URL da foto do usuário
     * @return nova versão das informações do usuário
     */
    ResponseEntity<?> setUserPhoto(UpdateUserPictureModel model);

    /**
     * Cria um novo usuário e salva no banco de dados
     * @param name Nome do usuário
     * @param email Email único do usuário
     * @param pictureUrl URL com a foto de perfil do usuário
     * @return Usuário salvo no banco ou nulo caso o usuário já exista
     */
    User create(String name, String email, String pictureUrl);

    /**
     * Atualiza um usuário existente
     * @param name Nome do usuário
     * @param email Email único do usuário
     * @param pictureUrl URL com a foto de perfil do usuário
     * @return Nulo caso o usuário não exista
     */
    User update(String name, String email, String pictureUrl);

}
