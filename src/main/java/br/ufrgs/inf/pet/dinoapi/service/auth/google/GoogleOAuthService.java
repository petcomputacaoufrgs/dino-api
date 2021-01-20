package br.ufrgs.inf.pet.dinoapi.service.auth.google;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleScopeDataModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.auth.GoogleAuthRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.auth.GoogleAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.auth.GoogleGrantRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.refresh_auth.GoogleRefreshAuthResponseModel;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface GoogleOAuthService {
    ResponseEntity<GoogleAuthResponseModel> googleAuthRequest(GoogleAuthRequestModel googleAuthRequestDataModel);

    ResponseEntity<GoogleAuthResponseModel> googleGrantRequest(GoogleGrantRequestModel googleGrantRequestModel);

    ResponseEntity<GoogleRefreshAuthResponseModel> googleRefreshAuth();

    GoogleAuth getUserGoogleAuth(Auth auth) throws AuthNullException;

    GoogleAuth save(GoogleAuth googleAuth);

    /**
     * Save current scopes
     *
     * @param currentScopes current scopes
     * @param auth          user auth
     * @return data model list of new scopes
     * @throws AuthNullException             throw when auth is null
     * @throws ConvertModelToEntityException throw when an error occur in model to entity conversion
     */
    List<GoogleScopeDataModel> saveAllScopes(List<String> currentScopes, Auth auth) throws AuthNullException, ConvertModelToEntityException;

    /**
     * Get Expires data in GoogleTokenResponse
     *
     * @param tokenResponse GoogleTokenResponse of auth request
     * @return token expires date
     */
    LocalDateTime getExpiresDateFromToken(GoogleTokenResponse tokenResponse);
}
