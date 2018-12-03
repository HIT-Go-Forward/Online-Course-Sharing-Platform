package hit.go.forward.platform.util;

import com.google.gson.Gson;
import hit.go.forward.common.protocol.RequestResult;
import hit.go.forward.platform.SystemConfig;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.util.Enumeration;

/**
 * Created by 班耀强 on 2018/9/21
 */
public class ResourceTransmissionUtil {
    private static final Logger logger = LoggerFactory.getLogger(ResourceTransmissionUtil.class);

    private static final Gson JSON = new Gson();

    private static final String UPLOAD_ACTION = "/resource/upload.action";

    public static RequestResult uploadToMediaServer(File file, String path) {
        RequestBody fileBody = RequestBody.create(MultipartBody.FORM, file);

        RequestBody multipart = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("storePath", path)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();

        Request request = new Request.Builder()
                .url(SystemConfig.getMediaAddress() + UPLOAD_ACTION)
                .post(multipart)
                .build();

        OkHttpClient client = new OkHttpClient();
        try {
            Response response = client.newCall(request).execute();
            return JSON.fromJson(response.body().string(), RequestResult.class);
        } catch (Exception e) {
            logger.error("文件上传资源服务器出错");
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 转发用户上转文件请求
     * 用户资源文件目录结构
     * 课程视频，封面
     * id/courseId/lessonId/filename
     *
     * 头像等其他资源
     * id/filename
     */
    public static boolean upload(String id, String courseId, String lessonId, String filename, HttpServletRequest request, HttpServletResponse response) {
        String path = "/" + String.join("/", new String[]{id, courseId, lessonId, filename});
        return upload(path, request, response);
    }

    public static boolean upload(String id, String filename, HttpServletRequest request, HttpServletResponse response) {
        String path = "/" + id + "/" + filename;
        return upload(path, request, response);
    }

    private static boolean upload(String path, HttpServletRequest request, HttpServletResponse response) {
        String server = SystemConfig.getMediaAddress();
        if (server == null) throw new Error("系统未配置资源服务器！");
        String url = server + UPLOAD_ACTION + "?storePath=" + path;
        return transmitRequest(url, request, response);
    }

    private static boolean transmitRequest(String url, HttpServletRequest request, HttpServletResponse response) {
        Response resp;
        OutputStream out;
        OkHttpClient client = new OkHttpClient();
        Request req;

        Request.Builder builder = new Request.Builder();
        builder.url(url);


        // 读取请求的请求方法以及body
        int len = request.getContentLength();
        byte[] data = new byte[len];
        try {
            ServletInputStream in = request.getInputStream();
            in.read(data, 0, len);
        } catch (Exception e) {
            logger.error("获取请求输入流出错");
            e.printStackTrace();
            return false;
        }
        MediaType mediaType = MediaType.parse(request.getContentType());
        builder.method(request.getMethod(), RequestBody.create(mediaType, data));


        Enumeration<String> names = request.getHeaderNames();

        while (names.hasMoreElements()) {
            String header = names.nextElement();
            builder.header(header, request.getHeader(header));
        }

        req = builder.build();
        try {
            resp = client.newCall(req).execute();
            data = resp.body().bytes();
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
            logger.error("响应写入失败");
            e.printStackTrace();
            return false;
        }

        return true;
    }


}
