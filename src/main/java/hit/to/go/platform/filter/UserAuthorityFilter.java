package hit.to.go.platform.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 班耀强 on 2018/9/20
 */
public class UserAuthorityFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(UserAuthorityFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String url = request.getRequestURI();
        if (!url.endsWith(".action")) logger.debug("请求资源 {}", url);

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
