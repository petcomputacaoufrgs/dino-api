package br.ufrgs.inf.pet.dinoapi.service.auth;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.auth.AuthRefreshRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.AuthRefreshResponseModel;
import br.ufrgs.inf.pet.dinoapi.repository.auth.AuthRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;

    private String key = "ie!>[1roh]f!7RmdPpzJ?sAQ(55+#E(RG@LXG*k[CPU4S^35ALLhÇF071[v>p[@t/SX]TD}504T)5|3:iAg2jE/I[yUKN5}N[_iyxç";

    private String secretKey = Base64.getEncoder().encodeToString(key.getBytes());

    private long validityInMilliseconds = 3600000;

    @Autowired
    public AuthServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public Auth generateAuth(User user) {
        Auth auth = new Auth();
        auth.setUser(user);

        List<String> roles = new ArrayList<>();

        this.generateAccessToken(auth, roles);

        return authRepository.save(auth);
    }

    @Override
    public ResponseEntity<?> refreshAuth(AuthRefreshRequestModel authRefreshRequestModel) {
        Optional<Auth> authSearch = authRepository.findByAccessToken(authRefreshRequestModel.getAccessToken());

        if (authSearch.isPresent()) {
            Auth auth = authSearch.get();

            this.generateAccessToken(auth, new ArrayList<>());

            authRepository.save(auth);

            AuthRefreshResponseModel response = new AuthRefreshResponseModel(auth.getAccessToken(), auth.getTokenExpiresDate());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>("Invalid refresh token.", HttpStatus.BAD_REQUEST);
    }

    @Override
    public Auth findByAccessToken(String accessToken) {
        if (accessToken.isEmpty()) {
            return null;
        }

        final Optional<Auth> authSearch = authRepository.findByAccessToken(accessToken);

        if (authSearch.isPresent()) {
            return authSearch.get();
        }

        return null;
    }

    @Override
    public Auth getCurrentAuth() {
        final UserDetails userDetails = this.getPrincipal();

        if (userDetails == null) {
            return null;
        }

        final String accessToken = userDetails.getPassword();

        return findByAccessToken(accessToken);
    }

    @Override
    public User getCurrentUser() {
        final UserDetails userDetails = this.getPrincipal();

        if (userDetails == null) {
            return null;
        }

        final String accessToken = userDetails.getPassword();

        Auth auth = this.findByAccessToken(accessToken);

        if (auth != null) {
            return auth.getUser();
        }

        return null;
    }

    @Override
    public ResponseEntity<?> logout() {
        authRepository.delete(getCurrentAuth());

        return new ResponseEntity<>("Autenticação removida.", HttpStatus.OK);
    }

    @Override
    public UserDetails getPrincipal() {
        final SecurityContext context =  SecurityContextHolder.getContext();

        if (context == null) {
            return null;
        }

        final Authentication auth = context.getAuthentication();

        if (auth == null) {
            return null;
        }

        return (UserDetails) auth.getPrincipal();
    }

    private void generateAccessToken(Auth auth, List<String> roles) {
        final Claims claims = Jwts.claims().setSubject(auth.getUser().getEmail());
        claims.put("roles", roles);
        final Date now = new Date();
        Date expiresDate = new Date(now.getTime() + validityInMilliseconds);

        final String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiresDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        auth.setAccessToken(accessToken);
        auth.setTokenExpiresDate(expiresDate);
        auth.setLastUpdate(now);
    }
}
