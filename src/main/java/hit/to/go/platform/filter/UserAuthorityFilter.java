package hit.to.go.platform.filter;

import hit.to.go.database.dao.UserMapper;
import hit.to.go.database.mybatis.MybatisProxy;
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

        String url = request.getRequestURI();
        logger.debug("AuthorityFilter-请求资源 {}", url);

        UserWithPassword user = (UserWithPassword) session.getAttribute(AttrKey.ATTR_USER);
        if (user == null) {
            String id = request.getParameter("id");
            String password = request.getParameter("password");

            if (id == null || password == null) {
                logger.debug("未自动登录的用户请求 {}", url);
                user = new UserWithPassword(true);
                session.setAttribute(AttrKey.ATTR_USER, user);
            } else {
                UserMapper mapper = MybatisProxy.create(UserMapper.class);
                user = mapper.selectUserById(id);
                if (user != null && user.getPassword().equals(password)) {
                    logger.debug("自动登录的用户 {}", user.getId());
                    session.setAttribute(AttrKey.ATTR_USER, user);
                } else {
                    logger.debug("错误的用户cookie信息, 删除cookie {}:{}", id, password);
                    user = new UserWithPassword(true);
                    session.setAttribute(AttrKey.ATTR_USER, user);
                    response.addCookie(SystemVariable.newDeleteIdCookie());
                    response.addCookie(SystemVariable.newDeletePasswordCookie());
                }
            }
        }
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
    public void destroy() {}
}
