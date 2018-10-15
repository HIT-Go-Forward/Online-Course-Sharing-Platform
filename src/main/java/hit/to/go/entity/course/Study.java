package hit.to.go.entity.course;

import hit.to.go.entity.user.User;

import java.util.Date;

/**
 * Created by 班耀强 on 2018/10/15
 */
public class Study {
    private Integer id;
    private User user;
    private Course course;
    private Date date;
    private Integer state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
