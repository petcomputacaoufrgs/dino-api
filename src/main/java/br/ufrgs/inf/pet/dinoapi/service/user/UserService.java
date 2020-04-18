package br.ufrgs.inf.pet.dinoapi.service.user;

import br.ufrgs.inf.pet.dinoapi.entity.User;
import org.springframework.stereotype.Service;

/**
 * Service para a tabela: {@link br.ufrgs.inf.pet.dinoapi.entity.User}
 *
 * @author joao.silva
 */
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
     * Salva um usuário direto na base de dados
     *
     * @param user Usuário já validado a ser salvo
     *
     * @author joao.silva
     */
    void save(User user);

    /**
     * Busca um usuário por seu token de acesso
     *
     * @param accessToken Token de acesso do usuário
     * @return Usuário encontrado ou nulo
     */
    User findByAccessToken(String accessToken);
}
