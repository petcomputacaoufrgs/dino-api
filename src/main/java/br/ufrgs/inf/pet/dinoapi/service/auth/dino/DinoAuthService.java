package br.ufrgs.inf.pet.dinoapi.service.auth.dino;

import br.ufrgs.inf.pet.dinoapi.entity.User;

/**
 * Implementa a autenticação do próprio Dino
 *
 * @author joao.silva
 */
public interface DinoAuthService {

    /**
     * Atualiza o token de acesso expirado
     *
     * @param user - Usuário
     * @return token atualizado
     */
    String refreshAccessToken(User user);

    /**
     * Gera um token de acesso no usuário
     *
     * @param user - Usuário
     * @return token
     */
    void generateAccessToken(User user);
}
