package hit.go.forward.common.exception;

import hit.go.forward.common.protocol.RequestResult;
import hit.go.forward.common.protocol.RequestResults;

/**
 * Created by 班耀强 on 2018/10/23
 */
public class RequestHandleException extends RuntimeException {
    private String result;

    public RequestHandleException(RequestResult result) {
        this.result = RequestResults.toJSON(result);
    }

    public String result() {
        return result;
    }
}
