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

    @Deprecated
    public static String error() {
        return error("Internal Server Error");
    }

    @Deprecated
    public static String error(Object data) {
        return JSON.toJson(new RequestResult(500, data));
    }

    @Deprecated
    public static String dataBaseWriteError() {
        return error("数据库写入失败！");
    }

    @Deprecated
    public static String forbidden() {
        return forbidden("Forbidden");
    }

    @Deprecated
    public static String invalidAccountOrPassword() {
        return forbidden("账号或密码错误!");
    }

    @Deprecated
    public static String needLogin() {
        return forbidden("未登录用户无权操作!");
    }

    @Deprecated
    public static String haveNoRight() {
        return forbidden("您所属的用户类型无权操作!");
    }

    @Deprecated
    public static String wrongParameters() {
        return forbidden("请求参数错误!");
    }

    @Deprecated
    public static String wrongParameters(String data) {
        return forbidden("请求参数错误:" + data);
    }

    @Deprecated
    public static String forbidden(Object data) {
        return JSON.toJson(new RequestResult(403, data));
    }

    @Deprecated
    public static String badRequest() {
        return badRequest("Bad Request");
    }

    @Deprecated
    public static String badRequest(Object data) {
        return JSON.toJson(new RequestResult(400, data));
    }

    @Deprecated
    public static String operationFailed() {
        return JSON.toJson(new RequestResult(400, "操作失败!"));
    }

    @Deprecated
    public static String operationFailed(String data) {
        return JSON.toJson(new RequestResult(400, data));
    }

    public static RequestResult success() {
        return success("success");
    }

    public static RequestResult success(Object data) {
        return new RequestResult(20000, data);
    }

    public static RequestResult partSucceeded(long numSucceeded, long numFailed) {
        return new RequestResult(20001, numSucceeded + " 执行成功，" + numFailed + " 执行失败，请核查");
    }

    public static RequestResult queryNotExist(String detail) {
        return new RequestResult(20002, detail);
    }

    public static RequestResult queryExisted(String detail) {
        return new RequestResult(20003, detail);
    }

    public static RequestResult lackNecessaryParam(String params) {
        return new RequestResult(40000, "缺少必须参数：" + params);
    }

    public static RequestResult invalidParamValue(String detail) {
        return new RequestResult(40001, "参数值错误：" + detail);
    }

    public static RequestResult requestCausedDBWritesError(String detail) {
        return new RequestResult(40002, detail);
    }

    public static RequestResult validateFailed(String detail) {
        return new RequestResult(40003, detail);
    }

    public static RequestResult validateFailed() {
        return new RequestResult(40003, "验证失败!");
    }

    public static RequestResult requestCausedDBWritesError() {
        return new RequestResult(40002, "写入失败，请修改相关信息之后再试！");
    }

    public static RequestResult accessDenied() {
        return new RequestResult(40300, "禁止访问!");
    }

    public static RequestResult operationDenied() {
        return new RequestResult(40301, "请求被禁止执行");
    }

    public static RequestResult operationDenied(String detail) {
        return new RequestResult(40301, detail);
    }

    public static RequestResult notFound() {
        return notFound("Not Found");
    }

    public static RequestResult notFound(Object data) {
        return new RequestResult(40400, data);
    }

    public static RequestResult serverError() {
        return new RequestResult(50000, "Internal Server Error");
    }

    public static RequestResult serverError(String message) {
        return new RequestResult(50000, message);
    }

    public static RequestResult fileWritesError() {
        return new RequestResult(50001, "文件写入失败！");
    }

    public static RequestResult serverConfigError() {
        return new RequestResult(50002, "服务器配置文件错误！");
    }

    public static String toJSON(RequestResult result) {
        return JSON.toJson(result);
    }

    public static RequestResult fromJSON(String json) {
        return JSON.fromJson(json, RequestResult.class);
    }
}
