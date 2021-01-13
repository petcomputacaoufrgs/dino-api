package br.ufrgs.inf.pet.dinoapi.service.google;

import br.ufrgs.inf.pet.dinoapi.entity.user.User;

public interface GoogleAccessTokenService {
    /**
     * Get a refreshed google access token for an user
     * @param user access token user
     * @return
     */
    String getAccessToken(User user);
}
