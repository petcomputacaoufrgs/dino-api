package br.ufrgs.inf.pet.dinoapi.controller.auth;

import br.ufrgs.inf.pet.dinoapi.model.auth.AuthRequestModel;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Implementação de: {@link AuthController}
 *
 * @author joao.silva
 */
@RestController
@RequestMapping("/auth/")
public class AuthControllerImpl implements AuthController {

    @Autowired
    AuthServiceImpl authService;

    @Override
    @PostMapping("google/")
    public ResponseEntity<?> authRequestGoogleSign(@RequestBody AuthRequestModel authRequestMode) {
        return authService.authRequestGoogleSign(authRequestMode);
    }
}
