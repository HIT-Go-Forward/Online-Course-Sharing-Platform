package hit.go.forward.rserver.filter;

import javax.servlet.*;
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
        HttpSession session = request.getSession();

        String url = request.getRequestURI();
        if (url.endsWith(".media")) {
            request.getRequestDispatcher(url.replace(".media", "")).forward(request, response);
            return;
        } else if (url.startsWith("/resource") && !url.endsWith(".action")) {
            File file = new File(session.getServletContext().getRealPath(url));
            if (file.exists()) {
                FileInputStream inputStream = new FileInputStream(file);
                OutputStream out = response.getOutputStream();
                int data;
                while ((data = inputStream.read()) != -1) out.write(data);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
