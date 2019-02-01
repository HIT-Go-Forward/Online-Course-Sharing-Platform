package hit.go.forward.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import hit.go.forward.common.entity.jwt.AuthorityVO;
import hit.go.forward.common.entity.user.User;
import hit.go.forward.service.UserAuthorityService;
import hit.go.forward.util.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by 班耀强 on 2018/12/1
 */
@Service
public class UserAuthorityServiceImpl implements UserAuthorityService {
    private static final Logger logger = LoggerFactory.getLogger(UserAuthorityServiceImpl.class);

    private static final String USER_ID =  "userId";
    private static final String USER_TYPE = "userType";

    @Override
    public String generateToken(User user) {
        Map<String, String> data = new HashMap<>();
        data.put(USER_ID, user.getId().toString());
        data.put(USER_TYPE, user.getType().toString());
        return JWTUtil.generateToken(data);
    }

    @Override
    public AuthorityVO verify(String token) {
        Map<String, String> data;
        try {
            data = JWTUtil.verify(token);
        } catch (JWTVerificationException e) {
            logger.debug("错误的token信息！");
            return null;
        }
        AuthorityVO result = new AuthorityVO();
        String userId = data.get(USER_ID);
        String userType =  data.get(USER_TYPE);
        if (userId == null || userType == null) {
            logger.debug("错误的token用户验证信息！userId={}, userType={}", userId, userType);
            return null;
        }
        result.setUserId(userId);
        result.setUserType(Integer.valueOf(userType));
        return result;
    }
}
