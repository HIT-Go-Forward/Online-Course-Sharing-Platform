package hit.to.go.database.dao;

import hit.to.go.entity.user.User;

import java.util.Map;

/**
 * Created by 班耀强 on 2018/9/18
 */
public interface UserMapper {
    User queryById(int id);

    Integer register(Map<String, String> map);

    Integer completeInfo(Map<String, String> map);

    Integer changePassword(Map<String, String> map);

    Integer selectIdByEmail(String email);

    String selectEmailById(Integer id);

    String selectPassword(Integer id);

    Map<String, String> loginById(Map<String, String> paras);

    Map<String, String> loginByEmail(Map<String, String> paras);
}
