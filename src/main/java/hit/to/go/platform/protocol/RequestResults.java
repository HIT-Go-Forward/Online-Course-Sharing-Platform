package hit.to.go.platform.protocol;

import com.google.gson.Gson;

/**
 * Created by 班耀强 on 2018/9/20
 */
public class RequestResults {
    private static final Gson JSON = new Gson();

    public static String success() {
        return success("success");
    }

    public static String success(Object data) {
        return JSON.toJson(new RequestResult(200, data));
    }

    public static String error() {
        return error("Internal Server Error");
    }

    public static String error(Object data) {
        return JSON.toJson(new RequestResult(500, data));
    }

    public static String dataBaseWriteError() {
        return error("数据库写入失败！");
    }

    public static String forbidden() {
        return forbidden("Forbidden");
    }

    public static String invalidAccountOrPassword() {
        return forbidden("账号或密码错误!");
    }

    public static String needLogin() {
        return forbidden("未登录用户无权操作!");
    }

    public static String haveNoRight() {
        return forbidden("您所属的用户类型无权操作!");
    }

    public static String wrongParameters() {
        return forbidden("请求参数错误!");
    }

    public static String forbidden(Object data) {
        return JSON.toJson(new RequestResult(403, data));
    }

    public static String notFound() {
        return notFound("Not Found");
    }

    public static String notFound(Object data) {
        return JSON.toJson(new RequestResult(404, data));
    }

    public static String badRequest() {
        return badRequest("Bad Request");
    }

    public static String badRequest(Object data) {
        return JSON.toJson(new RequestResult(400, data));
    }
}
