package hit.go.forward.entity.validate;

import java.util.Date;

/**
 * Created by 班耀强 on 2018/9/29
 */

public class ValidateCode {
    public static final int STATE_VALID = 0;
    public static final int STATE_INVALID = 1;

    private Integer id;
    private String email;
    private String code;
    private Date date;
    private Integer state;

    public ValidateCode() {}

    public ValidateCode(String email, String code) {
        this.email = email;
        this.code = code;
        this.date = new Date();
        this.state = STATE_VALID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
