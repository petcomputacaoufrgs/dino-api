package br.ufrgs.inf.pet.dinoapi.service.auth.dino;

import br.ufrgs.inf.pet.dinoapi.entity.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    /**
     * Atualiza o token de acesso expirado
     *
     * @param auth - Autenticação expirada
     * @return token atualizado
     */
    Auth refreshAuth(Auth auth);

    /**
     * Gera um token de acesso no usuário
     *
     * @param user - Usuário
     * @return token
     */
    Auth generateAuth(User user);

    /**
     * Busca a autenticação do usuário pelo token de acesso
     *
     * @param accessToken - Token de acesso
     * @return autenticação salva no banoo de dados
     */
    Auth findByAccessToken(String accessToken);

    /**
     * Retorna a autenticação corrente
     * @return Autenticação do usuário
     */
    Auth getCurrentAuth();

    /**
     * Retorna o usuário corrente
     * @return Usuário autenticado
     */
    User getCurrentUser();

    /**
     * Limpa os dados de autenticação correntes
     *
     * @return Mensagem de remoção com status OK
     */
    ResponseEntity<?> logout();

}
