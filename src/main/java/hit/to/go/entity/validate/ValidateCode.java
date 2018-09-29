package hit.to.go.entity.validate;

import java.util.Date;

/**
 * Created by 班耀强 on 2018/9/29
 */
public class ValidateCode {
    public static final int STATE_VALID = 0;
    public static final int STATE_INVALIDA = 1;

    private Integer id;
    private String email;
    private String code;
    private Date date;
    private Integer state;

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