package hit.to.go.controller;

import hit.to.go.database.dao.ApplyMapper;
import hit.to.go.entity.Apply;
import hit.to.go.entity.user.UserWithPassword;
import hit.to.go.platform.AttrKey;
import hit.to.go.platform.protocol.RequestResults;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
@ResponseBody
public class ApplyController {
    private ApplyMapper applyMapper;

    public ApplyController(ApplyMapper applyMapper) {
        this.applyMapper = applyMapper;
    }

    @RequestMapping("/applyTeacher")
    public String applyTeacher(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, String note) {
        Date now = new Date();
        Map<String, Object> paras = new HashMap<>();
        paras.put("userId", user.getId());
        paras.put("time", now);
        paras.put("note", note);

        Integer result = applyMapper.applyToBeTeacher(paras);
        if (result != null && result.equals(1)) return RequestResults.success(paras.get("apply_id"));
        else if (result != null && result.equals(0)) return RequestResults.forbidden("您有待处理的申请，请耐心等待管理员处理后再申请！");
        return RequestResults.error();
    }

    @RequestMapping("/acceptTeacherApply")
    public String acceptTeacherApply(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, String applyId, String note) {
        if (applyId == null) return RequestResults.wrongParameters();
        Date now = new Date();
        Map<String, Object> paras = new HashMap<>();
        paras.put("applyId", applyId);
        paras.put("handlerId", user.getId());
        paras.put("handleTime", now);
        paras.put("note", note);

        Integer result = applyMapper.acceptApply(paras);
        if (result != null && result.equals(2)) return RequestResults.success();
        else if (result != null && result.equals(0)) return RequestResults.forbidden("该申请已被处理!");
        return RequestResults.error();
    }

    @RequestMapping("/rejectTeacherApply")
    public String rejectTeacherApply(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, String applyId, String note) {
        if (applyId == null) return RequestResults.wrongParameters();
        Date now = new Date();
        Map<String, Object> paras = new HashMap<>();
        paras.put("applyId", applyId);
        paras.put("handlerId", user.getId());
        paras.put("handleTime", now);
        paras.put("note", note);

        Integer result = applyMapper.rejectApply(paras);
        if (result != null && result.equals(1)) return RequestResults.success();
        else if (result != null && result.equals(0)) return RequestResults.forbidden("该申请已被处理!");
        return RequestResults.error();
    }

    @RequestMapping("/getManageableApplies")
    public String getAllApplies(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, String type) {
        Map<String, String> paras = new HashMap<>();
        paras.put("id", user.getId().toString());
        paras.put("power", user.getType().toString());
        List<Apply> result;
        if (type == null || type.equals("all")) result = applyMapper.getAllApplies(paras);
        else if (type.equals("unhandled")) result = applyMapper.getAllUnhandledApplies(paras);
        else if (type.equals("handled")) result = applyMapper.getAllHandledApplies(paras);
        else return RequestResults.forbidden("错误的type参数!");
        return RequestResults.success(result);
    }
}
