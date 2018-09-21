package hit.to.go.database.dao;

import hit.to.go.entity.User;

/**
 * Created by 班耀强 on 2018/9/18
 */
public interface UserMapper {
    User queryById(int id);
}
