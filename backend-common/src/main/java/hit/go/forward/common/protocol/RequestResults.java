package hit.go.forward.common.protocol;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by 班耀强 on 2018/9/20
 */
public class RequestResults {
    private static Gson JSON;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });
        builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        JSON = builder.create();
    }

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

    public static String wrongParameters(String data) {
        return forbidden("请求参数错误:" + data);
    }

    public static String forbidden(Object data) {
        return JSON.toJson(new RequestResult(403, data));
    }

    public static String badRequest() {
        return badRequest("Bad Request");
    }

    public static String badRequest(Object data) {
        return JSON.toJson(new RequestResult(400, data));
    }

    public static String operationFailed() {
        return JSON.toJson(new RequestResult(400, "操作失败!"));
    }

    public static String operationFailed(String data) {
        return JSON.toJson(new RequestResult(400, data));
    }

    public static String completelySucceeded(String data) {
        return JSON.toJson(new RequestResult(20000, data));
    }

    public static String completelySucceeded() {
        return JSON.toJson(new RequestResult(20000, "success"));
    }

    public static String partSucceeded(int numSucceeded, int numFailed) {
        return JSON.toJson(new RequestResult(20001, numSucceeded + " 执行成功，" + numFailed + " 执行失败，请核查"));
    }

    public static String queryNotExist(String detail) {
        return JSON.toJson(new RequestResult(20002, detail));
    }

    public static String lacksNecessaryParam(String params) {
        return JSON.toJson(new RequestResult(40000, "缺少必须参数：" + params));
    }

    public static String invalidParamValue(String detail) {
        return JSON.toJson(new RequestResult(40001, "参数值错误：" + detail));
    }

    public static String requestCausedDBWritesError(String detail) {
        return JSON.toJson(new RequestResult(40002, detail));
    }

    public static String accessDenied() {
        return JSON.toJson(new RequestResult(40300, "禁止访问!"));
    }

    public static String operationDenied() {
        return JSON.toJson(new RequestResult(40301, "该操作只可执行一次!"));
    }

    public static String notFound() {
        return notFound("Not Found");
    }

    public static String notFound(Object data) {
        return JSON.toJson(new RequestResult(40400, data));
    }


}
