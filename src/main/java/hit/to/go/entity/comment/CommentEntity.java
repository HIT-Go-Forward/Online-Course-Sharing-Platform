package hit.to.go.entity.comment;

import hit.to.go.entity.user.User;

/**
 * Created by 班耀强 on 2018/11/1
 */
public class CommentEntity extends Comment {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
