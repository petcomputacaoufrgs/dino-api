package br.ufrgs.inf.pet.dinoapi.controller.auth;

import br.ufrgs.inf.pet.dinoapi.model.auth.AuthRefreshRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.AuthRefreshResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.auth.GoogleAuthRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.auth.GoogleAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.auth.GoogleGrantRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.refresh_auth.GoogleRefreshAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.web_socket.WebSocketAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.auth.google.GoogleAuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
public class AuthControllerImpl implements AuthController {

    private final GoogleAuthServiceImpl googleAuthService;

    private final AuthServiceImpl authService;

    @Autowired
    public AuthControllerImpl(GoogleAuthServiceImpl googleAuthService, AuthServiceImpl authService) {
        this.googleAuthService = googleAuthService;
        this.authService = authService;
    }

    @Override
    @PostMapping("/public/auth/google/")
    public ResponseEntity<GoogleAuthResponseModel> googleAuthRequest(@Valid @RequestBody GoogleAuthRequestModel googleAuthRequestDataModel) {
        return googleAuthService.googleAuthRequest(googleAuthRequestDataModel);
    }

    @Override
    @PutMapping("/public/auth/refresh/")
    public ResponseEntity<AuthRefreshResponseModel> refreshAuth(@Valid @RequestBody AuthRefreshRequestModel authRefreshRequestModel) {
        return authService.refreshAuth(authRefreshRequestModel);
    }

    @Override
    @PostMapping("/private/auth/google/grant/")
    public ResponseEntity<GoogleAuthResponseModel> googleGrantRequest(@Valid @RequestBody GoogleGrantRequestModel googleGrantRequestModel) {
        return googleAuthService.googleGrantRequest(googleGrantRequestModel);
    }

    @Override
    @GetMapping("/private/auth/google/")
    public ResponseEntity<GoogleRefreshAuthResponseModel> googleRefreshAuth() {
        return googleAuthService.googleRefreshAuth();
    }

    @Override
    @GetMapping("/private/auth/web_socket/")
    public ResponseEntity<WebSocketAuthResponseModel> webSocketAuthRequest() {
        return authService.webSocketAuthRequest();
    }
}
