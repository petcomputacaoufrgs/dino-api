package br.ufrgs.inf.pet.dinoapi.service.auth.google;

import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleAuthRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleGrantRequestModel;
import org.springframework.http.ResponseEntity;

public interface GoogleAuthService {
    ResponseEntity<?> googleAuthRequest(GoogleAuthRequestModel googleAuthRequestModel);

    ResponseEntity<?> googleGrantRequest(GoogleGrantRequestModel googleGrantRequestModel);

    ResponseEntity<?> googleRefreshAuth();

    GoogleAuth getUserGoogleAuth();

    GoogleAuth save(GoogleAuth googleAuth);
}
