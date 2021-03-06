package hit.go.forward.service;

import hit.go.forward.common.entity.jwt.AuthorityVO;
import hit.go.forward.common.entity.user.User;

public interface UserAuthorityService {
    String generateToken(User user);

    String generateToken(User user, Integer duration);

    AuthorityVO verify(String token);
}
