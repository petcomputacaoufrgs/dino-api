package br.ufrgs.inf.pet.dinoapi.service.auth;

import br.ufrgs.inf.pet.dinoapi.communication.google.GoogleAPICommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.model.auth.AuthRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.AuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.UsernameResponseModel;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

/**
 * Implementação de: {@link AuthService}
 *
 * @author joao.silva
 */
@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    UserServiceImpl userService;

    final GoogleAPICommunicationImpl googleAPICommunicationImpl = new GoogleAPICommunicationImpl();

    @Override
    public ResponseEntity<?> authRequestGoogleSign(AuthRequestModel authModel) {
        // Requisita o token de acesso com o Google
        GoogleTokenResponse tokenResponse = googleAPICommunicationImpl.getGoogleToken(authModel.getToken());

        if (tokenResponse != null) {
            // Seleciona o idToken para resgatar dados do usuário vindos do Google
            try {
                GoogleIdToken idToken = tokenResponse.parseIdToken();

                // Payload para requisitar os dados
                GoogleIdToken.Payload payload = idToken.getPayload();

                // Resgata o userId para verificar a existencia do usuário ou salvar como novo
                String userId = payload.getSubject();

                // Busca o usuário pelo userId do Google na base do Dino
                User userDB = userService.findOneUserByExternalId(userId);

                // Se não existir cria um novo registro
                if (userDB == null) {
                    // Resgata o email do Google e o nome do usuário
                    String email = payload.getEmail();
                    String name = (String) payload.get("name");

                    userDB = new User(name, email, userId);
                } else { // Se existir atualiza as informações do usuário se necessário
                    updateUserData(payload, userDB);
                }

                // Atualiza as informações dos tokens
                updateTokenInfo(tokenResponse, userDB);

                // Salva o usuário
                userService.save(userDB);

                // Cria a resposta para o cliente
                AuthResponseModel response = new AuthResponseModel();
                response.setAccessToken(userDB.getAccessToken());

                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (IOException e) {
                return new ResponseEntity<>("Erro ao resgatar dados da autenticação com o Google.", HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>("Erro na autenticação com a API do Google.", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> getName() {
        // Pega o usuário atual
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Busca seus dados no banco pelo id externo
        User userDB = userService.findOneUserByExternalId(userDetails.getUsername());

        if (userDB != null) {
            // Monta o retorno da requisição
            UsernameResponseModel response = new UsernameResponseModel();
            response.setName(userDB.getName());

            // Retorna os dados como sucesso
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        // Caso não encontrar o usuário retorna erro
        return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.BAD_REQUEST);
    }

    @Override
    public String refreshGoogleAuth(User userDB) {
        if (userDB != null) {
            // Requisita o token de acesso atualizado com o Google
            GoogleTokenResponse tokenResponse = googleAPICommunicationImpl.refreshAccessToken(userDB.getRefreshToken());

            if (tokenResponse != null) {
                // Atualiza as informações dos tokens
                updateTokenInfo(tokenResponse, userDB);

                // Salva o usuário
                userService.save(userDB);

                return userDB.getAccessToken();
            }
        }
        return null;
    }

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            // Pega o usuário atual
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (userDetails != null) {
                // Busca seus dados no banco pelo id externo
                return userService.findOneUserByExternalId(userDetails.getUsername());
            }
        }

        return null;
    }

    /**
     * Calcula o tempo de expiração do token
     *
     * @param expiresIn tempo em ms para o token expirar
     * @return data de expiração do token em MS
     *
     * @author joao.silva
     */
    private Long getTokenExpirationDateInMS(Long expiresIn) {
        //Soma a data atual com o tempo do token expirar (converte ele de segundos para milisegundos antes)
        return (new Date()).getTime() + (expiresIn * 1000);
    }

    /**
     * Atualiza dados cadastrais do usuário no banco
     *
     * @param payload payload com os dados do Google
     * @param user usuário a ser atualizado
     *
     * @author joao.silva
     */
    private void updateUserData(GoogleIdToken.Payload payload, User user) {
        // Resgata o email do Google e o nome do usuário
        String email = payload.getEmail();
        String name = (String) payload.get("name");

        // Verifica atualização de dados
        if (!user.getEmail().equals(email)) {
            user.setEmail(email);
        }
        if (!user.getName().equals(name)) {
            user.setName(name);
        }
    }
    /**
     * Atualiza os dados dos tokens quando necessário
     *
     * @param tokenResponse resposta da requisição de login com o Google
     * @param user usuário a ser atualizado
     *
     * @author joao.silva
     */
    private void updateTokenInfo(GoogleTokenResponse tokenResponse, User user) {
        // Resgata o token de acesso para utilizar como autenticação
        String accessToken = tokenResponse.getAccessToken();

        // Caso o novo token seja diferente do antigo
        if (user.getAccessToken() == null || !user.getAccessToken().equals(accessToken)) {
            // Tempo restante para o token expirar
            Long expiresIn = tokenResponse.getExpiresInSeconds();

            // Calcula a data que o token irá expirar
            Long tokenExpiresDateInMillis = getTokenExpirationDateInMS(expiresIn);

            // Atualiza o token de acesso
            user.setAccessToken(accessToken);

            // Salva o tempo limite de uso do token de acesso
            user.setTokenExpiresDateInMillis(tokenExpiresDateInMillis);

            // Resgata o refresh token para renovar o acesso quando o token de acesso expirar
            String refreshToken = tokenResponse.getRefreshToken();

            // Salva o refresh token caso aja um
            if (tokenResponse.getRefreshToken() != null && tokenResponse.getRefreshToken() != ""){
                user.setRefreshToken(refreshToken);
            }
        }
    }
}
