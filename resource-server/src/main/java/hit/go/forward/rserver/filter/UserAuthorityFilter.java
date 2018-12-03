package hit.go.forward.rserver.filter;

import hit.go.forward.service.UserAuthorityService;
import hit.go.forward.service.impl.RedisServiceImpl;
import hit.go.forward.service.impl.UserAuthorityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 班耀强 on 2018/10/18
 */
public class UserAuthorityFilter implements Filter {
    private static final String HAVE_NO_RIGHT = "您没有操作权限！";
    private static final Logger logger = LoggerFactory.getLogger(UserAuthorityFilter.class);

    private UserAuthorityService service = new UserAuthorityServiceImpl(new RedisServiceImpl());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String token = request.getParameter("token");
        if (token == null) {
            response.sendError(403, HAVE_NO_RIGHT);
            return;
        } else {
            try {
                Integer level;
                String levelStr = service.verify(token);
                if (levelStr != null) {
                    level = Integer.valueOf(levelStr);
                    if (level > 4) {
                        response.sendError(403, HAVE_NO_RIGHT);
                        return;
                    }
                }
                else {
                    logger.debug("未能从Redis获取到用户信息");
                    response.sendError(404, "未查询到用户信息");
                    return;
                }
            } catch (Exception e) {
                response.sendError(403, HAVE_NO_RIGHT);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
