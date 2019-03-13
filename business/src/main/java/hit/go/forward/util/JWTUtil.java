package hit.go.forward.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class JWTUtil {
    private static final String JWT_ISSUER = "hit.go.forward";
    private static final String SECRET_KEY = "HIT-Go-Forward-2018";
    private static final Algorithm ALGORITHM = Algorithm.HMAC512(SECRET_KEY);
    private static final JWTVerifier VERIFIER = JWT.require(ALGORITHM).withIssuer(JWT_ISSUER).build();

    private static final Integer TIME_MS_7_DAYS = 604800000;

    static {

    }

    public static String generateToken(Map<String, String> paras) {
        return generateToken(paras, TIME_MS_7_DAYS);
    }

    public static String generateToken(Map<String, String> paras, Integer duration) {
        JWTCreator.Builder builder = JWT.create()
                .withIssuer(JWT_ISSUER);
        if (duration != null) builder.withExpiresAt(new Date(new Date().getTime() + duration));
        for (String key : paras.keySet()) {
            builder = builder.withClaim(key, paras.get(key));
        }
        return builder.sign(ALGORITHM);
    }

    public static Map<String, String> verify(String token) throws JWTVerificationException {
        DecodedJWT decodedJWT = VERIFIER.verify(token);
        Map<String, String> data = new HashMap<>();
        Map<String, Claim> claims = decodedJWT.getClaims();
        for (String key : claims.keySet()) {
            data.put(key, claims.get(key).asString());
        }
        return data;
    }
}
