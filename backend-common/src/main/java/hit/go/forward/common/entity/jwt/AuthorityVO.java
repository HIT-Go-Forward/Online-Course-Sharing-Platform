package hit.go.forward.common.entity.jwt;

/**
 * Created by 班耀强 on 2018/12/5
 */
public class AuthorityVO {
    private String userId;
    private Integer userType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}
