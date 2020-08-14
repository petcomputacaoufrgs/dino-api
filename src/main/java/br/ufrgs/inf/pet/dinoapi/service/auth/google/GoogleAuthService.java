package br.ufrgs.inf.pet.dinoapi.service.auth.google;

import br.ufrgs.inf.pet.dinoapi.entity.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleAuthRequestModel;
import org.springframework.http.ResponseEntity;

public interface GoogleAuthService {

    ResponseEntity<?> googleSignIn(GoogleAuthRequestModel token);

    ResponseEntity<?> googleRefreshAuth();

    GoogleAuth getUserGoogleAuth();
}
