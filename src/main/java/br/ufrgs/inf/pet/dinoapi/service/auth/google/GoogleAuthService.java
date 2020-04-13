package br.ufrgs.inf.pet.dinoapi.service.auth.google;

import br.ufrgs.inf.pet.dinoapi.entity.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.model.auth.GoogleAuthRequestModel;
import org.springframework.http.ResponseEntity;

/**
 * Service de autenticação com o Google
 *
 * @author joao.silva
 */
public interface GoogleAuthService {
    /**
     * Dado um token de autenticação gera um token de acesso
     *
     * @param token - Token de autenticação do Google
     * @return token validado
     */
    ResponseEntity<?> requestGoogleSign(GoogleAuthRequestModel token);

    /**
     * Atualiza o token de acesso expirado
     *
     * @param googleAuth - Dados de autenticação do Google para o usuário logado
     * @return token atualizado
     */
    String refreshGoogleAuth(GoogleAuth googleAuth);

    /**
     * Retorna o token de acesso do usuário se ele possuir vinculo com o Google
     *
     * @return Token de acesso ou nulo caso não houver vinculo com o Google
     */
    GoogleAuth getUserGoogleAuth();
}
