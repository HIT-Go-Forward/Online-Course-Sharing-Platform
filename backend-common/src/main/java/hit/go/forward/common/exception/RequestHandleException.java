package hit.go.forward.common.exception;

/**
 * Created by 班耀强 on 2018/10/23
 */
public class RequestHandleException extends RuntimeException {
    private String result;

    public RequestHandleException(String result) {
        this.result = result;
    }

    public String result() {
        return result;
    }
}
