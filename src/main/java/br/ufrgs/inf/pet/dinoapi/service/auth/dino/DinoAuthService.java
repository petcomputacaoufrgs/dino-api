package br.ufrgs.inf.pet.dinoapi.service.auth.dino;

import br.ufrgs.inf.pet.dinoapi.entity.User;

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
