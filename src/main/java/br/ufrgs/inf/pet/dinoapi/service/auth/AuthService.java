package br.ufrgs.inf.pet.dinoapi.service.auth;

import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.model.auth.AuthRequestModel;
import org.springframework.http.ResponseEntity;

/**
 * Service de autenticação
 *
 * @author joao.silva
 */
public interface AuthService {
    /**
     * Dado um token de autenticação gera um token de acesso
     *
     * @param token - Token de autenticação do Google
     * @return token validado
     */
    ResponseEntity<?> authRequestGoogleSign(AuthRequestModel token);

    /**
     * Realiza o logout do usuário com a conta do Google na API
     *
     * @return http response com o resultado da requisição
     */
    ResponseEntity<?> logoutGoogleSign();

    /**
     * Retorna o nome do usuário
     */
    ResponseEntity<?> getName();

    /**
     * Atualiza o token de acesso expirado
     *
     * @param userDB - Usuário logado
     * @return token atualizado
     */
     String refreshGoogleAuth(User userDB);

    /**
     * Retorna o usuário logado com todos os seus dados do banco
     *
     * @return usuário do banco
     */
    User getCurrentUser();
}
