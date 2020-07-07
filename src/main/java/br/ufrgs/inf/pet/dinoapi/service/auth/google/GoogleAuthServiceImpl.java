package br.ufrgs.inf.pet.dinoapi.service.auth.google;

import br.ufrgs.inf.pet.dinoapi.communication.google.GoogleAPICommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.entity.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.exception.GoogleClientSecretIOException;
import br.ufrgs.inf.pet.dinoapi.model.auth.GoogleAuthRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.GoogleAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.repository.GoogleAuthRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.dino.AuthServiceImpl;
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
import java.util.Optional;

@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    AuthServiceImpl authService;

    @Autowired
    GoogleAuthRepository googleAuthRepository;

    final GoogleAPICommunicationImpl googleAPICommunicationImpl = new GoogleAPICommunicationImpl();

    public ResponseEntity<?> googleSignIn(GoogleAuthRequestModel authModel) {
        try {
            GoogleTokenResponse tokenResponse = googleAPICommunicationImpl.getGoogleToken(authModel.getToken());

            if (tokenResponse != null) {

                GoogleIdToken idToken = tokenResponse.parseIdToken();

                GoogleIdToken.Payload payload = idToken.getPayload();

                String googleId = payload.getSubject();

                GoogleAuth googleAuth;
                User userDB;

                String pictureUrl = (String) payload.get("picture");
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String refreshToken = tokenResponse.getRefreshToken();

                Optional<GoogleAuth> googleAuthSearchResult = googleAuthRepository.findByGoogleId(googleId);

                if (googleAuthSearchResult.isPresent()) {
                    googleAuth = googleAuthSearchResult.get();

                    if (isWithRefreshTokenPresent(refreshToken)) {
                        googleAuth.setRefreshToken(refreshToken);
                    } else if (googleAuth.getRefreshToken().isEmpty()) {
                        return getRefreshTokenError();
                    }

                    userDB = googleAuth.getUser();

                    updateUserData(payload, userDB);

                } else {
                    if (isWithRefreshTokenEmpty(refreshToken)) {
                        return getRefreshTokenError();
                    }

                    userDB = new User(name, email);

                    userService.save(userDB);

                    googleAuth = new GoogleAuth(googleId, refreshToken);

                    googleAuth.setUser(userDB);

                }

                updateGoogleAccessTokenInfo(tokenResponse, googleAuth);

                googleAuthRepository.save(googleAuth);

                Auth auth = authService.generateAuth(userDB);

                GoogleAuthResponseModel response = new GoogleAuthResponseModel();

                response.setAccessToken(auth.getAccessToken());

                response.setGoogleAccessToken(googleAuth.getAccessToken());

                response.setEmail(email);

                response.setName(name);

                response.setPictureUrl(pictureUrl);

                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (GoogleClientSecretIOException e) {
            return new ResponseEntity<>("Erro interno ao ler os dados de autenticação da aplicação com o Google", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            return new ResponseEntity<>("Erro ao resgatar dados da autenticação com o Google.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Erro na autenticação com a API do Google.", HttpStatus.BAD_REQUEST);
    }

    @Override
    public String refreshGoogleAuth(GoogleAuth googleAuth) {
        if (googleAuth != null) {
            GoogleTokenResponse tokenResponse = googleAPICommunicationImpl.refreshAccessToken(googleAuth.getRefreshToken());

            if (tokenResponse != null) {
                updateGoogleAccessTokenInfo(tokenResponse, googleAuth);

                googleAuthRepository.save(googleAuth);

                return googleAuth.getAccessToken();
            }
        }
        return null;
    }

    @Override
    public GoogleAuth getUserGoogleAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();

            if (userDetails != null) {
                User userDB = userService.findUserByEmail(userDetails.getUsername());
                return userDB.getGoogleAuth();
            }
        }

        return null;
    }

    private Boolean isWithRefreshTokenEmpty(String refreshToken) {
        return refreshToken == null || refreshToken.isEmpty();
    }

    private Boolean isWithRefreshTokenPresent(String refreshToken) {
        return !isWithRefreshTokenEmpty(refreshToken);
    }

    private ResponseEntity<?> getRefreshTokenError() {
        return new ResponseEntity<>("Refresh token perdido. Por favor, requira um novo.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    /**
     * Soma a data atual com o tempo do token expirar (converte ele de segundos para milisegundos antes)
     * @param expiresIn - Tempo até o token expirar
     * @return Data de expiração do token
     */
    private Long getTokenExpirationDateInMS(Long expiresIn) {
        return (new Date()).getTime() + (expiresIn * 1000);
    }

    private void updateUserData(GoogleIdToken.Payload payload, User user) {
        String email = payload.getEmail();
        String name = (String) payload.get("name");

        if (!user.getEmail().equals(email)) {
            user.setEmail(email);
        }
        if (!user.getName().equals(name)) {
            user.setName(name);
        }
    }

    private void updateGoogleAccessTokenInfo(GoogleTokenResponse tokenResponse, GoogleAuth googleAuth) {
        String accessToken = tokenResponse.getAccessToken();

        Long expiresIn = tokenResponse.getExpiresInSeconds();

        Long tokenExpiresDateInMillis = getTokenExpirationDateInMS(expiresIn);

        googleAuth.setAccessToken(accessToken);

        googleAuth.setTokenExpiresDateInMillis(tokenExpiresDateInMillis);
    }
}
