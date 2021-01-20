package br.ufrgs.inf.pet.dinoapi.service.google;

import br.ufrgs.inf.pet.dinoapi.communication.google.oauth.GoogleaOAuthCommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleAccessToken;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.repository.google.GoogleAccessTokenRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.google.GoogleOAuthServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class GoogleAccessTokenServiceImpl implements GoogleAccessTokenService {
    private final GoogleAccessTokenRepository googleAccessTokenRepository;
    private final GoogleaOAuthCommunicationImpl googleOAuthCommunication;
    private final GoogleOAuthServiceImpl googleOAuthService;

    @Autowired
    public GoogleAccessTokenServiceImpl(GoogleAccessTokenRepository googleAccessTokenRepository,
                                        GoogleaOAuthCommunicationImpl googleOAuthCommunication,
                                        GoogleOAuthServiceImpl googleOAuthService) {
        this.googleAccessTokenRepository = googleAccessTokenRepository;
        this.googleOAuthCommunication = googleOAuthCommunication;
        this.googleOAuthService = googleOAuthService;
    }

    @Override
    public String getAccessToken(User user) {
        final GoogleAuth googleAuth = user.getGoogleAuth();

        if (googleAuth == null) return null;

        final Optional<GoogleAccessToken> googleAccessTokenSearch = googleAccessTokenRepository.findByUserId(user.getId());

        if (googleAccessTokenSearch.isPresent()) {
            final GoogleAccessToken googleAccessToken = googleAccessTokenSearch.get();
            final LocalDateTime expiration = googleAccessToken.getExpiration();

            if (expiration.isAfter(LocalDateTime.now())) {
                return googleAccessToken.getAccessToken();
            } else {
                return this.updateGoogleAccessToken(googleAccessToken, googleAuth);
            }
        }

        return this.createGoogleAccessToken(googleAuth);
    }

    private String updateGoogleAccessToken(GoogleAccessToken googleAccessToken, GoogleAuth googleAuth) {
        final GoogleTokenResponse googleTokenResponse =
                googleOAuthCommunication.getNewAccessTokenWithRefreshToken(googleAuth.getRefreshToken());

        final String accessToken = googleTokenResponse.getAccessToken();

        googleAccessToken.setAccessToken(accessToken);

        googleAccessTokenRepository.save(googleAccessToken);

        return accessToken;
    }

    private String createGoogleAccessToken(GoogleAuth googleAuth) {
        final GoogleTokenResponse googleTokenResponse =
                googleOAuthCommunication.getNewAccessTokenWithRefreshToken(googleAuth.getRefreshToken());

        final String accessToken = googleTokenResponse.getAccessToken();
        final LocalDateTime expiration = googleOAuthService.getExpiresDateFromToken(googleTokenResponse);

        final GoogleAccessToken googleAccessToken = new GoogleAccessToken();
        googleAccessToken.setAccessToken(accessToken);
        googleAccessToken.setExpiration(expiration);
        googleAccessToken.setGoogleAuth(googleAuth);

        googleAccessTokenRepository.save(googleAccessToken);

        return accessToken;
    }
}
