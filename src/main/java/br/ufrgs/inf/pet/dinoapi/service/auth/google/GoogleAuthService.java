package br.ufrgs.inf.pet.dinoapi.service.auth.google;

import br.ufrgs.inf.pet.dinoapi.entity.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleAuthRequestModel;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface GoogleAuthService {
    /**
     * Dado um token de autenticação gera um token de acesso
     *
     * @param token - Token de autenticação do Google
     * @return token validado
     */
    ResponseEntity<?> googleSignIn(GoogleAuthRequestModel token, HttpServletRequest request);

    /**
     * Requisita a atualização do token de acesso Google
     *
     * @return novo token de acesso
     */
    ResponseEntity<?> googleRefreshAuth();

    /**
     * Atualiza o token de acesso expirado
     *
     * @param googleAuth - Dados de autenticação do Google para o usuário logado
     * @return objeto com o token atualizado
     */
    GoogleAuth refreshGoogleAuth(GoogleAuth googleAuth);

    /**
     * Retorna o token de acesso do usuário se ele possuir vinculo com o Google
     *
     * @return Token de acesso ou nulo caso não houver vinculo com o Google
     */
    GoogleAuth getUserGoogleAuth();
}
