package br.ufrgs.inf.pet.dinoapi.service.auth;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.auth.AuthRefreshRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.AuthRefreshResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.web_socket.WebSocketAuthResponse;
import br.ufrgs.inf.pet.dinoapi.repository.auth.AuthRepository;
import br.ufrgs.inf.pet.dinoapi.configuration.security.DinoCredentials;
import br.ufrgs.inf.pet.dinoapi.configuration.security.DinoUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String KEY = "ie!>[1roh]f!7RmdPpzJ?sAQ(55+#E(RG@LXG*k[CPU4S^35ALLhÇF071[v>p[@t/SX]TD}504T)5|3:iAg2jE/I[yUKN5}N[_iyxç";

    private static final String ENCODED_KEY = Base64.getEncoder().encodeToString(KEY.getBytes());

    private static final String WEB_SOCKET_KEY = "m.|TGrhhXkp+(=Q-{6F{m2KFShSD[[D]WQEL.P[WAS]D@$JHW=qLukasdsdas224334432$@#@#hi/&l{Udnk@!@!@F%4&<0;X3l1gsSd$";

    private static final String WEB_SOCKET_ENCODED_KEY = Base64.getEncoder().encodeToString(WEB_SOCKET_KEY.getBytes());

    private static final long TOKEN_LIFE_TIME_IN_MS = 3600000;

    private final AuthRepository authRepository;

    @Autowired
    public AuthServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public Auth generateAuth(User user) {
        Auth auth = new Auth();
        auth.setUser(user);
        auth.setWebSocketConnected(false);

        List<String> roles = new ArrayList<>();

        this.generateAccessToken(auth, roles);

        return authRepository.save(auth);
    }

    @Override
    public ResponseEntity<WebSocketAuthResponse> webSocketAuthRequest() {
        final Auth auth = this.getCurrentAuth();
        auth.setWebSocketConnected(false);
        this.generateWebSocketToken(auth);

        authRepository.save(auth);

        final WebSocketAuthResponse model = new WebSocketAuthResponse(auth);

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> refreshAuth(AuthRefreshRequestModel authRefreshRequestModel) {
        Optional<Auth> authSearch = authRepository.findByAccessToken(authRefreshRequestModel.getAccessToken());

        if (authSearch.isPresent()) {
            Auth auth = authSearch.get();

            final Date tokenExpiresDate = this.generateAccessToken(auth, new ArrayList<>());

            authRepository.save(auth);

            AuthRefreshResponseModel response = new AuthRefreshResponseModel(auth.getAccessToken(), tokenExpiresDate);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>("Invalid refresh token.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    public Auth findByAccessToken(String accessToken) {
        if (!accessToken.isBlank()) {
            final Optional<Auth> authSearch = authRepository.findByAccessToken(accessToken);

            if (authSearch.isPresent()) {
                return authSearch.get();
            }
        }

        return null;
    }

    @Override
    public Auth findByWebSocketToken(String webSocketToken) {
        if (!webSocketToken.isBlank()) {
            final Optional<Auth> authSearch = authRepository.findByWebSocketToken(webSocketToken);

            if (authSearch.isPresent()) {
                return authSearch.get();
            }
        }

        return null;
    }

    @Override
    public Boolean canConnectToWebSocket(Auth auth) {
        return !auth.getWebSocketConnected();
    }

    @Override
    public Auth getCurrentAuth() {
        final DinoCredentials dinoCredentials = this.getCredentials();

        return dinoCredentials.getAuth();
    }

    @Override
    public User getCurrentUser() {
        final DinoUser dinoUser = this.getPrincipal();

        if (dinoUser == null) {
            return null;
        }

        return dinoUser.getUser();
    }

    @Override
    public DinoUser getPrincipal() {
        final SecurityContext context =  SecurityContextHolder.getContext();

        if (context == null) {
            return null;
        }

        final Authentication auth = context.getAuthentication();

        if (auth == null) {
            return null;
        }

        if (auth.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        return (DinoUser) auth.getPrincipal();
    }

    @Override
    public DinoCredentials getCredentials() {
        final SecurityContext context =  SecurityContextHolder.getContext();

        if (context == null) {
            return null;
        }

        final Authentication auth = context.getAuthentication();

        if (auth == null) {
            return null;
        }

        return (DinoCredentials) auth.getCredentials();
    }

    public Claims decodeAccessToken(String accessToken) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(this.ENCODED_KEY))
                .parseClaimsJws(accessToken).getBody();
    }

    @Override
    public List<String> getAllUserWebSocketTokenExceptCurrentByUser() {
        final Auth auth = this.getCurrentAuth();

        return authRepository.findAllWebSocketTokensExceptOneByUser(auth.getUser(), auth.getWebSocketToken());
    }

    @Override
    public List<String> getAllUserWebSocketTokenByUser(User user) {
        return authRepository.findAllWebSocketTokensByUser(user);
    }

    @Override
    public void setWebSocketConnected() {
        final Auth auth = this.getCurrentAuth();
        if (auth != null) {
            auth.setWebSocketConnected(true);
            authRepository.save(auth);
        }
    }

    public Boolean isValidToken(Auth auth) {
        final Claims claims = decodeAccessToken(auth.getAccessToken());
        final Long currentDate = new Date().getTime();

        return claims.getExpiration().getTime() >= currentDate;
    }

    private void generateWebSocketToken(Auth auth) {
        final Claims claims = Jwts.claims().setSubject(auth.getUser().getEmail());
        claims.put("roles", new ArrayList<>());
        final Date now = new Date();

        final String webSocketToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(now)
                .signWith(SignatureAlgorithm.HS256, this.WEB_SOCKET_ENCODED_KEY)
                .compact();

        auth.setWebSocketToken(webSocketToken);
    }

    private Date generateAccessToken(Auth auth, List<String> roles) {
        final Claims claims = Jwts.claims().setSubject(auth.getUser().getEmail());
        claims.put("roles", roles);
        final Date now = new Date();
        final Date expiresDate = new Date(now.getTime() + this.TOKEN_LIFE_TIME_IN_MS);

        final String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiresDate)
                .signWith(SignatureAlgorithm.HS256, this.ENCODED_KEY)
                .compact();

        auth.setAccessToken(accessToken);

        return expiresDate;
    }
}
