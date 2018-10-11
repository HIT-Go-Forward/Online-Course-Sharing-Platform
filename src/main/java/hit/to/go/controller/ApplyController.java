package hit.to.go.controller;

import com.sun.org.apache.regexp.internal.RE;
import hit.to.go.database.dao.ApplyMapper;
import hit.to.go.database.mybatis.MybatisProxy;
import hit.to.go.entity.Apply;
import hit.to.go.entity.user.User;
import hit.to.go.entity.user.UserWithPassword;
import hit.to.go.platform.AttrKey;
import hit.to.go.platform.SystemStorage;
import hit.to.go.platform.protocol.RequestResult;
import hit.to.go.platform.protocol.RequestResults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

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
    @RequestMapping("/applyTeacher")
    public String applyTeacher(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, String note) {
        Date now = new Date();
        Map<String, Object> paras = new HashMap<>();
        paras.put("userId", user.getId());
        paras.put("time", now);
        paras.put("note", note);

        ApplyMapper mapper = MybatisProxy.create(ApplyMapper.class);
        Integer result = mapper.applyToBeTeacher(paras);
        if (result != null && result.equals(1)) return RequestResults.success(paras.get("apply_id"));
        else if (result != null && result.equals(0)) return RequestResults.forbidden("您有待处理的申请，请耐心等待管理员处理后再申请！");
        return RequestResults.error();
    }

    @RequestMapping("/acceptTeacherApply")
    public String acceptTeacherApply(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, String applyId, String note) {
        if (applyId == null) return RequestResults.wrongParameters();
        Date now = new Date();
        ApplyMapper mapper = MybatisProxy.create(ApplyMapper.class);
        Map<String, Object> paras = new HashMap<>();
        paras.put("applyId", applyId);
        paras.put("handlerId", user.getId());
        paras.put("handleTime", now);
        paras.put("note", note);

        Integer result = mapper.acceptApply(paras);
        if (result != null && result.equals(2)) return RequestResults.success();
        else if (result != null && result.equals(0)) return RequestResults.forbidden("该申请已被处理!");
        return RequestResults.error();
    }

    @RequestMapping("/rejectTeacherApply")
    public String rejectTeacherApply(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, String applyId, String note) {
        if (applyId == null) return RequestResults.wrongParameters();
        Date now = new Date();
        ApplyMapper mapper = MybatisProxy.create(ApplyMapper.class);
        Map<String, Object> paras = new HashMap<>();
        paras.put("applyId", applyId);
        paras.put("handlerId", user.getId());
        paras.put("handleTime", now);
        paras.put("note", note);

        Integer result = mapper.rejectApply(paras);
        if (result != null && result.equals(1)) return RequestResults.success();
        else if (result != null && result.equals(0)) return RequestResults.forbidden("该申请已被处理!");
        return RequestResults.error();
    }

    @RequestMapping("/getApplies")
    public String getAllApplies(String type) {
        ApplyMapper mapper = MybatisProxy.create(ApplyMapper.class);
        List<Apply> result;
        if (type == null || type.equals("all")) result = mapper.getAllApplies();
        else if (type.equals("unhandled")) result = mapper.getAllUnhandledApplies();
        else if (type.equals("handled")) result = mapper.getAllHandledApplies();
        else return RequestResults.forbidden("错误的type参数!");
        return RequestResults.success(result);
    }
}
