package hit.go.forward.common.exception;


import hit.go.forward.common.protocol.RequestResults;

/**
 * Created by 班耀强 on 2018/10/23
 */
public class InvalidParametersException extends RequestHandleException {
    public InvalidParametersException() {
        super(RequestResults.wrongParameters());
    }
}
