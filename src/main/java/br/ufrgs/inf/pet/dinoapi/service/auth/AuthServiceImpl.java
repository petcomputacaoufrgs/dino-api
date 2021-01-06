package br.ufrgs.inf.pet.dinoapi.service.auth;

import br.ufrgs.inf.pet.dinoapi.model.auth.web_socket.WebSocketAuthDataModel;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.constants.AuthConstants;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.model.auth.AuthRefreshRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.AuthRefreshResponseDataModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.AuthRefreshResponseModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.web_socket.WebSocketAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.projection.auth.AuthWebSocketToken;
import br.ufrgs.inf.pet.dinoapi.repository.auth.AuthRepository;
import br.ufrgs.inf.pet.dinoapi.security.DinoCredentials;
import br.ufrgs.inf.pet.dinoapi.security.DinoUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import javax.xml.bind.DatatypeConverter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String ACCESS_TOKEN_KEY = "ie!>[é1roh]f!7RmdPâpzõJ?sAQ(55+ç#E(RG@LXG*k[CPU4S^35ALLhÇF071[v>pó[@t/ãSX]ÊTD}504T)ç5|3:iAg2jE/I[yUKN5}N[_iyxç";

    private static final String ACCESS_TOKEN_ENCODED_KEY = Base64.getEncoder().encodeToString(ACCESS_TOKEN_KEY.getBytes());

    private static final String WEB_SOCKET_TOKEN_KEY = "m.|TGrãhéhXkp+(=Q-{6F{m2KFShSD[[D]WQEL.P[WAS]Dõ@$JHW=qLukasdsdas22433ç4432$@#@#hi/&l{Udnk@!@!@F%4&<0;X3l1gsSd$";

    private static final String WEB_SOCKET_TOKEN_ENCODED_KEY = Base64.getEncoder().encodeToString(WEB_SOCKET_TOKEN_KEY.getBytes());

    private static final String REFRESH_TOKEN_KEY = "J~z}[[Ri=:çm1qg,X9.zéQ+G@D3iãED}~[3=dTâ`_PQe>=BXS)xw4aP1P<m@1v)$B9;siE=7vpsZ)CZG6YX+-1f+.YYruL|}<+%i>hlSh>lRte";

    private static final String REFRESH_TOKEN_ENCODED_KEY = Base64.getEncoder().encodeToString(REFRESH_TOKEN_KEY.getBytes());

    private static final long ALLOWED_CLOCK_SKEW_SECONDS = 300;

    private static final long ACCESS_TOKEN_LIFE_TIME_IN_MIN = 60;

    private static final long WEB_SOCKET_TOKEN_LIFE_TIME_IN_MIN = 5;

    private final AuthRepository authRepository;

    private final ClockServiceImpl clockService;

    @Autowired
    public AuthServiceImpl(AuthRepository authRepository, ClockServiceImpl clockService) {
        this.authRepository = authRepository;
        this.clockService = clockService;
    }

    @Override
    public Auth generateAuth(User user) {
        final Auth auth = new Auth();
        auth.setUser(user);
        auth.setWebSocketConnected(false);

        final Auth savedAuth = authRepository.save(auth);

        List<String> roles = new ArrayList<>();

        this.generateAccessToken(savedAuth, roles);
        this.generateRefreshToken(savedAuth);

        return authRepository.save(savedAuth);
    }

    @Override
    public ResponseEntity<WebSocketAuthResponseModel> webSocketAuthRequest() {
        final WebSocketAuthResponseModel response = new WebSocketAuthResponseModel();

        try {
            final Auth auth = this.getCurrentAuth();
            auth.setWebSocketConnected(false);
            this.generateWebSocketToken(auth);

            authRepository.save(auth);

            final WebSocketAuthDataModel model = new WebSocketAuthDataModel();
            model.setWebSocketToken(auth.getWebSocketToken());
            response.setData(model);
            response.setSuccess(true);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setError(AuthConstants.UNKNOWN_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<AuthRefreshResponseModel> refreshAuth(AuthRefreshRequestModel authRefreshRequestModel) {
        final AuthRefreshResponseModel response = new AuthRefreshResponseModel();

        try {
            final Optional<Auth> authSearch = authRepository.findByRefreshToken(authRefreshRequestModel.getRefreshToken());

            if (authSearch.isPresent()) {
                Auth auth = authSearch.get();

                final LocalDateTime tokenExpiresDate = this.generateAccessToken(auth, new ArrayList<>());

                auth = authRepository.save(auth);

                final AuthRefreshResponseDataModel responseData = new AuthRefreshResponseDataModel();
                responseData.setAccessToken(auth.getAccessToken());
                responseData.setExpiresDate(clockService.toUTCZonedDateTime(tokenExpiresDate));

                response.setSuccess(true);
                response.setData(responseData);

                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            response.setSuccess(false);
            response.setError(AuthConstants.INVALID_AUTH);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setError(AuthConstants.UNKNOWN_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
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
    public boolean canConnectToWebSocket(Auth auth) {
        return !auth.getWebSocketConnected();
    }

    @Override
    public Auth getCurrentAuth() {
        final DinoCredentials dinoCredentials = this.getCredentials();

        if (dinoCredentials != null) {
            return dinoCredentials.getAuth();
        }

        return null;
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

        if (auth instanceof AnonymousAuthenticationToken) {
            return null;
        }

        return (DinoCredentials) auth.getCredentials();
    }

    public Claims decodeAccessToken(String token) {
        return this.decodeJWT(token, ACCESS_TOKEN_ENCODED_KEY);
    }

    public Claims decodeRefreshToken(String token) {
        return this.decodeJWT(token, REFRESH_TOKEN_ENCODED_KEY);
    }

    public Claims decodeWebSocketToken(String token) {
        return this.decodeJWT(token, WEB_SOCKET_TOKEN_ENCODED_KEY);
    }

    @Override
    public List<String> getAllUserWebSocketTokenExceptByAuth(Auth auth) {
        if (auth != null) {
            List<AuthWebSocketToken> results;
            if (auth.getWebSocketToken() != null) {
                results = authRepository.findAllByUserExceptWithThisWebSocketToken(auth.getUser(), auth.getWebSocketToken());
            } else {
                results = authRepository.findAllByUser(auth.getUser());
            }
            return results.stream().map(AuthWebSocketToken::getWebSocketToken).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    @Override
    public void setWebSocketConnected() {
        final Auth auth = this.getCurrentAuth();
        if (auth != null) {
            auth.setWebSocketConnected(true);
            authRepository.save(auth);
        }
    }

    public boolean isValidAccessToken(String token) {
        final Claims claims = this.decodeAccessToken(token);
        final ClockServiceImpl clock = new ClockServiceImpl();

        return claims.getExpiration().getTime() >= clock.now().getTime();
    }

    public boolean isValidRefreshToken(String token) {
        final Claims claims = this.decodeRefreshToken(token);
        final ClockServiceImpl clock = new ClockServiceImpl();

        return claims.getExpiration().getTime() >= clock.now().getTime();
    }

    public boolean isValidWebSocketToken(String token) {
        final Claims claims = this.decodeWebSocketToken(token);
        final ClockServiceImpl clock = new ClockServiceImpl();

        return claims.getExpiration().getTime() >= clock.now().getTime();
    }

    private void generateWebSocketToken(Auth auth) {
        final ClockServiceImpl clock = new ClockServiceImpl();
        final Claims claims = Jwts.claims().setSubject(auth.getUser().getEmail());
        claims.put("roles", new ArrayList<>());

        final String webSocketToken = Jwts.builder()
                .setClaims(claims)
                .setId(auth.getId().toString())
                .setSubject(auth.getUser().getEmail())
                .setIssuedAt(clock.now())
                .setExpiration(clock.nowPlusMinutes(WEB_SOCKET_TOKEN_LIFE_TIME_IN_MIN))
                .signWith(SignatureAlgorithm.HS256, WEB_SOCKET_TOKEN_ENCODED_KEY)
                .compact();

        auth.setWebSocketToken(webSocketToken);
    }

    private LocalDateTime generateAccessToken(Auth auth, List<String> roles) {
        final Claims claims = Jwts.claims().setSubject(auth.getUser().getEmail());
        claims.put("roles", roles);
        final ClockServiceImpl clock = new ClockServiceImpl();
        final Date expiresDate = clock.nowPlusMinutes(ACCESS_TOKEN_LIFE_TIME_IN_MIN);
        final String accessToken = Jwts.builder()
                .setIssuedAt(clock.now())
                .setClaims(claims)
                .setId(auth.getId().toString())
                .setSubject(auth.getUser().getEmail())
                .setExpiration(expiresDate)
                .signWith(SignatureAlgorithm.HS256, ACCESS_TOKEN_ENCODED_KEY)
                .compact();

        auth.setAccessToken(accessToken);
        auth.setLastTokenRefresh(LocalDateTime.now());

        return clock.toLocalDateTime(expiresDate);
    }

    private void generateRefreshToken(Auth auth) {
        final ClockServiceImpl clock = new ClockServiceImpl();

        final String refreshToken = Jwts.builder()
                .setIssuedAt(clock.now())
                .setId(auth.getId().toString())
                .setSubject(auth.getUser().getEmail())
                .signWith(SignatureAlgorithm.HS256, REFRESH_TOKEN_ENCODED_KEY)
                .compact();

        auth.setRefreshToken(refreshToken);
    }

    private Claims decodeJWT(String jwt, String key) {
        Clock clock = new ClockServiceImpl();
        return Jwts.parser().setClock(clock)
                .setAllowedClockSkewSeconds(ALLOWED_CLOCK_SKEW_SECONDS)
                .setSigningKey(DatatypeConverter.parseBase64Binary(key))
                .parseClaimsJws(jwt).getBody();
    }
}
