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
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.queue.generic.GenericQueueMessageServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {

    private final UserServiceImpl userService;

    private final AuthServiceImpl authService;

    private final GoogleAuthRepository googleAuthRepository;

    private final GoogleScopeRepository googleScopeRepository;

    private final GoogleAPICommunicationImpl googleAPICommunicationImpl;

    private final GenericQueueMessageServiceImpl genericQueueMessageService;

    @Autowired
    public GoogleAuthServiceImpl(UserServiceImpl userService, AuthServiceImpl authService, GoogleAuthRepository googleAuthRepository, GoogleScopeRepository googleScopeRepository, GoogleAPICommunicationImpl googleAPICommunicationImpl, GenericQueueMessageServiceImpl genericQueueMessageService) {
        this.userService = userService;
        this.authService = authService;
        this.googleAuthRepository = googleAuthRepository;
        this.googleScopeRepository = googleScopeRepository;
        this.googleAPICommunicationImpl = googleAPICommunicationImpl;
        this.genericQueueMessageService = genericQueueMessageService;
    }

    @Override
    public ResponseEntity<?> googleAuthRequest(GoogleAuthRequestModel googleAuthRequestModel) {
        try {
            final List<String> scopeList = new ArrayList<>();

            final String code = googleAuthRequestModel.getCode();

            final GoogleTokenResponse tokenResponse = googleAPICommunicationImpl.getGoogleToken(code, scopeList);

            if (tokenResponse != null) {
                final GoogleIdToken idToken = tokenResponse.parseIdToken();

                final GoogleIdToken.Payload payload = idToken.getPayload();

                final String googleId = payload.getSubject();

                final String refreshToken = tokenResponse.getRefreshToken();

                final GoogleAuth googleAuth = this.getGoogleAuthByGoogleId(googleId);

                final List<String> currentScopes = Arrays.asList(tokenResponse.getScope().split(" "));

                User user;

                if (googleAuth != null) {
                    googleScopeRepository.deleteAllByGoogleAuthId(googleAuth.getId());

                    final List<GoogleScope> newScopes = currentScopes.stream().map(scope -> new GoogleScope(googleAuth, scope))
                            .collect(Collectors.toList());

                    googleScopeRepository.saveAll(newScopes);

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

                    final GoogleAuth newGoogleAuth = googleAuthRepository.save(new GoogleAuth(googleId, refreshToken, user));

                    final List<GoogleScope> newScopes = currentScopes.stream().map(scope -> new GoogleScope(newGoogleAuth, scope))
                            .collect(Collectors.toList());

                    googleScopeRepository.saveAll(newScopes);
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

                response.setScopeList(currentScopes);

                if (googleAuth != null) {
                    response.setDeclinedContactsGrant(googleAuth.isDeclinedContatsGrant());
                } else {
                    response.setDeclinedContactsGrant(false);
                }

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
            final GoogleAuth googleAuth = this.getUserGoogleAuth();

            if (googleAuth == null) {
                return this.googleAuthRequest(googleGrantRequestModel);
            }

            final List<String> scopeList = googleGrantRequestModel.getScopeList();

            final String code = googleGrantRequestModel.getCode();

            final GoogleTokenResponse tokenResponse = googleAPICommunicationImpl.getGoogleToken(code, scopeList);

            if (tokenResponse != null) {
                final GoogleIdToken idToken = tokenResponse.parseIdToken();

                final GoogleIdToken.Payload payload = idToken.getPayload();

                if (this.grantUserIsCurrentUser(payload)) {
                    final String refreshToken = tokenResponse.getRefreshToken();

                    if (this.isWithRefreshTokenPresent(refreshToken)) {
                        googleAuth.setRefreshToken(refreshToken);
                        googleAuthRepository.save(googleAuth);
                    }

                    final List<String> currentScopes = Arrays.asList(tokenResponse.getScope().split(" "));

                    googleScopeRepository.deleteAllByGoogleAuthId(googleAuth.getId());

                    final List<GoogleScope> newScopes = currentScopes.stream().map(scope -> new GoogleScope(googleAuth, scope))
                            .collect(Collectors.toList());

                    googleScopeRepository.saveAll(newScopes);

                    this.saveGrantConfig(scopeList, googleAuth);

                    final GoogleRefreshAuthResponseModel response = new GoogleRefreshAuthResponseModel();

                    final String accessToken = tokenResponse.getAccessToken();

                    final Long expiresInSeconds = tokenResponse.getExpiresInSeconds();

                    response.setGoogleAccessToken(accessToken);
                    response.setGoogleExpiresDate(expiresInSeconds);
                    response.setScopeList(currentScopes);

                    genericQueueMessageService.sendObjectMessage(null, WebSocketDestinationsEnum.ALERT_AUTH_SCOPE_UPDATE);

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

    @Override
    public GoogleAuth save(GoogleAuth googleAuth) {
        return googleAuthRepository.save(googleAuth);
    }

    private void saveGrantConfig(List<String> scopes, GoogleAuth googleAuth) {
        final boolean hasContactGrant = scopes.stream().anyMatch(scope -> scope == GoogleScopeEnum.CONTACTS.getValue());
        boolean changed = false;

        if (hasContactGrant) {
            if (googleAuth.isDeclinedContatsGrant()) {
                changed = true;
                googleAuth.setDeclinedContatsGrant(false);
            }
        }

        if (changed) {
            this.save(googleAuth);
        }
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
        return new ResponseEntity<>(GoogleAuthConstants.REFRESH_TOKEN_NECESSARY, HttpStatus.ACCEPTED);
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
            final List<String> currentScopes = Arrays.asList(tokenResponse.getScope().split(" "));

            if (tokenResponse != null) {
                GoogleAuthResponseModel authModel = this.generateGoogleAuthResponse(tokenResponse);

                GoogleRefreshAuthResponseModel response = new GoogleRefreshAuthResponseModel();
                response.setGoogleExpiresDate(authModel.getGoogleExpiresDate());
                response.setGoogleAccessToken(authModel.getGoogleAccessToken());
                response.setScopeList(currentScopes);
                response.setDeclinedContatsGrant(googleAuth.isDeclinedContatsGrant());

                return response;
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
