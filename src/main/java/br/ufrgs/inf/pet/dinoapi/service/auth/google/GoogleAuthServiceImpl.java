package br.ufrgs.inf.pet.dinoapi.service.auth.google;

import br.ufrgs.inf.pet.dinoapi.communication.google.GoogleAPICommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.constants.GoogleAuthConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleScope;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.exception.GoogleClientSecretIOException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.*;
import br.ufrgs.inf.pet.dinoapi.model.user.UserDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.auth.google.GoogleAuthRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {

    private final UserServiceImpl userService;

    private final AuthServiceImpl authService;

    private final GoogleAuthRepository googleAuthRepository;

    private final GoogleScopeServiceImpl googleScopeService;

    private final GoogleAPICommunicationImpl googleAPICommunicationImpl;

    @Autowired
    public GoogleAuthServiceImpl(UserServiceImpl userService, AuthServiceImpl authService,
                                 GoogleAuthRepository googleAuthRepository, GoogleScopeServiceImpl googleScopeService,
                                 GoogleAPICommunicationImpl googleAPICommunicationImpl) {
        this.userService = userService;
        this.authService = authService;
        this.googleAuthRepository = googleAuthRepository;
        this.googleScopeService = googleScopeService;
        this.googleAPICommunicationImpl = googleAPICommunicationImpl;
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

                final List<String> currentScopes = Arrays.asList(tokenResponse.getScope().split(" "));

                boolean updateUser = false;

                GoogleAuth googleAuth = this.getGoogleAuthByGoogleId(googleId);

                User user;

                if (googleAuth != null) {
                    if (this.isWithRefreshTokenPresent(refreshToken)) {
                        googleAuth.setRefreshToken(refreshToken);
                        googleAuth = googleAuthRepository.save(googleAuth);
                    } else if (googleAuth.getRefreshToken().isEmpty()) {
                        return getRefreshTokenError();
                    }

                    user = googleAuth.getUser();

                    updateUser = true;
                } else {
                    if (this.isWithRefreshTokenEmpty(refreshToken)) {
                        return getRefreshTokenError();
                    }

                    user = this.createUser(payload);

                    googleAuth = new GoogleAuth(googleId, refreshToken, user);
                    user.setGoogleAuth(googleAuthRepository.save(googleAuth));
                }

                final Auth auth = authService.generateAuth(user);

                final Claims claims = authService.decodeAccessToken(auth.getAccessToken());

                final UserDataModel userData = new UserDataModel();

                if (updateUser) {
                    user = this.updateUser(payload, auth);
                }

                final List<GoogleScopeDataModel> savedScopes = this.saveAllScopes(currentScopes, auth);

                userData.setEmail(user.getEmail());

                userData.setName(user.getName());

                userData.setPictureURL(user.getPictureURL());

                userData.setLastUpdate(LocalDateTime.now());

                userData.setId(user.getId());

                final GoogleAuthResponseModel response = this.generateGoogleAuthResponse(tokenResponse);

                final String accessToken = tokenResponse.getAccessToken();

                final Long expiresInSeconds = tokenResponse.getExpiresInSeconds();

                response.setGoogleAccessToken(accessToken);

                response.setGoogleExpiresDate(expiresInSeconds);

                response.setAccessToken(auth.getAccessToken());

                response.setExpiresDate(claims.getExpiration().getTime());

                response.setUser(userData);

                response.setScopes(savedScopes);

                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (GoogleClientSecretIOException e) {
            return new ResponseEntity<>(GoogleAuthConstants.INTERNAL_AUTH_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AuthNullException | ConvertModelToEntityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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

                    final Auth auth = authService.getCurrentAuth();

                    final List<GoogleScopeDataModel> savedScopes = this.saveAllScopes(currentScopes, auth);

                    final GoogleRefreshAuthResponseModel response = new GoogleRefreshAuthResponseModel();

                    final String accessToken = tokenResponse.getAccessToken();

                    final Long expiresInSeconds = tokenResponse.getExpiresInSeconds();

                    response.setGoogleAccessToken(accessToken);
                    response.setGoogleExpiresDate(expiresInSeconds);
                    response.setScopes(savedScopes);

                    return new ResponseEntity<>(response, HttpStatus.OK);
                }

                return new ResponseEntity<>(GoogleAuthConstants.INVALID_GOOGLE_GRANT_USER, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            }
        } catch (GoogleClientSecretIOException e) {
            return new ResponseEntity<>(GoogleAuthConstants.INTERNAL_AUTH_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AuthNullException | ConvertModelToEntityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>(GoogleAuthConstants.INVALID_GOOGLE_AUTH_DATA, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(GoogleAuthConstants.GOOGLE_AUTH_ERROR, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> googleRefreshAuth() {
        GoogleAuth googleAuth = this.getUserGoogleAuth();

        if (googleAuth != null) {
            try {
                final GoogleRefreshAuthResponseModel response = this.refreshGoogleAuth(googleAuth);

                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (ConvertModelToEntityException | AuthNullException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
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

    private List<GoogleScopeDataModel> saveAllScopes(List<String> currentScopes, Auth auth) throws AuthNullException, ConvertModelToEntityException {
        final List<String> scopes = new LinkedList<>(currentScopes);

        List<GoogleScope> databaseScopes = googleScopeService.getEntitiesByUserAuth(auth);

        final List<String> databaseScopesStrings = databaseScopes.stream().map(GoogleScope::getName).collect(Collectors.toList());

        if (databaseScopesStrings.size() > 0) {
            scopes.removeAll(databaseScopesStrings);
            databaseScopesStrings.removeAll(currentScopes);
            if (databaseScopesStrings.size() > 0) {
                final List<GoogleScope> removedScopes = googleScopeService.getEntitiesByName(auth.getUser(), databaseScopesStrings);
                googleScopeService.deleteAllScopes(removedScopes, auth);
                databaseScopes = googleScopeService.getEntitiesByUserAuth(auth);
            }
        }

        Set<String> uniqueScopes = new HashSet<>(scopes);

        final List<GoogleScopeDataModel> allScopes = googleScopeService.internalConvertEntitiesToModels(databaseScopes);

        if (uniqueScopes.size() > 0) {
            final List<GoogleScopeDataModel> newScopes = googleScopeService.saveAllScopes(uniqueScopes, auth);

            if (newScopes != null && newScopes.size() > 0) {
                allScopes.addAll(newScopes);
            }
        }

        return allScopes;
    }

    private boolean grantUserIsCurrentUser(GoogleIdToken.Payload payload) {
        final String email = payload.getEmail();

        final Auth currentAuth = authService.getCurrentAuth();

        final User user = currentAuth.getUser();

        return user.getEmail().equalsIgnoreCase(email);
    }

    private GoogleAuth getGoogleAuthByGoogleId(String googleId) {
        final Optional<GoogleAuth> googleAuthSearchResult = googleAuthRepository.findByGoogleId(googleId);

        return googleAuthSearchResult.orElse(null);
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

    private User updateUser(GoogleIdToken.Payload payload, Auth auth) {
        final String email = payload.getEmail();
        final String name = (String) payload.get("name");
        final String pictureUrl = (String) payload.get("picture");

        return userService.save(name, email, pictureUrl, auth);
    }

    private User createUser(GoogleIdToken.Payload payload) {
        final String email = payload.getEmail();
        final String name = (String) payload.get("name");
        final String pictureUrl = (String) payload.get("picture");

        return userService.saveNew(name, email, pictureUrl);
    }

    private GoogleRefreshAuthResponseModel refreshGoogleAuth(GoogleAuth googleAuth) throws AuthNullException, ConvertModelToEntityException {
        if (googleAuth != null) {
            final GoogleTokenResponse tokenResponse = googleAPICommunicationImpl.refreshAccessToken(googleAuth.getRefreshToken());
            final List<String> currentScopes = Arrays.asList(tokenResponse.getScope().split(" "));

            final Auth auth = authService.getCurrentAuth();

            final List<GoogleScopeDataModel> savedScopes = this.saveAllScopes(currentScopes, auth);

            GoogleAuthResponseModel authModel = this.generateGoogleAuthResponse(tokenResponse);

            GoogleRefreshAuthResponseModel response = new GoogleRefreshAuthResponseModel();
            response.setGoogleExpiresDate(authModel.getGoogleExpiresDate());
            response.setGoogleAccessToken(authModel.getGoogleAccessToken());
            response.setScopes(savedScopes);

            return response;
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
