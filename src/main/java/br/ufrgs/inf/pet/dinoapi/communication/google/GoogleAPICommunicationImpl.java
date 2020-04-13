package br.ufrgs.inf.pet.dinoapi.communication.google;

import br.ufrgs.inf.pet.dinoapi.entity.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.enumerable.GoogleScopes;
import br.ufrgs.inf.pet.dinoapi.service.auth.google.GoogleAuthService;
import br.ufrgs.inf.pet.dinoapi.service.auth.google.GoogleAuthServiceImpl;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Implementação de: {@link GoogleAPICommunication}
 *
 * @author joao.silva
 */
public class GoogleAPICommunicationImpl implements GoogleAPICommunication {

    @Autowired
    GoogleAuthServiceImpl googleAuthService;

    private String REDIRECT_URI = "http://localhost:3000";

    public GoogleTokenResponse getGoogleToken(String token) {
        try {
            GoogleClientSecrets clientSecrets = getClientSecrets();

            ArrayList<String> scopes = new ArrayList<>();

            scopes.add(GoogleScopes.CALENDAR.getScope());
            scopes.add(GoogleScopes.PROFILE.getScope());

            GoogleTokenResponse tokenResponse =
                    new GoogleAuthorizationCodeTokenRequest(
                            new NetHttpTransport(),
                            JacksonFactory.getDefaultInstance(),
                            "https://oauth2.googleapis.com/token",
                            clientSecrets.getDetails().getClientId(),
                            clientSecrets.getDetails().getClientSecret(),
                            token,
                            REDIRECT_URI)   // Specify the same redirect URI that you use with your web
                                            // app. If you don't have a web version of your app, you can
                                            // specify an empty string.
                            .setScopes(scopes)
                            .execute();

            return tokenResponse;
        } catch (IOException ex) {
            //TO-DO Tratar erro
            ex.printStackTrace();
        }

        //TO-DO Tratar erro
        return null;
    }

    public GoogleTokenResponse refreshAccessToken(String refreshToken) {
        ArrayList<String> scopes = new ArrayList<>();

        scopes.add(GoogleScopes.CALENDAR.getScope());

        GoogleTokenResponse tokenResponse = null;
        try {
            GoogleClientSecrets secrets = getClientSecrets();

            tokenResponse = new GoogleRefreshTokenRequest(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    refreshToken,
                    secrets.getDetails().getClientId(),
                    secrets.getDetails().getClientSecret())
                    .setScopes(scopes)
                    .setGrantType("refresh_token")
                    .execute();

            return tokenResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private GoogleClientSecrets getClientSecrets() throws IOException {
        return GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new StringReader("{\"web\":{\"client_id\":\"467762039422-nl6hebh4tceoi8k6amdfbrqredhgvikq.apps.googleusercontent.com\",\"project_id\":\"dinoapp-264514\",\"auth_uri\":\"https://accounts.google.com/o/oauth2/auth\",\"token_uri\":\"https://oauth2.googleapis.com/token\",\"auth_provider_x509_cert_url\":\"https://www.googleapis.com/oauth2/v1/certs\",\"client_secret\":\"IQjzf6_St1fLPcQ26RAN8tcE\"}}"));
    }

}
