package br.ufrgs.inf.pet.dinoapi.service.user;

import br.ufrgs.inf.pet.dinoapi.entity.User;
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
     * Salva um usuário direto na base de dados
     *
     * @param user Usuário já validado a ser salvo
     * @return Novo objeto com o usuário salvo
     *
     * @author joao.silva
     */
    User save(User user);

    /**
     * Busca o usuário logado no banco
     * @return Usuário logado ou nulo
     */
    User getCurrentUser();
}
