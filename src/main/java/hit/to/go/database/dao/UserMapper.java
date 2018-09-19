package hit.to.go.database.dao;

import hit.to.go.entity.User;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/9/18
 */
public interface UserMapper {
    User selectUser(int id);

    User selectUserByName(String name);

    User loginById(Map<String, Object> param);

    User loginByName(Map<String, Object> param);

    ArrayList<User> selectAll();

    String selectPassword(int id);

    Integer insertUser(Map<String, Object> param);
}
