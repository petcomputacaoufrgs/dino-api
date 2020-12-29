package br.ufrgs.inf.pet.dinoapi.service.auth.google;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.auth.GoogleAuthRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.auth.GoogleAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.auth.GoogleGrantRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.refresh_auth.GoogleRefreshAuthResponseModel;
import org.springframework.http.ResponseEntity;

public interface GoogleAuthService {
    ResponseEntity<GoogleAuthResponseModel> googleAuthRequest(GoogleAuthRequestModel googleAuthRequestDataModel);

    ResponseEntity<GoogleAuthResponseModel> googleGrantRequest(GoogleGrantRequestModel googleGrantRequestModel);

    ResponseEntity<GoogleRefreshAuthResponseModel> googleRefreshAuth();

    GoogleAuth getUserGoogleAuth(Auth auth) throws AuthNullException;

    GoogleAuth save(GoogleAuth googleAuth);
}
