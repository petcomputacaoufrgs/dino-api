package br.ufrgs.inf.pet.dinoapi.service.user;

import br.ufrgs.inf.pet.dinoapi.controller.auth.AuthController;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service para a tabela: {@link br.ufrgs.inf.pet.dinoapi.entity.User}
 *
 * @author joao.silva
 */
@Service
public interface UserService {
    /**
     * Cria um novo usuário na base de dados validando os dados da entradad
     *
     * @param user Objeto com os dados do usuário a ser salvo
     * @return ResponseEntity com o usuário salvo e status ok ou com uma mensagem e um status de erro
     *
     * @author joao.silva
     */
    ResponseEntity<?> create(User user);

    /**
     * Busca na base de dados um usuário pelo seu "externalId"
     *
     * @param externalId Token externo de um provedor de autenticação
     * @return Usuário salvo
     *
     * @author joao.silva
     */
    User findOneUserByExternalId(String externalId);

    /**
     * Salva um usuário direto na base de dados
     *
     * @param user Usuário já validado a ser salvo
     *
     * @author joao.silva
     */
    void save(User user);

    /**
     * Busca os dados de um usuário pelo seu token de acesso
     *
     * @param token token de acesso
     * @return Usuário encontrado ou null
     *
     * @author joao.silva
     */
    User findOneUserByAccessToken(String token);
}
