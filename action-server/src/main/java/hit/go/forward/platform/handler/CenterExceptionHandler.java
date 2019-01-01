package hit.go.forward.platform.handler;

import hit.go.forward.common.exception.RequestHandleException;
import hit.go.forward.common.protocol.RequestResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Created by 班耀强 on 2018/10/23
 */
public class CenterExceptionHandler implements HandlerExceptionResolver {
    private static final Logger logger = LoggerFactory.getLogger(CenterExceptionHandler.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        OutputStream out = null;
        String result;
        ModelAndView tmp = new ModelAndView();

        logger.error("捕获异常{}:{}", e.getClass().getName(), e.getMessage());

        if (e instanceof RequestHandleException) {
            RequestHandleException ex = (RequestHandleException) e;
            result = ex.result();
        } else if (e instanceof org.springframework.dao.DuplicateKeyException) {
            result = RequestResults.operationFailed("操作失败！该操作可能已被执行，请勿重复操作.");
        } else result = e.getMessage();

        try {
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.setStatus(200);
            out = httpServletResponse.getOutputStream();
            out.write(result.getBytes(StandardCharsets.UTF_8));
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (out != null) out.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        tmp.clear();
        return tmp;
    }
}
