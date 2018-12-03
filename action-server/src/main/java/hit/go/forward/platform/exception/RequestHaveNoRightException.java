package hit.go.forward.platform.exception;


import hit.go.forward.common.protocol.RequestResults;

/**
 * Created by 班耀强 on 2018/10/23
 */
public class RequestHaveNoRightException extends RequestHandleException {
    public RequestHaveNoRightException() {
        super(RequestResults.haveNoRight());
    }
}
