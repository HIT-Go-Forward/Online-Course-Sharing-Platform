package hit.to.go.platform.filter;

import hit.to.go.platform.protocol.RequestWrapper;
import hit.to.go.platform.util.MediaResolver;
import hit.to.go.platform.util.MediaTransmissionUtil;
import okhttp3.Response;
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

        response.setHeader("Access-Control-Allow-Origin", "*");
        if (!(url.equals("/") || url.endsWith(".media") || url.endsWith(".action"))) {
            response.sendError(403, "本服务只接受.media或.action请求");
            return;
        } else if (url.endsWith(".media")) {
            url = "/media" + url.replaceAll("\\.media", ".action");
            logger.debug("转发 {}", url);
            request.getRequestDispatcher(url).forward(request, response);
            return;
        } else if (url.endsWith(".action")) {
            Cookie[] cookies = request.getCookies();
            RequestWrapper requestWrapper = new RequestWrapper(request);
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("id")) requestWrapper.addParameter("id", cookie.getValue());
                    else if (cookie.getName().equals("password")) requestWrapper.addParameter("password", cookie.getValue());
                }
            }
            filterChain.doFilter(requestWrapper, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
