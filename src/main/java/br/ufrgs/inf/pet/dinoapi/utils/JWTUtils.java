package br.ufrgs.inf.pet.dinoapi.utils;

import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;

public class JWTUtils {
    private static final long ALLOWED_CLOCK_SKEW_SECONDS = 300;

    public static String generateUnlimited(String id, String subject, String key) {
        final Claims claims = Jwts.claims().setSubject(subject);
        final ClockServiceImpl clock = new ClockServiceImpl();

        return Jwts.builder()
                .setIssuedAt(clock.now())
                .setClaims(claims)
                .setId(id)
                .setSubject(subject)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public static String generate(Claims claims, String id, String subject, Long duration, String key) {
        final ClockServiceImpl clock = new ClockServiceImpl();
        final Date expiresDate = clock.nowPlusMinutes(duration);

        return Jwts.builder()
                .setIssuedAt(clock.now())
                .setClaims(claims)
                .setId(id)
                .setSubject(subject)
                .setExpiration(expiresDate)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public static Claims decode(String jwt, String key) {
        final ClockServiceImpl clock = new ClockServiceImpl();

        return Jwts.parser()
                .setClock(clock)
                .setAllowedClockSkewSeconds(ALLOWED_CLOCK_SKEW_SECONDS)
                .setSigningKey(DatatypeConverter.parseBase64Binary(key))
                .parseClaimsJws(jwt).getBody();
    }
}
