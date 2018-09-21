package hit.to.go.platform.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Enumeration;

/**
 * Created by 班耀强 on 2018/9/21
 */
public class MediaTransmissionUtil {
    public static Response transmission(String url) {
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(req).execute()) {
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void transmission(Integer id, HttpServletRequest request, HttpServletResponse response) {
        Response resp;
        OutputStream out;
        String url = MediaResolver.getRealUrl(id);
        if (url == null) {
            try {
                response.sendError(404);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        OkHttpClient client = new OkHttpClient();
        Request req;
        Request.Builder builder = new Request.Builder();
        builder.url(url);

        Enumeration<String> names = request.getHeaderNames();

        while (names.hasMoreElements()) {
            String header = names.nextElement();
            builder.header(header, request.getHeader(header));
        }

        req = builder.build();
        try {
            resp = client.newCall(req).execute();
            byte[] data = resp.body().bytes();
            response.setContentLengthLong(resp.body().contentLength());
            response.setContentType(resp.body().contentType().type());
            for (String n : resp.headers().names()) {
                response.setHeader(n, resp.header(n));
            }

            response.setStatus(resp.code());
            out = response.getOutputStream();
            out.write(data);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
