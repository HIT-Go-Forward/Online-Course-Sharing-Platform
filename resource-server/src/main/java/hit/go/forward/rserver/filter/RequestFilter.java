package hit.go.forward.rserver.filter;

import hit.go.forward.common.protocol.RequestWrapper;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by 班耀强 on 2018/9/20
 */
public class RequestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String url = request.getRequestURI();
        if (url.endsWith(".action")) {
            Cookie[] cookies = request.getCookies();
            RequestWrapper requestWrapper = new RequestWrapper(request);
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    switch (cookie.getName()) {
                        case "id":
                            requestWrapper.addParameter("$cookieId", cookie.getValue());
                            break;
                        case "password":
                            requestWrapper.addParameter("$cookiePassword", cookie.getValue());
                            break;
                        case "token":
                            requestWrapper.addParameter("token", cookie.getValue());
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
    public void destroy() {

    }
}
