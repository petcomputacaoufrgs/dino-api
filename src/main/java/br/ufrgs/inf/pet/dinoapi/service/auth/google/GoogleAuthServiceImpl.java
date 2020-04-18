package br.ufrgs.inf.pet.dinoapi.service.auth.google;

import br.ufrgs.inf.pet.dinoapi.communication.google.GoogleAPICommunicationImpl;
import br.ufrgs.inf.pet.dinoapi.entity.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.entity.User;
import br.ufrgs.inf.pet.dinoapi.model.auth.GoogleAuthRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.auth.GoogleAuthResponseModel;
import br.ufrgs.inf.pet.dinoapi.repository.GoogleAuthRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.dino.DinoAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

/**
 * Implementação de: {@link GoogleAuthService}
 *
 * @author joao.silva
 */
@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    DinoAuthServiceImpl dinoAuthService;

    @Autowired
    GoogleAuthRepository googleAuthRepository;

    final GoogleAPICommunicationImpl googleAPICommunicationImpl = new GoogleAPICommunicationImpl();

    @Override
    public ResponseEntity<?> requestGoogleSign(GoogleAuthRequestModel authModel) {
        GoogleTokenResponse tokenResponse = googleAPICommunicationImpl.getGoogleToken(authModel.getToken());

        if (tokenResponse != null) {
            try {
                GoogleIdToken idToken = tokenResponse.parseIdToken();

                GoogleIdToken.Payload payload = idToken.getPayload();

                String googleId = payload.getSubject();

                Optional<GoogleAuth> googleAuthSearchResult = googleAuthRepository.findByGoogleId(googleId);

                GoogleAuth googleAuth;
                User userDB;

                String pictureUrl = (String) payload.get("picture");
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                if (googleAuthSearchResult.isPresent()) {
                    googleAuth = googleAuthSearchResult.get();

                    if (googleAuth.getRefreshToken().isEmpty()) {
                        String refreshToken = tokenResponse.getRefreshToken();

                        if (refreshToken.isEmpty()) {
                            return new ResponseEntity<>("Refresh token perdido. Por favor, requira um novo.", HttpStatus.PRECONDITION_REQUIRED);
                        } else {
                            googleAuth.setRefreshToken(refreshToken);
                        }
                    }

                    userDB = googleAuth.getUser();

                    updateUserData(payload, userDB);

                    updateAccessTokenInfo(tokenResponse, googleAuth);

                    googleAuthRepository.save(googleAuth);
                } else {
                    userDB = new User(name, email);

                    String refreshToken = tokenResponse.getRefreshToken();

                    if (refreshToken.isEmpty()) {
                        return new ResponseEntity<>("Refresh token perdido. Por favor, requira um novo.", HttpStatus.PRECONDITION_REQUIRED);
                    }

                    googleAuth = new GoogleAuth(googleId, refreshToken);

                    updateAccessTokenInfo(tokenResponse, googleAuth);

                    userDB.setGoogleAuth(googleAuth);
                }

                dinoAuthService.generateAccessToken(userDB);

                userService.save(userDB);

                GoogleAuthResponseModel response = new GoogleAuthResponseModel();

                response.setAccessToken(userDB.getAccessToken());

                response.setGoogleAccessToken(googleAuth.getAccessToken());

                response.setEmail(email);

                response.setName(name);

                response.setPictureUrl(pictureUrl);

                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (IOException e) {
                return new ResponseEntity<>("Erro ao resgatar dados da autenticação com o Google.", HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>("Erro na autenticação com a API do Google.", HttpStatus.BAD_REQUEST);
    }

    @Override
    public String refreshGoogleAuth(GoogleAuth googleAuth) {
        if (googleAuth != null) {
            GoogleTokenResponse tokenResponse = googleAPICommunicationImpl.refreshAccessToken(googleAuth.getRefreshToken());

            if (tokenResponse != null) {
                updateAccessTokenInfo(tokenResponse, googleAuth);

                googleAuthRepository.save(googleAuth);

                return googleAuth.getAccessToken();
            }
        }
        return null;
    }

    @Override
    public GoogleAuth getUserGoogleAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();

            if (userDetails != null) {
                User userDB = userService.findUserByEmail(userDetails.getUsername());
                return userDB.getGoogleAuth();
            }
        }

        return null;
    }


    private Long getTokenExpirationDateInMS(Long expiresIn) {
        //Soma a data atual com o tempo do token expirar (converte ele de segundos para milisegundos antes)
        return (new Date()).getTime() + (expiresIn * 1000);
    }

    private void updateUserData(GoogleIdToken.Payload payload, User user) {
        String email = payload.getEmail();
        String name = (String) payload.get("name");

        if (!user.getEmail().equals(email)) {
            user.setEmail(email);
        }
        if (!user.getName().equals(name)) {
            user.setName(name);
        }
    }

    private void updateAccessTokenInfo(GoogleTokenResponse tokenResponse, GoogleAuth googleAuth) {
        String accessToken = tokenResponse.getAccessToken();

        Long expiresIn = tokenResponse.getExpiresInSeconds();

        Long tokenExpiresDateInMillis = getTokenExpirationDateInMS(expiresIn);

        googleAuth.setAccessToken(accessToken);

        googleAuth.setTokenExpiresDateInMillis(tokenExpiresDateInMillis);
    }
}
