package hit.to.go.platform.exception;

import hit.to.go.platform.protocol.RequestResult;
import hit.to.go.platform.protocol.RequestResults;

/**
 * Created by 班耀强 on 2018/10/23
 */
public class RequestHaveNoRightException extends RequestHandleException {
    public RequestHaveNoRightException() {
        super(RequestResults.haveNoRight());
    }
}
