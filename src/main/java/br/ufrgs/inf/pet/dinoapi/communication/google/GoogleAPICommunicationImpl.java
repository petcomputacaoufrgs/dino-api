package br.ufrgs.inf.pet.dinoapi.communication.google;

import br.ufrgs.inf.pet.dinoapi.controller.auth.AuthController;
import br.ufrgs.inf.pet.dinoapi.enumerable.GoogleScopes;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Implementação de: {@link GoogleAPICommunication}
 *
 * @author joao.silva
 */
public class GoogleAPICommunicationImpl implements GoogleAPICommunication {
    private String REDIRECT_URI = "";

    public GoogleTokenResponse getGoogleToken(String token){
        try {
            GoogleClientSecrets clientSecrets = getClientSecrets();

            ArrayList<String> scopes = new ArrayList<>();

            scopes.add(GoogleScopes.CALENDAR.getScope());

            return new GoogleAuthorizationCodeTokenRequest(
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
        } catch (IOException ex) {
            //TO-DO Tratar erro
        }

        //TO-DO Tratar erro
        return null;
    }

    public GoogleTokenResponse refreshAccessToken(String refreshToken) {
        ArrayList<String> scopes = new ArrayList<>();

        scopes.add(GoogleScopes.CALENDAR.getScope());

        GoogleClientSecrets secrets = getClientSecrets();

        if (secrets != null) {
            GoogleTokenResponse tokenResponse = null;
            try {
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
        }

        return null;
    }

    private GoogleClientSecrets getClientSecrets() {
        GoogleClientSecrets secrets = new GoogleClientSecrets();
        secrets.getDetails().setAuthUri("https://accounts.google.com/o/oauth2/auth");
        secrets.getDetails().setClientId("467762039422-lgajbj314vd4ehfq51n8h7abear3pcjk.apps.googleusercontent.com");
        secrets.getDetails().setClientSecret("W8RsPL9SDhIqJKwsTIUxMx5K");
        secrets.getDetails().setTokenUri("https://oauth2.googleapis.com/token");

        return secrets;
    }

}
