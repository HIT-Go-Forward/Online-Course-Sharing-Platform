package hit.to.go.database.dao;

import hit.to.go.entity.user.User;

import java.util.Map;

/**
 * Created by 班耀强 on 2018/9/18
 */
public interface UserMapper {
    User queryById(int id);

    Integer signUp(Map<String, String> map);

    User login(Map<String, String> map);
}
