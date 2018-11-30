package hit.go.forward.common.entity;

import hit.go.forward.common.entity.user.User;

import java.util.Date;

/**
 * Created by 班耀强 on 2018/10/1
 */
public class Apply {
    public static int STATE_APPLYING = 1;
    public static int STATE_RESOLVED = 2;
    public static int STATE_REJECTED = 3;

    private Integer id;
//    private Integer userId;
    private User applyUser;
    private Date time;
    private String note;
//    private Integer handlerId;
    private User handler;
    private Integer state;
    private Date handleTime;
    private String handleNote;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUserId() {
        return applyUser;
    }

    public void setUserId(User userId) {
        this.applyUser = userId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public User getHandler() {
        return handler;
    }

    public void setHandler(User handler) {
        this.handler = handler;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public String getHandleNote() {
        return handleNote;
    }

    public void setHandleNote(String handleNote) {
        this.handleNote = handleNote;
    }
}
