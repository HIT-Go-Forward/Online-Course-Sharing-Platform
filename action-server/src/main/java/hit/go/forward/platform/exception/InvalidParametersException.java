package hit.go.forward.platform.exception;

import hit.to.go.platform.protocol.RequestResults;

/**
 * Created by 班耀强 on 2018/10/23
 */
public class InvalidParametersException extends RequestHandleException {
    public InvalidParametersException() {
        super(RequestResults.wrongParameters());
    }
}
