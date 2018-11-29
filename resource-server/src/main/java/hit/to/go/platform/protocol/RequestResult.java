package hit.to.go.platform.protocol;

/**
 * Created by 班耀强 on 2018/10/18
 */
public class RequestResult {
    private Integer status;
    private Object data;

    public RequestResult(Integer status, Object data) {
        this.status = status;
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
