package br.ufrgs.inf.pet.dinoapi.communication.google;

import br.ufrgs.inf.pet.dinoapi.communication.google.oauth.GoogleOAuthCommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.enumerable.GoogleAPIHeaderEnum;
import br.ufrgs.inf.pet.dinoapi.enumerable.HttpContentTypeEnum;
import br.ufrgs.inf.pet.dinoapi.enumerable.HttpHeaderEnum;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.service.auth.google.GoogleAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public abstract class GoogleCommunication extends LogUtilsBase {

    private final GoogleOAuthCommunicationImpl googleOAuthCommunication;
    private final GoogleAuthServiceImpl googleAuthService;

    public GoogleCommunication(LogAPIErrorServiceImpl logAPIErrorService,
                               GoogleOAuthCommunicationImpl googleOAuthCommunication,
                               GoogleAuthServiceImpl googleAuthService) {
        super(logAPIErrorService);
        this.googleOAuthCommunication = googleOAuthCommunication;
        this.googleAuthService = googleAuthService;
    }


    public HttpRequest.Builder createBaseRequest(String accessToken) {
        return HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .header(GoogleAPIHeaderEnum.AUTHORIZATION.getValue(), "Bearer " + accessToken)
                .header(HttpHeaderEnum.CONTENT_TYPE.getValue(), HttpContentTypeEnum.JSON.getValue());
    }

    public HttpResponse<String> send(HttpRequest request) throws IOException, InterruptedException {
        return HttpClient
                .newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
    }

    public GoogleAuth getGoogleAuth(User user) {
        final GoogleAuth googleAuth = user.getGoogleAuth();

        if (googleAuth == null || googleAuth.getRefreshToken() == null) return null;

        return googleAuth;
    }

    public String getAccessTokenAndSaveScopes(
            GoogleAuth googleAuth
    ) throws AuthNullException, ConvertModelToEntityException {
        if (googleAuth.getAccessToken() == null
                || LocalDateTime.now().isAfter(googleAuth.getAccessTokenExpiresDate())) {
            final GoogleTokenResponse googleTokenResponse =
                    googleOAuthCommunication.getNewAccessTokenWithRefreshToken(googleAuth);

            if (googleTokenResponse == null) return null;

            final String accessToken = googleTokenResponse.getAccessToken();
            final LocalDateTime expiresDate = googleAuthService.getExpiresDateFromToken(googleTokenResponse);
            googleAuth.setAccessToken(accessToken);
            googleAuth.setAccessTokenExpiresDate(expiresDate);
            googleAuthService.save(googleAuth);

            this.saveAllScopes(googleTokenResponse, googleAuth.getUser());

            return accessToken;
        }

        return googleAuth.getAccessToken();
    }

    private List<String> saveAllScopes(
            GoogleTokenResponse googleTokenResponse, User user
    ) throws AuthNullException, ConvertModelToEntityException {
        final List<String> currentScopes = Arrays.asList(googleTokenResponse.getScope().split(" "));

        final Auth fakeAuth = new Auth();
        fakeAuth.setUser(user);

        googleAuthService.saveAllScopes(currentScopes, fakeAuth);

        return currentScopes;
    }
}
