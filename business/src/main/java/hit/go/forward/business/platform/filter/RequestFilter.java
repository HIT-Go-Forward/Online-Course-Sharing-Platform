package hit.go.forward.business.platform.filter;

import hit.go.forward.common.entity.jwt.AuthorityVO;
import hit.go.forward.common.protocol.RequestWrapper;
import hit.go.forward.service.UserAuthorityService;
import hit.go.forward.service.impl.UserAuthorityServiceImpl;
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
    private UserAuthorityService authorityService = new UserAuthorityServiceImpl();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


        String url = request.getRequestURI();
        logger.debug("请求资源 {}", url);
        boolean useCookie = true;

        if (url.endsWith(".action")) {
            RequestWrapper requestWrapper = new RequestWrapper(request);
            String token = request.getParameter("token");
            if (token != null) {
                useCookie = false;
                requestWrapper.addParameter("token", token);
                AuthorityVO vo = authority(token);
                if (vo != null) {
                    requestWrapper.addParameter("$userId", vo.getUserId());
                    requestWrapper.addParameter("$userType", vo.getUserType());
                }
            }

            Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    logger.debug("收到 Cookie {}={}", cookie.getName(), cookie.getValue());
                    switch (cookie.getName()) {
                        case "id":
                            requestWrapper.addParameter("$cookieId", cookie.getValue());
                            break;
                        case "password":
                            requestWrapper.addParameter("$cookiePassword", cookie.getValue());
                            break;
                        case "token":
                            if (useCookie) {
                                token = cookie.getValue();
                                requestWrapper.addParameter("token", token);
                                AuthorityVO vo = authority(token);
                                if (vo != null) {
                                    requestWrapper.addParameter("$userId", vo.getUserId());
                                    requestWrapper.addParameter("$userType", vo.getUserType());
                                }
                            }
                            break;
                    }
                }
            }
            filterChain.doFilter(requestWrapper, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {}

    private AuthorityVO authority(String token) {
        AuthorityVO vo;
        try {
            vo = authorityService.verify(token);
        } catch (Exception e) {
            vo = null;
        }
        return vo;
    }
}
