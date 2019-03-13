package hit.go.forward.common.util;

import hit.go.forward.common.entity.user.User;
import hit.go.forward.common.entity.user.UserWithToken;

/**
 * Created by 班耀强 on 2018/12/5
 */
public final class UserUtil {
    public static UserWithToken toTokenUser(User user) {
        UserWithToken userWithToken = new UserWithToken();
        userWithToken.setId(user.getId());
        userWithToken.setName(user.getName());
        userWithToken.setType(user.getType());
        userWithToken.setSex(user.getSex());
        userWithToken.setBirthday(user.getBirthday());
        userWithToken.setEmail(user.getEmail());
        userWithToken.setEducation(user.getEducation());
        userWithToken.setSchool(user.getSchool());
        userWithToken.setIntro(user.getIntro());
        userWithToken.setNote(user.getNote());
        userWithToken.setPhone(user.getPhone());
        userWithToken.setImg(user.getImg());
        userWithToken.setTokenVersion(user.getTokenVersion());

        return userWithToken;
    }
}
