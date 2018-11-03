package hit.to.go.platform.exception;

import hit.to.go.platform.protocol.RequestResults;

/**
 * Created by 班耀强 on 2018/11/1
 */
public class DatabaseWriteException extends RequestHandleException {
    public DatabaseWriteException() {
        super(RequestResults.dataBaseWriteError());
    }
}
