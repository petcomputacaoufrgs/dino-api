package br.ufrgs.inf.pet.dinoapi.service.user_details;

import br.ufrgs.inf.pet.dinoapi.service.user.UserService;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Gerencia os dados de login do usuário
 *
 * @author joao.silva
 */
@Service
public class DinoUserDetailsService implements UserDetailsService {
    @Autowired
    UserService userService = new UserServiceImpl();

    @Override
    public UserDetails loadUserByUsername(String externalId) throws UsernameNotFoundException {
        br.ufrgs.inf.pet.dinoapi.entity.User userDB = userService.findOneUserByExternalId(externalId);

        if (userDB != null) {
            return new User(userDB.getExternalId(), userDB.getAccessToken(), new ArrayList<>());
        }

        throw new UsernameNotFoundException(externalId);
    }

    /**
     * Carrega o usuário pelo seu externalId e cria a entidade de login com o id e o token de acesso
     * @param externalId Id do usuário na API de autenticacã́o
     * @param jwt Token de acesso da API de autenticação
     *
     * @return UserDetais com o id externo caso seja encontrado o usuário e o token
     *
     * @author joao.silva
     */
    public UserDetails loadUserByUsername(String externalId, String jwt) throws UsernameNotFoundException {
        br.ufrgs.inf.pet.dinoapi.entity.User userDB = userService.findOneUserByExternalId(externalId);

        if (userDB != null) {
            return new User(userDB.getExternalId(),  jwt, new ArrayList<>());
        }

        throw new UsernameNotFoundException(externalId);
    }
}
