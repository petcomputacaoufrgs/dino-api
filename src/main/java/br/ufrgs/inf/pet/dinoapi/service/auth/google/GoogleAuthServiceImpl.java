package br.ufrgs.inf.pet.dinoapi.service.auth.google;

import br.ufrgs.inf.pet.dinoapi.communication.google.GoogleAPICommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.constants.GoogleAuthConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleScope;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.enumerable.GoogleAuthErrorCode;
import br.ufrgs.inf.pet.dinoapi.exception.GoogleClientSecretIOException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.*;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.auth.GoogleAuthRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.auth.GoogleAuthResponseDataModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.auth.GoogleAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.auth.GoogleGrantRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.refresh_auth.GoogleRefreshAuthResponseDataModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.refresh_auth.GoogleRefreshAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.SynchronizableGenericResponseModelImpl;
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

    private final ClockServiceImpl clockService;

    @Autowired
    public GoogleAuthServiceImpl(UserServiceImpl userService, AuthServiceImpl authService,
                                 GoogleAuthRepository googleAuthRepository, GoogleScopeServiceImpl googleScopeService,
                                 GoogleAPICommunicationImpl googleAPICommunicationImpl,
                                 ClockServiceImpl clockService) {
        this.userService = userService;
        this.authService = authService;
        this.googleAuthRepository = googleAuthRepository;
        this.googleScopeService = googleScopeService;
        this.googleAPICommunicationImpl = googleAPICommunicationImpl;
        this.clockService = clockService;
    }

    @Override
    public ResponseEntity<GoogleAuthResponseModel> googleAuthRequest(GoogleAuthRequestModel googleAuthRequestDataModel) {
        final GoogleAuthResponseModel response = new GoogleAuthResponseModel();

        try {
            final List<String> scopeList = new ArrayList<>();

            final String code = googleAuthRequestDataModel.getCode();

            final GoogleTokenResponse tokenResponse = googleAPICommunicationImpl.getGoogleToken(code, scopeList);

            if (tokenResponse != null) {
                final GoogleIdToken idToken = tokenResponse.parseIdToken();

                final GoogleIdToken.Payload payload = idToken.getPayload();

                final String googleId = payload.getSubject();

                final String refreshToken = tokenResponse.getRefreshToken();

                final List<String> currentScopes = Arrays.asList(tokenResponse.getScope().split(" "));

                GoogleAuth googleAuth = this.getGoogleAuthByGoogleId(googleId);
                User user;
                Auth auth;

                if (googleAuth != null) {
                    if (this.isWithRefreshTokenPresent(refreshToken)) {
                        googleAuth.setRefreshToken(refreshToken);
                        googleAuth = googleAuthRepository.save(googleAuth);
                    } else if (googleAuth.getRefreshToken().isEmpty()) {
                        return getRefreshTokenError(response, payload);
                    }

                    auth = authService.generateAuth(googleAuth.getUser());
                    user = this.updateUser(payload, auth);
                } else {
                    if (this.isWithRefreshTokenEmpty(refreshToken)) {
                        return getRefreshTokenError(response, payload);
                    }

                    user = this.createUser(payload);

                    googleAuth = new GoogleAuth(googleId, refreshToken, user);
                    user.setGoogleAuth(googleAuthRepository.save(googleAuth));

                    auth = authService.generateAuth(googleAuth.getUser());
                }

                final ClockServiceImpl clock = new ClockServiceImpl();

                final Claims claims = authService.decodeAccessToken(auth.getAccessToken());

                final UserDataModel userData = new UserDataModel();

                final List<GoogleScopeDataModel> savedScopes = this.saveAllScopes(currentScopes, auth);

                userData.setEmail(user.getEmail());

                userData.setName(user.getName());

                userData.setPictureURL(user.getPictureURL());

                userData.setLastUpdate(clockService.getUTCZonedDateTime());

                userData.setId(user.getId());

                final GoogleAuthResponseDataModel dataResponse = this.generateGoogleAuthResponseData(tokenResponse);

                final String accessToken = tokenResponse.getAccessToken();

                final LocalDateTime expiresDate = this.getExpiresDateFromToken(tokenResponse);

                final LocalDateTime dinoTokenExpiresDate = clock.toLocalDateTime(claims.getExpiration());

                dataResponse.setGoogleAccessToken(accessToken);

                dataResponse.setGoogleExpiresDate(clockService.toUTCZonedDateTime(expiresDate));

                dataResponse.setAccessToken(auth.getAccessToken());

                dataResponse.setRefreshToken(auth.getRefreshToken());

                dataResponse.setExpiresDate(clockService.toUTCZonedDateTime(dinoTokenExpiresDate));

                dataResponse.setUser(userData);

                dataResponse.setScopes(savedScopes);

                response.setSuccess(true);
                response.setData(dataResponse);

                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (GoogleClientSecretIOException e) {
            this.setExceptionError(response);
            response.setError(GoogleAuthConstants.INTERNAL_AUTH_ERROR);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AuthNullException | ConvertModelToEntityException e) {
            this.setExceptionError(response);
            response.setError(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            this.setExceptionError(response);
            response.setError(GoogleAuthConstants.INVALID_GOOGLE_AUTH_DATA);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            this.setExceptionError(response);
            response.setError(GoogleAuthConstants.UNKNOWN_EXCEPTION);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        this.setExceptionError(response);
        response.setError(GoogleAuthConstants.GOOGLE_AUTH_ERROR);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<GoogleAuthResponseModel> googleGrantRequest(GoogleGrantRequestModel googleGrantRequestModel) {
        final GoogleAuthResponseModel response = new GoogleAuthResponseModel();
        try {
            final Auth auth = authService.getCurrentAuth();

            final GoogleAuth googleAuth = this.getUserGoogleAuth(auth);

            if (googleAuth == null) {
                return this.googleAuthRequest(googleGrantRequestModel);
            }

            final List<String> scopeList = googleGrantRequestModel.getScopeList();

            final String code = googleGrantRequestModel.getCode();

            final GoogleTokenResponse tokenResponse = googleAPICommunicationImpl.getGoogleToken(code, scopeList);

            if (tokenResponse != null) {
                final GoogleIdToken idToken = tokenResponse.parseIdToken();

                final GoogleIdToken.Payload payload = idToken.getPayload();

                if (this.grantUserIsCurrentUser(payload, auth)) {
                    final String refreshToken = tokenResponse.getRefreshToken();

                    if (this.isWithRefreshTokenPresent(refreshToken)) {
                        googleAuth.setRefreshToken(refreshToken);
                        googleAuthRepository.save(googleAuth);
                    }

                    final List<String> currentScopes = Arrays.asList(tokenResponse.getScope().split(" "));

                    final List<GoogleScopeDataModel> savedScopes = this.saveAllScopes(currentScopes, auth);

                    final GoogleAuthResponseDataModel responseData = new GoogleAuthResponseDataModel();

                    final String accessToken = tokenResponse.getAccessToken();

                    final LocalDateTime expiresDate = this.getExpiresDateFromToken(tokenResponse);

                    responseData.setGoogleAccessToken(accessToken);
                    responseData.setGoogleExpiresDate(clockService.toUTCZonedDateTime(expiresDate));
                    responseData.setScopes(savedScopes);

                    response.setSuccess(true);
                    response.setData(responseData);

                    return new ResponseEntity<>(response, HttpStatus.OK);
                }

                response.setSuccess(false);
                response.setError(auth.getUser().getEmail());
                response.setErrorCode(GoogleAuthErrorCode.INVALID_GOOGLE_GRANT_USER.getValue());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (GoogleClientSecretIOException e) {
            this.setExceptionError(response);
            response.setError(GoogleAuthConstants.INTERNAL_AUTH_ERROR);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AuthNullException | ConvertModelToEntityException e) {
            this.setExceptionError(response);
            response.setError(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            this.setExceptionError(response);
            response.setError(GoogleAuthConstants.INVALID_GOOGLE_AUTH_DATA);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        this.setExceptionError(response);
        response.setError(GoogleAuthConstants.GOOGLE_AUTH_ERROR);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GoogleRefreshAuthResponseModel> googleRefreshAuth() {
        final GoogleRefreshAuthResponseModel response = new GoogleRefreshAuthResponseModel();
        try {
            final Auth auth = authService.getCurrentAuth();

            GoogleAuth googleAuth = this.getUserGoogleAuth(auth);

            if (googleAuth != null) {
                try {
                    final GoogleRefreshAuthResponseDataModel responseData = this.refreshGoogleAuth(googleAuth);
                    response.setSuccess(true);
                    response.setData(responseData);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (ConvertModelToEntityException | AuthNullException e) {
                    this.setExceptionError(response);
                    response.setError(e.getMessage());
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }

            response.setError(GoogleAuthConstants.GOOGLE_AUTH_FAIL);
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            this.setExceptionError(response);
            response.setError(GoogleAuthConstants.UNKNOWN_EXCEPTION);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @Override
    public GoogleAuth getUserGoogleAuth(Auth auth) throws AuthNullException {
        if (auth != null) {
            return auth.getUser().getGoogleAuth();
        }

        throw new AuthNullException();
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

    private boolean grantUserIsCurrentUser(GoogleIdToken.Payload payload, Auth auth) {
        final String email = payload.getEmail();

        final User user = auth.getUser();

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

    private ResponseEntity<GoogleAuthResponseModel> getRefreshTokenError(GoogleAuthResponseModel response, GoogleIdToken.Payload payload) {
        final String email = payload.getEmail();

        response.setSuccess(false);
        response.setError(email);
        response.setErrorCode(GoogleAuthErrorCode.REFRESH_TOKEN.getValue());
        return new ResponseEntity<>(response, HttpStatus.OK);
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

    private GoogleRefreshAuthResponseDataModel refreshGoogleAuth(GoogleAuth googleAuth) throws AuthNullException, ConvertModelToEntityException {
        if (googleAuth != null) {
            final GoogleTokenResponse tokenResponse = googleAPICommunicationImpl.refreshAccessToken(googleAuth.getRefreshToken());
            final List<String> currentScopes = Arrays.asList(tokenResponse.getScope().split(" "));

            final Auth auth = authService.getCurrentAuth();

            final List<GoogleScopeDataModel> savedScopes = this.saveAllScopes(currentScopes, auth);

            final GoogleAuthResponseDataModel authModel = this.generateGoogleAuthResponseData(tokenResponse);

            GoogleRefreshAuthResponseDataModel response = new GoogleRefreshAuthResponseDataModel();
            response.setGoogleExpiresDate(authModel.getGoogleExpiresDate());
            response.setGoogleAccessToken(authModel.getGoogleAccessToken());
            response.setScopes(savedScopes);

            return response;
        }
        return null;
    }

    private GoogleAuthResponseDataModel generateGoogleAuthResponseData(GoogleTokenResponse tokenResponse) {
        GoogleAuthResponseDataModel response = new GoogleAuthResponseDataModel();

        final String accessToken = tokenResponse.getAccessToken();

        final LocalDateTime expiresDate = this.getExpiresDateFromToken(tokenResponse);

        response.setGoogleAccessToken(accessToken);

        response.setGoogleExpiresDate(clockService.toUTCZonedDateTime(expiresDate));

        return response;
    }

    private void setExceptionError(SynchronizableGenericResponseModelImpl response) {
        //TODO Log API Error
        response.setSuccess(false);
        response.setErrorCode(GoogleAuthErrorCode.EXCEPTION.getValue());
    }

    private LocalDateTime getExpiresDateFromToken(GoogleTokenResponse tokenResponse) {
        final Long googleExpiresDateInSeconds = tokenResponse.getExpiresInSeconds();

        final LocalDateTime localDateTime = LocalDateTime.now();

        return localDateTime.plusSeconds(googleExpiresDateInSeconds);
    }
}
