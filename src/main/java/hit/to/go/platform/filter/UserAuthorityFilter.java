package hit.to.go.platform.filter;

import hit.to.go.entity.user.UserWithPassword;
import hit.to.go.platform.AttrKey;
import hit.to.go.platform.SystemStorage;
import hit.to.go.platform.SystemVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
        HttpSession session = request.getSession();

        logger.debug("当前用户session id {}", session.getId());

        String url = request.getRequestURI();
        logger.debug("AuthorityFilter-请求资源 {}", url);

        UserWithPassword user = (UserWithPassword) session.getAttribute(AttrKey.ATTR_USER);
        if (user == null) {
            user = new UserWithPassword(true);
            session.setAttribute(AttrKey.ATTR_USER, user);
        }

        logger.debug("当前用户权限等级 {}", user.getType());

        if (!url.startsWith("/video/course") && url.endsWith(".action")) {
            if (!url.startsWith("/develop/")) {
                Integer p = SystemStorage.getActionPower(url);
                logger.debug("sessionId {} 当前用户权限等级 {} action权限等级 {}", session.getId(), user.getType(), p);
                if (p == null) {
                    response.sendError(404);
                    return;
                }
                if (user.getType() > p) {
                    request.getRequestDispatcher(SystemVariable.ERR_HAVE_NO_RIGHT_URL).forward(request, response);
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
