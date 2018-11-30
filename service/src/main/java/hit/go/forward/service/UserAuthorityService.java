package hit.go.forward.service;

import hit.go.forward.common.entity.user.User;

public interface UserAuthorityService {
    String generateToken(User user);


}
