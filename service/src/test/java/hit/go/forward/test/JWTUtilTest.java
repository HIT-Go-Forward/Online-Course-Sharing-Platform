package hit.go.forward.test;

import hit.go.forward.entity.AuthorityJWTData;
import hit.go.forward.util.JWTUtil;
import org.junit.Test;

public class JWTUtilTest {

    @Test
    public void testJWT() {
        AuthorityJWTData data = new AuthorityJWTData();
        data.setAccount("banyq");
        data.setCacheKey("cacheKey test");

        String token = JWTUtil.generateToken(data);
        AuthorityJWTData vData = JWTUtil.verify(token);
        System.out.println();
    }
}
