package hit.go.forward.platform.filter;

import hit.go.forward.common.entity.jwt.AuthorityVO;
import hit.go.forward.common.entity.user.UserWithPassword;
import hit.go.forward.platform.AttrKey;
import hit.go.forward.platform.SystemStorage;
import hit.go.forward.platform.SystemVariable;
import hit.go.forward.service.UserAuthorityService;
import hit.go.forward.service.impl.UserAuthorityServiceImpl;
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
    private static final String HAVE_NO_RIGHT = "您没有操作权限！";
    private static final Logger logger = LoggerFactory.getLogger(UserAuthorityFilter.class);

    private UserAuthorityService authorityService = new UserAuthorityServiceImpl();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        HttpSession session = request.getSession();

        String url = request.getRequestURI();
        logger.debug("AuthorityFilter-请求资源 {}", url);

//        UserWithPassword user = (UserWithPassword) session.getAttribute(AttrKey.ATTR_USER);
//        if (user == null) {
//            String id = request.getParameter("$cookieId");
//            String password = request.getParameter("$cookiePassword");
//
//            if (id == null || password == null) {
//                logger.debug("未自动登录的用户请求 {}", url);
//                user = new UserWithPassword(true);
//                session.setAttribute(AttrKey.ATTR_USER, user);
//            } else {
//                request.getRequestDispatcher("/authority/autoLogin.action").forward(request, response);
//                return;
//            }
//        }
//        // TODO
//        if (!url.startsWith("/video/course") && url.endsWith(".action")) {
//            if (!url.startsWith("/develop/")) {
//                Integer p = SystemStorage.getActionPower(url);
//                logger.debug("sessionId {} 当前用户权限等级 {} action权限等级 {}", session.getId(), user.getType(), p);
//                if (p == null) {
//                    response.sendError(404);
//                    return;
//                }
//                if (user.getType() > p) {
//                    request.getRequestDispatcher(SystemVariable.ERR_HAVE_NO_RIGHT_URL).forward(request, response);
//                    return;
//                }
//            }
//        }
        Integer power = SystemStorage.getActionPower(url);
        if (power == null) {
            response.sendError(404);
            return;
        } else if (power < 5) {
            String token = request.getParameter("token");
            if (token == null) {
                response.sendError(403, HAVE_NO_RIGHT);
                return;
            } else {
                try {
                    AuthorityVO vo = authorityService.verify(token);
                    if (vo != null) {
                        if (vo.getUserType() > power) {
                            response.sendError(403, HAVE_NO_RIGHT);
                            return;
                        }
                    }
                    else {
                        logger.debug("错误的token信息！");
                        response.sendError(403, "用户验证未通过！");
                        return;
                    }
                } catch (Exception e) {
                    response.sendError(403, HAVE_NO_RIGHT);
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
