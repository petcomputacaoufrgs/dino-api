package br.ufrgs.inf.pet.dinoapi.communication.google.oauth;

import br.ufrgs.inf.pet.dinoapi.configuration.properties.AppConfig;
import br.ufrgs.inf.pet.dinoapi.configuration.properties.GoogleOAuth2Config;
import br.ufrgs.inf.pet.dinoapi.constants.AuthConstants;
import br.ufrgs.inf.pet.dinoapi.constants.GoogleAuthConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.enumerable.GoogleAPIURLEnum;
import br.ufrgs.inf.pet.dinoapi.exception.GoogleClientSecretIOException;
import br.ufrgs.inf.pet.dinoapi.model.logout.LogoutMessage;
import br.ufrgs.inf.pet.dinoapi.repository.auth.google.GoogleAuthRepository;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.GenericMessageService;
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
public class GoogleOAuthCommunicationImpl extends LogUtilsBase implements GoogleaOAuthCommunication {
    private final GoogleOAuth2Config googleOAuth2Config;
    private final AppConfig appConfig;
    private final GenericMessageService genericMessageService;
    private final GoogleAuthRepository googleAuthRepository;

    @Autowired
    public GoogleOAuthCommunicationImpl(GoogleOAuth2Config googleOAuth2Config, AppConfig appConfig,
                                        LogAPIErrorServiceImpl logAPIErrorService,
                                        GenericMessageService genericMessageService,
                                        GoogleAuthRepository googleAuthRepository) {
        super(logAPIErrorService);
        this.googleOAuth2Config = googleOAuth2Config;
        this.appConfig = appConfig;
        this.genericMessageService = genericMessageService;
        this.googleAuthRepository = googleAuthRepository;
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
    public GoogleTokenResponse getNewAccessTokenWithRefreshToken(GoogleAuth googleAuth) {
        final ArrayList<String> scopes = new ArrayList<>();

        GoogleTokenResponse tokenResponse;
        try {
            tokenResponse = new GoogleRefreshTokenRequest(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    googleAuth.getRefreshToken(),
                    googleOAuth2Config.getClientid(),
                    googleOAuth2Config.getClientsecret())
                    .setScopes(scopes)
                    .setGrantType("refresh_token")
                    .execute();

            if (tokenResponse == null || tokenResponse.getAccessToken() == null) {
                this.cleanRefreshTokenAndSendLogoutMessage(googleAuth);
            }
            return tokenResponse;
        } catch (Exception e) {
            this.cleanRefreshTokenAndSendLogoutMessage(googleAuth);
            this.logAPIError(e);
        }

        return null;
    }

    private void cleanRefreshTokenAndSendLogoutMessage(GoogleAuth googleAuth) {
        final User user = googleAuth.getUser();
        googleAuth.setRefreshToken(null);
        googleAuthRepository.save(googleAuth);

        final LogoutMessage logoutMessage = new LogoutMessage();
        logoutMessage.setMessage(GoogleAuthConstants.GOOGLE_LOGOUT_REQUEST);

        genericMessageService.send(logoutMessage, WebSocketDestinationsEnum.LOGOUT_REQUEST.getValue(), user);
    }
}
