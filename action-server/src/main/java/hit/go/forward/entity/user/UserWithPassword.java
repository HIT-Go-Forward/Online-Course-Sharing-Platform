package hit.go.forward.entity.user;

/**
 * Created by 班耀强 on 2018/10/1
 */
public class UserWithPassword extends User {
    private String password;

    public UserWithPassword() {
        super();
    }

    public UserWithPassword(boolean isVisitor) {
        super(isVisitor);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
