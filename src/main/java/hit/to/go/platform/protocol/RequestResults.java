package hit.to.go.platform.protocol;

import com.google.gson.Gson;

/**
 * Created by 班耀强 on 2018/9/20
 */
public class RequestResults {
    private static final Gson JSON = new Gson();

    public static String success(Object data) {
        return JSON.toJson(new RequestResult(200, data));
    }

    public static String error(Object data) {
        return JSON.toJson(new RequestResult(500, data));
    }

    public static String forbidden(Object data) {
        return JSON.toJson(new RequestResult(403, data));
    }

    public static String notFound(Object data) {
        return JSON.toJson(new RequestResult(404, data));
    }

    public static String badRequest(Object data) {
        return JSON.toJson(new RequestResult(400, data));
    }
}
