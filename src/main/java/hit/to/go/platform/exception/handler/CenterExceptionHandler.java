package hit.to.go.platform.exception.handler;

import hit.to.go.platform.exception.RequestHandleException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by 班耀强 on 2018/10/23
 */
public class CenterExceptionHandler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        OutputStream out = null;
        String result;
        ModelAndView tmp = new ModelAndView();
        if (e instanceof RequestHandleException) {
            RequestHandleException ex = (RequestHandleException) e;
            result = ex.result();
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
