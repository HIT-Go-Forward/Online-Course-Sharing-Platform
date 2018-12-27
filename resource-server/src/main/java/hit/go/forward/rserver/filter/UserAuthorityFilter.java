package hit.go.forward.rserver.filter;

import hit.go.forward.common.entity.jwt.AuthorityVO;
import hit.go.forward.common.protocol.RequestWrapper;
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

    private UserAuthorityService service = new UserAuthorityServiceImpl();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String url = request.getRequestURI();
        if (url.endsWith(".action")) {
            String userId = request.getParameter("$userId");
            if (userId == null) {
                response.sendError(403);
                return;
            }
            Integer userType;
            try {
                userType = Integer.valueOf(request.getParameter("$userType"));
            } catch (Exception e) {
                response.sendError(403);
                return;
            }
            if (userType >= 5) {
                response.sendError(403);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
