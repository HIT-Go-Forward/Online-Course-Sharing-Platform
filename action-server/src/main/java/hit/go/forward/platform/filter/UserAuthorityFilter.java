package hit.go.forward.platform.filter;

import hit.go.forward.platform.SystemStorage;
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
    private static final String HAVE_NO_RIGHT = "您没有操作权限！";
    private static final Logger logger = LoggerFactory.getLogger(UserAuthorityFilter.class);

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

        if (!url.startsWith("/develop") && url.endsWith(".action")) {
            Integer power = SystemStorage.getActionPower(url);
            if (power == null) {
                response.sendError(404);
                return;
            } else if (power < 5) {
                String userId = request.getParameter("$userId");
                if (userId == null) {
                    response.sendError(403, HAVE_NO_RIGHT);
                    return;
                }
                Integer userType;
                try {
                    userType = Integer.valueOf(request.getParameter("$userType"));
                } catch (Exception e) {
                    response.sendError(403, HAVE_NO_RIGHT);
                    return;
                }
                if (userType > power) {
                    response.sendError(403, HAVE_NO_RIGHT);
                    return;
                }
                filterChain.doFilter(request, response);
                return;
//                String token = request.getParameter("token");
//                if (token == null) {
//
//                } else {
//                    try {
//                        AuthorityVO vo = authorityService.verify(token);
//                        if (vo != null) {
//                            if (vo.getUserType() > power) {
//                                response.sendError(403, HAVE_NO_RIGHT);
//                                return;
//                            }
//                            String userId = vo.getUserId();
//                            RequestWrapper requestWrapper;
//                            if (request instanceof RequestWrapper) requestWrapper = (RequestWrapper) request;
//                            else requestWrapper = new RequestWrapper(request);
//                            requestWrapper.addParameter("$userId", userId);
//                            requestWrapper.addParameter("$userType", vo.getUserType());
//                            filterChain.doFilter(requestWrapper, response);
//                            return;
//                        }
//                        else {
//                            logger.debug("错误的token信息！");
//                            response.sendError(403, "用户验证未通过！");
//                            return;
//                        }
//                    } catch (Exception e) {
//                        response.sendError(403, HAVE_NO_RIGHT);
//                        return;
//                    }
//                }
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
