package hit.to.go.platform.filter;

import hit.to.go.platform.protocol.RequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 班耀强 on 2018/9/20
 */
public class RequestFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


        String url = request.getRequestURI();
        logger.debug("请求资源 {}", url);

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, Access-Token");

        if (url.endsWith(".media")) {
            url = "/media" + url.replaceAll("\\.media", ".action");
            logger.debug("转发 {}", url);
            request.getRequestDispatcher(url).forward(request, response);
            return;
        } else if (url.endsWith(".action")) {
            Cookie[] cookies = request.getCookies();
            RequestWrapper requestWrapper = new RequestWrapper(request);
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    logger.debug("收到 Cookie {}={}", cookie.getName(), cookie.getValue());
                    if (cookie.getName().equals("id")) requestWrapper.addParameter("$cookieId", cookie.getValue());
                    else if (cookie.getName().equals("password")) requestWrapper.addParameter("$cookiePassword", cookie.getValue());
                }
            }
            filterChain.doFilter(requestWrapper, response);
            return;
        } else if (url.matches("^/video/course\\d+$")) {
            url = url.replaceFirst("(course\\d+)", "$1.action");
            request.getRequestDispatcher(url).forward(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
