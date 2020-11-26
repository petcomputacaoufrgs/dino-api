package br.ufrgs.inf.pet.dinoapi.communication.google;

import br.ufrgs.inf.pet.dinoapi.config.AppOriginConfig;
import br.ufrgs.inf.pet.dinoapi.enumerable.GoogleScopesEnum;
import br.ufrgs.inf.pet.dinoapi.exception.GoogleClientSecretIOException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class GoogleAPICommunicationImpl implements GoogleAPICommunication {

    public GoogleTokenResponse getGoogleToken(String token) throws GoogleClientSecretIOException {
        try {
            final GoogleClientSecrets clientSecrets = this.getClientSecrets();

            final String redirect_uri = new AppOriginConfig().getOrigin();

            final ArrayList<String> scopes = new ArrayList<>();

            scopes.add(GoogleScopesEnum.CALENDAR.getScope());
            scopes.add(GoogleScopesEnum.PROFILE.getScope());

            GoogleTokenResponse tokenResponse =
                    new GoogleAuthorizationCodeTokenRequest(
                            new NetHttpTransport(),
                            JacksonFactory.getDefaultInstance(),
                            "https://oauth2.googleapis.com/token",
                            clientSecrets.getDetails().getClientId(),
                            clientSecrets.getDetails().getClientSecret(),
                            token,
                            redirect_uri)   // Specify the same redirect URI that you use with your web
                                            // app. If you don't have a web version of your app, you can
                                            // specify an empty string.
                            .setScopes(scopes)
                            .execute();


            return tokenResponse;
        } catch (IOException ex) {
            throw new GoogleClientSecretIOException("Erro ao ler arquivos de configuração do servidor.");
        }
    }

    public GoogleTokenResponse refreshAccessToken(String refreshToken) {
        final ArrayList<String> scopes = new ArrayList<>();

        scopes.add(GoogleScopesEnum.CALENDAR.getScope());
        scopes.add(GoogleScopesEnum.PROFILE.getScope());

        try {
            final GoogleClientSecrets secrets = this.getClientSecrets();

            final GoogleTokenResponse tokenResponse = new GoogleRefreshTokenRequest(
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
        final String googleSecret = "{\"web\":{\"client_id\":\"398811150587-720e3bk1uvvij6t1a59d8220f620hj6d.apps.googleusercontent.com\",\"project_id\":\"dinoapp-285513\",\"auth_uri\":\"https://accounts.google.com/o/oauth2/auth\",\"token_uri\":\"https://oauth2.googleapis.com/token\",\"auth_provider_x509_cert_url\":\"https://www.googleapis.com/oauth2/v1/certs\",\"client_secret\":\"2n8Y_xTxfb_jPfJM6mAPZg_i\",\"javascript_origins\":[\"http://localhost:3000\"]}}";
        return GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new StringReader(googleSecret));
    }

}
