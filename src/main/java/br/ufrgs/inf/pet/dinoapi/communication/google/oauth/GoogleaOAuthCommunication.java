package br.ufrgs.inf.pet.dinoapi.communication.google.oauth;

import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import java.util.List;

public interface GoogleaOAuthCommunication {
    /**
     * Request a new login with Google API
     *
     * @param token Google login token
     * @return GoogleTokenResponse with new access data
     */
    GoogleTokenResponse getGoogleToken(String token, List<String> scopes) throws Exception;
    
    /**
     *  Get new access token from Google API
     *
     * @param googleAuth Google auth data
     * @return GoogleTokenResponse with new access data
     */
    GoogleTokenResponse getNewAccessTokenWithRefreshToken(GoogleAuth googleAuth);
}
