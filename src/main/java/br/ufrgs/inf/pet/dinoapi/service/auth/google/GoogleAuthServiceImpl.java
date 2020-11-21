package br.ufrgs.inf.pet.dinoapi.service.auth.google;

import br.ufrgs.inf.pet.dinoapi.communication.google.GoogleAPICommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.constants.GoogleAuthConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleScope;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.enumerable.GoogleScopeEnum;
import br.ufrgs.inf.pet.dinoapi.exception.GoogleClientSecretIOException;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.*;
import br.ufrgs.inf.pet.dinoapi.model.user.UserResponseModel;
import br.ufrgs.inf.pet.dinoapi.repository.auth.google.GoogleAuthRepository;
import br.ufrgs.inf.pet.dinoapi.repository.auth.google.GoogleScopeRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {

    private final UserServiceImpl userService;

    private final AuthServiceImpl authService;

    private final GoogleAuthRepository googleAuthRepository;

    private final GoogleScopeRepository googleScopeRepository;

    private final GoogleAPICommunicationImpl googleAPICommunicationImpl;

    @Autowired
    public GoogleAuthServiceImpl(UserServiceImpl userService, AuthServiceImpl authService, GoogleAuthRepository googleAuthRepository, GoogleScopeRepository googleScopeRepository) {
        this.userService = userService;
        this.authService = authService;
        this.googleAuthRepository = googleAuthRepository;
        this.googleScopeRepository = googleScopeRepository;
        this.googleAPICommunicationImpl = new GoogleAPICommunicationImpl();
    }

    @Override
    public ResponseEntity<?> googleAuthRequest(GoogleAuthRequestModel googleAuthRequestModel) {
        return this.googleAuthRequest(googleAuthRequestModel, new ArrayList<>());
    }

    @Override
    public ResponseEntity<?> googleAuthRequest(GoogleAuthRequestModel googleAuthRequestModel, List<String> scopes) {
        try {
            final String code = googleAuthRequestModel.getCode();

            final List<String> newScopes = googleAuthRequestModel.getScopeList();

            final GoogleAuth googleAuth = this.getUserGoogleAuth();

            if (this.isValidScopes(scopes)) {
                return new ResponseEntity<>(GoogleAuthConstants.INVALID_SCOPES, HttpStatus.BAD_REQUEST);
            }

            final GoogleTokenResponse tokenResponse = googleAPICommunicationImpl.getGoogleToken(code, scopes);

            if (tokenResponse != null) {
                final GoogleIdToken idToken = tokenResponse.parseIdToken();

                final GoogleIdToken.Payload payload = idToken.getPayload();

                final String googleId = payload.getSubject();

                final String refreshToken = tokenResponse.getRefreshToken();

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

                final Claims claims = authService.decodeAccessToken(auth.getAccessToken());

                final UserResponseModel userResponseModel = new UserResponseModel();

                userResponseModel.setEmail(user.getEmail());

                userResponseModel.setName(user.getName());

                userResponseModel.setPictureURL(user.getPictureURL());

                userResponseModel.setVersion(user.getVersion());

                final GoogleAuthResponseModel response = this.generateGoogleAuthResponse(tokenResponse);

                response.setAccessToken(auth.getAccessToken());

                response.setExpiresDate(claims.getExpiration().getTime());

                response.setUser(userResponseModel);

                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (GoogleClientSecretIOException e) {
            return new ResponseEntity<>(GoogleAuthConstants.INTERNAL_AUTH_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            return new ResponseEntity<>(GoogleAuthConstants.INVALID_GOOGLE_AUTH_DATA, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(GoogleAuthConstants.GOOGLE_AUTH_ERROR, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> googleGrantRequest(GoogleGrantRequestModel googleGrantRequestModel) {
        try {
            final String code = googleGrantRequestModel.getCode();

            final List<String> newScopes = googleGrantRequestModel.getScopeList();

            final GoogleAuth googleAuth = this.getUserGoogleAuth();

            if (googleAuth == null) {
                return this.googleAuthRequest(googleGrantRequestModel, googleGrantRequestModel.getScopeList());
            }

            if (this.isValidScopes(newScopes)) {
                return new ResponseEntity<>(GoogleAuthConstants.INVALID_SCOPES, HttpStatus.BAD_REQUEST);
            }

            final List<String> requestScopes = googleScopeRepository.findAllNamesByGoogleAuthId(googleAuth.getId());
            final List<GoogleScope> unsavedScopes = new ArrayList<>();

            newScopes.forEach(newScope -> {
                final boolean notSaved = !requestScopes.stream().anyMatch(scope -> scope.equals(newScope));

                if (notSaved) {
                    requestScopes.add(newScope);

                    final GoogleScope googleScope = new GoogleScope(googleAuth, newScope);
                    unsavedScopes.add(googleScope);
                }
            });

            final GoogleTokenResponse tokenResponse = googleAPICommunicationImpl.getGoogleToken(code, requestScopes);

            if (tokenResponse != null) {
                final GoogleIdToken idToken = tokenResponse.parseIdToken();

                final GoogleIdToken.Payload payload = idToken.getPayload();

                if (this.grantUserIsCurrentUser(payload)) {
                    googleScopeRepository.saveAll(unsavedScopes);

                    final GoogleGrantResponseModel response = new GoogleGrantResponseModel();

                    final String accessToken = tokenResponse.getAccessToken();

                    final Long expiresInSeconds = tokenResponse.getExpiresInSeconds();

                    response.setGoogleAccessToken(accessToken);
                    response.setGoogleExpiresDate(expiresInSeconds);

                    return new ResponseEntity<>(response, HttpStatus.OK);
                }

                return new ResponseEntity<>(GoogleAuthConstants.INVALID_GOOGLE_GRANT_USER, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            }
        } catch (GoogleClientSecretIOException e) {
            return new ResponseEntity<>(GoogleAuthConstants.INTERNAL_AUTH_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            return new ResponseEntity<>(GoogleAuthConstants.INVALID_GOOGLE_AUTH_DATA, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(GoogleAuthConstants.GOOGLE_AUTH_ERROR, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> googleRefreshAuth() {
        GoogleAuth googleAuth = this.getUserGoogleAuth();

        if (googleAuth != null) {
            final GoogleRefreshAuthResponseModel response = this.refreshGoogleAuth(googleAuth);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(GoogleAuthConstants.GOOGLE_AUTH_FAIL, HttpStatus.BAD_REQUEST);
    }

    @Override
    public GoogleAuth getUserGoogleAuth() {
        final User user = authService.getCurrentUser();

        if (user != null) {
            return user.getGoogleAuth();
        }

        return null;
    }

    private boolean isValidScopes(List<String> scopes) {
        return scopes.stream().anyMatch(scope -> GoogleScopeEnum.findByScope(scope) == null);
    }

    private boolean grantUserIsCurrentUser(GoogleIdToken.Payload payload) {
        final String email = payload.getEmail();

        final Auth currentAuth = authService.getCurrentAuth();

        final User user = currentAuth.getUser();

        return user.getEmail().equalsIgnoreCase(email);
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
        return new ResponseEntity<>(GoogleAuthConstants.REFRESH_TOKEN_ERROR, HttpStatus.CONTINUE);
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
