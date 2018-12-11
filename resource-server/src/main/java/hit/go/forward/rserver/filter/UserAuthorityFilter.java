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

        String token = request.getParameter("token");
        if (token == null) {
            response.sendError(403, HAVE_NO_RIGHT + ":token=null");
            return;
        } else {
            try {
                AuthorityVO vo = service.verify(token);
                if (vo != null) {
                    if (vo.getUserType() > 4) {
                        response.sendError(403, HAVE_NO_RIGHT + ":vo=null");
                        return;
                    }
                    String userId = vo.getUserId();
                    RequestWrapper requestWrapper = new RequestWrapper(request);
                    requestWrapper.addParameter("$userId", userId);
                    filterChain.doFilter(requestWrapper, response);
                    return;
                }
                else {
                    logger.debug("错误的token信息！");
                    response.sendError(403, "用户验证未通过！您可能无权操作！");
                    return;
                }
            } catch (Exception e) {
                response.sendError(403, HAVE_NO_RIGHT + ":exception=" + e.getClass().getName());
                return;
            }
        }

    }

    @Override
    public void destroy() {

    }
}
