package hit.to.go.entity.http;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Created by 班耀强 on 2018/9/21
 */
public class MediaTransmissionResponse extends HttpServletResponseWrapper {
    private OutputStream out;
    private PrintWriter writer;

    public MediaTransmissionResponse(HttpServletResponse response) {
        super(response);

        try {
            out = response.getOutputStream();
            writer = response.getWriter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return super.getOutputStream();
    }

}
