package br.ufrgs.inf.pet.dinoapi.communication.google.oauth;

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import java.util.List;

public interface GoogleaOAuthCommunication {
    /**
     * Requisita os tokens de acesso do Google utilizando um token de autenticação do usuário
     *
     * @param token Token de autorização Google
     * @return GoogleTokenResponse contendo todos os dados e tokens necessários para login
     */
    GoogleTokenResponse getGoogleToken(String token, List<String> scopes) throws Exception;
    
    /**
     *  Solicita um novo token de acesso utilizando o token de atualização
     *
     * @param refreshToken Token de atualização do Google
     * @return GoogleTokenResponse contendo todos os dados e tokens necessários para login renovados
     */
    GoogleTokenResponse getNewAccessTokenWithRefreshToken(String refreshToken);
}