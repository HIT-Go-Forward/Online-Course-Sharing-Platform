package hit.go.forward.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import hit.go.forward.entity.AuthorityJWTData;

import java.util.Map;

public final class JWTUtil {
    private static final String JWT_ISSUER = "hit.go.forward";
    private static final String SECRET_KEY = "HIT-Go-Forward-2018";
    private static final Algorithm ALGORITHM = Algorithm.HMAC512(SECRET_KEY);
    private static final JWTVerifier VERIFIER = JWT.require(ALGORITHM).withIssuer(JWT_ISSUER).build();


    static {

    }

    public static String generateToken(AuthorityJWTData data) {
        JWTCreator.Builder builder = JWT.create()
                .withClaim(AuthorityJWTData.ACCOUNT, data.getAccount())
                .withClaim(AuthorityJWTData.CACHE_KEY, data.getCacheKey())
                .withIssuer(JWT_ISSUER);

        return builder.sign(ALGORITHM);
    }

    public static AuthorityJWTData verify(String token) throws JWTVerificationException {
        DecodedJWT decodedJWT = VERIFIER.verify(token);
        AuthorityJWTData data = new AuthorityJWTData();
        data.setAccount(decodedJWT.getClaim(AuthorityJWTData.ACCOUNT).asString());
        data.setCacheKey(decodedJWT.getClaim(AuthorityJWTData.CACHE_KEY).asString());
        return data;
    }
}
