package br.ufrgs.inf.pet.dinoapi.service.auth;

import br.ufrgs.inf.pet.dinoapi.entity.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {

    /**
     * Atualiza o token de acesso expirado
     *
     * @param auth - Autenticação expirada
     * @return token atualizado
     */
    Auth refreshAuth(Auth auth, HttpServletRequest request);

    /**
     * Gera um token de acesso no usuário
     *
     * @param user - Usuário
     * @return token
     */
    Auth generateAuth(User user, HttpServletRequest request);

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
     * @return Usuário
     */
    User getCurrentUser();

    /**
     * Retorna o contexto de autenticação corrente
     * @return Principal
     */
    UserDetails getPrincipal();

    /**
     * Limpa os dados de autenticação correntes
     *
     * @return Mensagem de remoção com status OK
     */
    ResponseEntity<?> logout();

    /**
     * Salva as informações (non-safe) do dispositivo que está acessando a API
     * @param auth Auth atual
     * @param userAgent Informações
     */
    void saveUserAgent(Auth auth, String userAgent);

}
