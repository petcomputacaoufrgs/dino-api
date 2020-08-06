package br.ufrgs.inf.pet.dinoapi.service.auth.google;

import br.ufrgs.inf.pet.dinoapi.communication.google.GoogleAPICommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.entity.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.exception.GoogleClientSecretIOException;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleAuthRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleRefreshAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.user.UserResponseModel;
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

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {

    private final UserServiceImpl userService;

    private final AuthServiceImpl authService;

    private final GoogleAuthRepository googleAuthRepository;

    private final GoogleAPICommunicationImpl googleAPICommunicationImpl;

    @Autowired
    public GoogleAuthServiceImpl(UserServiceImpl userService, AuthServiceImpl authService, GoogleAuthRepository googleAuthRepository) {
        this.userService = userService;
        this.authService = authService;
        this.googleAuthRepository = googleAuthRepository;
        this.googleAPICommunicationImpl = new GoogleAPICommunicationImpl();
    }

    @Override
    public ResponseEntity<?> googleSignIn(GoogleAuthRequestModel authModel) {
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
                        googleAuthRepository.save(googleAuth);
                    } else if (googleAuth.getRefreshToken().isEmpty()) {
                        return getRefreshTokenError();
                    }

                    user = googleAuth.getUser();

                    user = this.updateUserData(payload, user);
                } else {
                    if (this.isWithRefreshTokenEmpty(refreshToken)) {
                        return getRefreshTokenError();
                    }

                    final String email = payload.getEmail();
                    final String name = (String) payload.get("name");
                    final String pictureUrl = (String) payload.get("picture");

                    user = userService.create(name, email, pictureUrl);

                    googleAuth = new GoogleAuth(googleId, refreshToken, user);

                    googleAuthRepository.save(googleAuth);
                }

                final Auth auth = authService.generateAuth(user);

                final UserResponseModel userResponseModel = new UserResponseModel();

                userResponseModel.setEmail(user.getEmail());

                userResponseModel.setName(user.getName());

                userResponseModel.setPictureURL(user.getPictureURL());

                userResponseModel.setVersion(user.getVersion());

                final GoogleAuthResponseModel response = this.generateGoogleAuthResponse(tokenResponse);

                response.setAccessToken(auth.getAccessToken());

                response.setExpiresDate(auth.getTokenExpiresDate().getTime());

                response.setUser(userResponseModel);

                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (GoogleClientSecretIOException e) {
            return new ResponseEntity<>("Internal auth error.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            return new ResponseEntity<>("Google auth data error.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Google auth error.", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> googleRefreshAuth() {
        GoogleAuth googleAuth = this.getUserGoogleAuth();

        if (googleAuth != null) {
            final GoogleRefreshAuthResponseModel response = this.refreshGoogleAuth(googleAuth);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>("Google auth fail.", HttpStatus.BAD_REQUEST);
    }

    @Override
    public GoogleAuth getUserGoogleAuth() {
        final User user = authService.getCurrentUser();

        if (user != null) {
            return user.getGoogleAuth();
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

    private Date getTokenExpiresDate(Long expiresInSeconds) {
        return new Date(new Date().getTime() + (expiresInSeconds * 1000));
    }

    private User updateUserData(GoogleIdToken.Payload payload, User user) {
        final String email = payload.getEmail();
        final String name = (String) payload.get("name");
        final String pictureUrl = (String) payload.get("picture");

        return userService.update(name, email, pictureUrl);
    }

    private GoogleRefreshAuthResponseModel refreshGoogleAuth(GoogleAuth googleAuth) {
        if (googleAuth != null) {
            final GoogleTokenResponse tokenResponse = googleAPICommunicationImpl.refreshAccessToken(googleAuth.getRefreshToken());

            if (tokenResponse != null) {
                GoogleAuthResponseModel authModel = this.generateGoogleAuthResponse(tokenResponse);

                GoogleRefreshAuthResponseModel refreshModel = new GoogleRefreshAuthResponseModel();
                refreshModel.setGoogleExpiresDate(authModel.getGoogleExpiresDate());
                refreshModel.setGoogleAccessToken(authModel.getGoogleAccessToken());

                return refreshModel;
            }
        }
        return null;
    }

    private GoogleAuthResponseModel generateGoogleAuthResponse(GoogleTokenResponse tokenResponse) {
        GoogleAuthResponseModel response = new GoogleAuthResponseModel();

        final String accessToken = tokenResponse.getAccessToken();

        final Long expiresInSeconds = tokenResponse.getExpiresInSeconds();

        final Date tokenExpiresDate = this.getTokenExpiresDate(expiresInSeconds);

        response.setGoogleAccessToken(accessToken);

        response.setGoogleExpiresDate(tokenExpiresDate.getTime());

        return response;
    }
}
