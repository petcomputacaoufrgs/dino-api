package br.ufrgs.inf.pet.dinoapi.service.auth.dino;

import br.ufrgs.inf.pet.dinoapi.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * Implementa DinoAuthService
 *
 * @author joao.silva
 */
@Service
public class DinoAuthServiceImpl implements  DinoAuthService{

    private String key = "ie!>[1roh]f!7RmdPpzJ?sAQ(55+#E(RG@LXG*k[CPU4S^35ALLhÇF071[v>p[@t/SX]TD}504T)5|3:iAg2jE/I[yUKN5}N[_iyxç";

    private String secretKey = Base64.getEncoder().encodeToString(key.getBytes());

    private long validityInMilliseconds = 3600000;

    @Override
    public String refreshAccessToken(User user) {
        if (user != null) {
            generateAccessToken(user);

            return user.getAccessToken();
        }

        return null;
    }

    @Override
    public void generateAccessToken(User user) {
        if (user != null) {
            createToken(user, new ArrayList<>());
        }
    }

    private void createToken(User user, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("roles", roles);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        user.setTokenExpiresDateInMillis(validity.getTime());
        user.setAccessToken(accessToken);
    }
}
