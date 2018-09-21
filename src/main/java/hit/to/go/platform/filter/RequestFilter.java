package hit.to.go.platform.filter;

import hit.to.go.platform.util.MediaResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 班耀强 on 2018/9/20
 */
public class RequestFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String url = request.getRequestURI();
        logger.debug("请求资源 {}", url);

        if (!(url.equals("/") || url.endsWith(".media") || url.endsWith(".action"))) {
            response.sendError(403, "服务器只接受.media或.action请求");
            return;
        } else if (url.endsWith(".media")) {
            url = "/media" + url.replaceAll("\\.media", ".action");
            logger.debug("转发 {}", url);
            request.getRequestDispatcher(url).forward(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
