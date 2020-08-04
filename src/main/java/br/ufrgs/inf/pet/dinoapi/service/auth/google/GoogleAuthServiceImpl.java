package br.ufrgs.inf.pet.dinoapi.service.auth.google;

import br.ufrgs.inf.pet.dinoapi.communication.google.GoogleAPICommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.entity.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.exception.GoogleClientSecretIOException;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleAuthRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleRefreshAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.user.UserModel;
import br.ufrgs.inf.pet.dinoapi.repository.GoogleAuthRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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

    @Override
    public ResponseEntity<?> googleSignIn(GoogleAuthRequestModel authModel, HttpServletRequest request) {
        try {
            final GoogleTokenResponse tokenResponse = googleAPICommunicationImpl.getGoogleToken(authModel.getToken());

            if (tokenResponse != null) {

                final GoogleIdToken idToken = tokenResponse.parseIdToken();

                final GoogleIdToken.Payload payload = idToken.getPayload();

                final String googleId = payload.getSubject();

                final String refreshToken = tokenResponse.getRefreshToken();

                GoogleAuth googleAuth = this.getGoogleAuthByGoogleId(googleId);

                User user;

                if (googleAuth != null) {

                    if (this.isWithRefreshTokenPresent(refreshToken)) {
                        googleAuth.setRefreshToken(refreshToken);
                    } else if (googleAuth.getRefreshToken().isEmpty()) {
                        return getRefreshTokenError();
                    }

                    user = googleAuth.getUser();

                    user = this.updateUserData(payload, user);
                } else {
                    if (isWithRefreshTokenEmpty(refreshToken)) {
                        return getRefreshTokenError();
                    }

                    final String email = payload.getEmail();
                    final String name = (String) payload.get("name");
                    final String pictureUrl = (String) payload.get("picture");

                    user = userService.create(name, email, pictureUrl);

                    googleAuth = new GoogleAuth(googleId, refreshToken);

                    googleAuth.setUser(user);
                }

                googleAuth = this.updateGoogleAccessTokenData(tokenResponse, googleAuth);

                final Auth auth = authService.generateAuth(user, request);

                final UserModel userModel = new UserModel();

                userModel.setEmail(user.getEmail());

                userModel.setName(user.getName());

                userModel.setPictureURL(user.getPictureURL());

                userModel.setVersion(user.getVersion());

                final GoogleAuthResponseModel response = new GoogleAuthResponseModel();

                response.setAccessToken(auth.getAccessToken());

                response.setGoogleAccessToken(googleAuth.getAccessToken());

                response.setGoogleExpiresDate(googleAuth.getTokenExpiresDateInMillis());

                response.setUser(userModel);

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
    public ResponseEntity<?> googleRefreshAuth() {
        GoogleAuth googleAuth = this.getUserGoogleAuth();

        if (googleAuth != null) {
            googleAuth = refreshGoogleAuth(googleAuth);

            GoogleRefreshAuthResponseModel response = new GoogleRefreshAuthResponseModel();
            response.setGoogleAccessToken(googleAuth.getAccessToken());
            response.setGoogleExpiresDate(googleAuth.getTokenExpiresDateInMillis());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>("Falha na autenticação com o Google.", HttpStatus.BAD_REQUEST);
    }

    @Override
    public GoogleAuth refreshGoogleAuth(GoogleAuth googleAuth) {
        if (googleAuth != null) {
            final GoogleTokenResponse tokenResponse = googleAPICommunicationImpl.refreshAccessToken(googleAuth.getRefreshToken());

            if (tokenResponse != null) {
                this.updateGoogleAccessTokenData(tokenResponse, googleAuth);

                return googleAuth;
            }
        }
        return null;
    }

    @Override
    public GoogleAuth getUserGoogleAuth() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();

            if (userDetails != null) {
                final User userDB = userService.findUserByEmail(userDetails.getUsername());
                return userDB.getGoogleAuth();
            }
        }

        return null;
    }

    private GoogleAuth getGoogleAuthByGoogleId(String googleId) {
        final Optional<GoogleAuth> googleAuthSearchResult = googleAuthRepository.findByGoogleId(googleId);

        if (googleAuthSearchResult.isPresent()) {
            return googleAuthSearchResult.get();
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

    private User updateUserData(GoogleIdToken.Payload payload, User user) {
        final String email = payload.getEmail();
        final String name = (String) payload.get("name");
        final String pictureUrl = (String) payload.get("picture");

        return userService.update(name, email, pictureUrl);
    }

    private GoogleAuth updateGoogleAccessTokenData(GoogleTokenResponse tokenResponse, GoogleAuth googleAuth) {
        final String accessToken = tokenResponse.getAccessToken();

        final Long expiresIn = tokenResponse.getExpiresInSeconds();

        final Long tokenExpiresDateInMillis = getTokenExpirationDateInMS(expiresIn);

        googleAuth.setAccessToken(accessToken);

        googleAuth.setTokenExpiresDateInMillis(tokenExpiresDateInMillis);

        return googleAuthRepository.save(googleAuth);
    }
}
