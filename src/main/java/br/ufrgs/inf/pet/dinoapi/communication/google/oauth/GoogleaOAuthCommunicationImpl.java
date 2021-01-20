package br.ufrgs.inf.pet.dinoapi.communication.google.oauth;

import br.ufrgs.inf.pet.dinoapi.configuration.application_properties.AppConfig;
import br.ufrgs.inf.pet.dinoapi.configuration.application_properties.GoogleOAuth2Config;
import br.ufrgs.inf.pet.dinoapi.constants.AuthConstants;
import br.ufrgs.inf.pet.dinoapi.enumerable.GoogleAPIURLEnum;
import br.ufrgs.inf.pet.dinoapi.exception.GoogleClientSecretIOException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GoogleaOAuthCommunicationImpl implements GoogleaOAuthCommunication {

    private final GoogleOAuth2Config googleOAuth2Config;

    private final AppConfig appConfig;

    @Autowired
    public GoogleaOAuthCommunicationImpl(GoogleOAuth2Config googleOAuth2Config, AppConfig appConfig) {
        this.googleOAuth2Config = googleOAuth2Config;
        this.appConfig = appConfig;
    }

    @Override
    public GoogleTokenResponse getGoogleToken(String token, List<String> scopes) throws GoogleClientSecretIOException {
        try {
            final String redirect_uri = appConfig.getOrigin();

            return new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    GoogleAPIURLEnum.TOKEN_REQUEST.getValue(),
                    googleOAuth2Config.getClientid(),
                    googleOAuth2Config.getClientsecret(),
                    token,
                    redirect_uri)
                    .setScopes(scopes)
                    .execute();
        } catch (IOException ex) {
            throw new GoogleClientSecretIOException(AuthConstants.ERROR_READING_CONFIG_FILES);
        }
    }

    @Override
    public GoogleTokenResponse getNewAccessTokenWithRefreshToken(String refreshToken) {
        final ArrayList<String> scopes = new ArrayList<>();

        GoogleTokenResponse tokenResponse;
        try {
            tokenResponse = new GoogleRefreshTokenRequest(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    refreshToken,
                    googleOAuth2Config.getClientid(),
                    googleOAuth2Config.getClientsecret())
                    .setScopes(scopes)
                    .setGrantType("refresh_token")
                    .execute();

            return tokenResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
