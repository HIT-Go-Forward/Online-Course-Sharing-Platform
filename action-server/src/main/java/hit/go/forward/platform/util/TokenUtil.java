package hit.go.forward.platform.util;

import com.auth0.jwt.algorithms.Algorithm;

import java.util.Map;

public final class TokenUtil {
    private static final String SECRET_KEY = "hit-6095";

    public static String generate(Map<String, Object> data) {
        Algorithm hs = Algorithm.HMAC512(SECRET_KEY);
        return "";
    }
}
