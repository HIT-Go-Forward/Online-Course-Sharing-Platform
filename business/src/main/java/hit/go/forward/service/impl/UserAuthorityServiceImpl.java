package hit.go.forward.service.impl;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import hit.go.forward.common.entity.user.User;
import hit.go.forward.service.RedisService;
import hit.go.forward.service.UserAuthorityService;
import hit.go.forward.util.JWTUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by 班耀强 on 2018/12/1
 */
@Service
public class UserAuthorityServiceImpl implements UserAuthorityService {
    private RedisService redisService;

    public UserAuthorityServiceImpl(RedisService redisService) {
        this.redisService = redisService;
    }

    private static final String ACCOUNT = "account";
    private static final String CACHE_KEY = "cacheKey";

    @Override
    public String generateToken(User user) {
        Map<String, String> paras = new HashMap<>();
        paras.put(ACCOUNT, user.getId().toString());
        redisService.set(user.getId().toString(), user.getType().toString());
        return JWTUtil.generateToken(paras);
    }

    @Override
    public String verify(String token) {
        Map<String, String> data = null;
        try {
            data = JWTUtil.verify(token);
        } catch (JWTVerificationException e) {
            return null;
        }
        return redisService.get(data.get(ACCOUNT));
    }
}
