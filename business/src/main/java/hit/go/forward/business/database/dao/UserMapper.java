package hit.go.forward.business.database.dao;

import hit.go.forward.common.entity.user.User;
import hit.go.forward.common.entity.user.UserWithPassword;

import java.util.Map;

/**
 * Created by 班耀强 on 2018/9/18
 */
public interface UserMapper {
    User queryById(String id);

    Integer register(Map<String, Object> map);

    Integer completeInfo(Map<String, String> map);

    Integer changePassword(Map<String, String> map);

    Integer selectIdByEmail(String email);

    String selectEmailById(String id);

    String selectPassword(String id);

    Map<String, String> loginById(Map<String, String> paras);

    Map<String, String> loginByEmail(Map<String, String> paras);

    UserWithPassword selectUser(Map<String, String> paras);

    UserWithPassword selectUserById(String id);

    UserWithPassword selectUserByEmail(String email);

    Integer updateUserImg(Map<String, Object> paras);
}
