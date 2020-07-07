package br.ufrgs.inf.pet.dinoapi.controller.auth;

import br.ufrgs.inf.pet.dinoapi.model.auth.GoogleAuthRequestModel;
import br.ufrgs.inf.pet.dinoapi.service.auth.dino.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.auth.google.GoogleAuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/")
public class AuthControllerImpl implements AuthController {

    @Autowired
    GoogleAuthServiceImpl googleAuthService;

    @Autowired
    AuthServiceImpl authService;

    @Override
    @PostMapping("google/")
    public ResponseEntity<?> googleAuthRequest(@RequestBody GoogleAuthRequestModel authRequestMode) {
        return googleAuthService.googleSignIn(authRequestMode);
    }

    @Override
    @PutMapping("logout/")
    public ResponseEntity<?> logout() {
        return authService.logout();
    }
}
