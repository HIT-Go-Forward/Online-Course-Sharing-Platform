package hit.go.forward.controller;

import hit.go.forward.common.entity.Apply;
import hit.go.forward.common.entity.blog.Blog;
import hit.go.forward.common.protocol.RequestResult;
import hit.go.forward.common.protocol.RequestResults;
import hit.go.forward.business.database.dao.ApplyMapper;
import hit.go.forward.business.database.mongo.MongoDB;
import hit.go.forward.common.exception.RequestHandleException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/10/1
 */
@Controller
@RequestMapping("/apply")
@ResponseBody
public class ApplyController {
    private ApplyMapper applyMapper;

    public ApplyController(ApplyMapper applyMapper) {
        this.applyMapper = applyMapper;
    }

    @Transactional
    @RequestMapping("/applyTeacher")
    public RequestResult applyTeacher(String $userId, String note) {
        Date now = new Date();
        Map<String, Object> paras = new HashMap<>();
        paras.put("userId", $userId);
        paras.put("time", now);
        paras.put("note", note);

        Integer result = applyMapper.applyToBeTeacher(paras);
        if (result != null && result.equals(1)) return RequestResults.success(paras.get("apply_id"));
        else if (result != null && result.equals(0)) return RequestResults.operationDenied("您有待处理的申请，请耐心等待管理员处理后再申请！");
        return RequestResults.serverConfigError();
    }

    @Transactional
    @RequestMapping("/acceptTeacherApply")
    public RequestResult acceptTeacherApply(String $userId, String applyId, String note) {
        if (applyId == null) return RequestResults.lackNecessaryParam("applyId");
        Date now = new Date();
        Map<String, Object> paras = new HashMap<>();
        paras.put("applyId", applyId);
        paras.put("handlerId", $userId);
        paras.put("handleTime", now);
        paras.put("note", note);

        Integer result = applyMapper.acceptApply(paras);
        if (result != null && result.equals(2)) return RequestResults.success();
        else if (result != null && result.equals(0)) return RequestResults.operationDenied("该申请已被处理!");
        throw new RequestHandleException(RequestResults.serverError("处理失败！"));
    }

    @Transactional
    @RequestMapping("/rejectTeacherApply")
    public RequestResult rejectTeacherApply(String $userId, String applyId, String note) {
        if (applyId == null) return RequestResults.lackNecessaryParam("applyId");
        Date now = new Date();
        Map<String, Object> paras = new HashMap<>();
        paras.put("applyId", applyId);
        paras.put("handlerId", $userId);
        paras.put("handleTime", now);
        paras.put("note", note);

        Integer result = applyMapper.rejectApply(paras);
        if (result != null && result.equals(1)) return RequestResults.success();
        else if (result != null && result.equals(0)) return RequestResults.operationDenied("该申请已被处理!");
        throw new RequestHandleException(RequestResults.serverError("处理失败！"));
    }

    @RequestMapping("/getManageableApplies")
    public RequestResult getAllApplies(String $userId, Integer $userType, String type) {
        Map<String, String> paras = new HashMap<>();
        paras.put("id", $userId);
        paras.put("power", $userType.toString());
        List<Apply> result;
        if (type == null || type.equals("all")) result = applyMapper.getAllApplies(paras);
        else if (type.equals("unhandled")) result = applyMapper.getAllUnhandledApplies(paras);
        else if (type.equals("handled")) result = applyMapper.getAllHandledApplies(paras);
        else return RequestResults.invalidParamValue("type");
        return RequestResults.success(result);
    }

    @RequestMapping("/handleBlogApplies")
    public RequestResult handleBlogApplies(String[] ids, String operation) {
        long successCount = MongoDB.updateBlog(ids, "status", operation.equals("accept") ? Blog.STATUS_RELEASED : Blog.STATUS_REJECTED);
        if (successCount == ids.length) return RequestResults.success();
        return RequestResults.partSucceeded(successCount, ids.length - successCount);
    }

    @RequestMapping("/getBlogApplies")
    public RequestResult getBlogApplies(Integer start, Integer length) {
        return RequestResults.success(MongoDB.getBlogByField("status", Blog.STATUS_PENDING, start, length));
    }
}
