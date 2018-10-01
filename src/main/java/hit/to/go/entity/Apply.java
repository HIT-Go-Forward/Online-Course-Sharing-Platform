package hit.to.go.entity;

import java.util.Date;

/**
 * Created by 班耀强 on 2018/10/1
 */
public class Apply {
    public static int STATE_APPLYING = 1;
    public static int STATE_RESOLVED = 2;
    public static int STATE_REJECTED = 3;

    private Integer id;
    private Integer userId;
    private Date time;
    private String note;
    private Integer handlerId;
    private Integer state;
    private Date handleTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Integer getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(Integer handlerId) {
        this.handlerId = handlerId;
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
}
