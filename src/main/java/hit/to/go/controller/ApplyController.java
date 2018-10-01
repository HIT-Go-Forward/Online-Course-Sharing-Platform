package hit.to.go.controller;

import com.sun.org.apache.regexp.internal.RE;
import hit.to.go.database.dao.ApplyMapper;
import hit.to.go.database.mybatis.MybatisProxy;
import hit.to.go.entity.Apply;
import hit.to.go.entity.user.User;
import hit.to.go.entity.user.UserWithPassword;
import hit.to.go.platform.SystemStorage;
import hit.to.go.platform.protocol.RequestResult;
import hit.to.go.platform.protocol.RequestResults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/10/1
 */
@Controller
@RequestMapping("/apply")
@ResponseBody
public class ApplyController {
    @RequestMapping("/applyTeacher")
    public String applyTeacher(String id, String password, String note) {
        if (id != null && password != null) {
            Date now = new Date();
            Map<String, Object> paras = new HashMap<>();
            paras.put("userId", id);
            paras.put("time", now);
            paras.put("note", note);
            UserWithPassword user = SystemStorage.getOnlineUser(id);
            if (user == null) return RequestResults.needLogin();
            if (!user.getPassword().equals(password)) return RequestResults.invalidAccountOrPassword();
            if (user.getType() != User.TYPE_STUDENT) return RequestResults.haveNoRight();

            ApplyMapper mapper = MybatisProxy.create(ApplyMapper.class);
            Integer result = mapper.applyTeacher(paras);
            if (result != null && result.equals(1)) return RequestResults.success(paras.get("apply_id"));
            return RequestResults.error();
        }
        return RequestResults.wrongParameters();
    }

    @RequestMapping("/acceptTeacherApply")
    public String acceptTeacherApply(String handlerId, String password, String id) {
        if (handlerId != null && password != null && id != null) {
            UserWithPassword admin = SystemStorage.getOnlineUser(handlerId);
            if (admin == null) return RequestResults.needLogin();
            if (!admin.getPassword().equals(password)) return RequestResults.invalidAccountOrPassword();
            if (admin.getType() != User.TYPE_ADMIN) return RequestResults.haveNoRight();
            Date now = new Date();
            ApplyMapper mapper = MybatisProxy.create(ApplyMapper.class);
            Map<String, Object> paras = new HashMap<>();
            paras.put("id", id);
            paras.put("handlerId", handlerId);
            paras.put("handleTime", now);

            Integer result = mapper.acceptApply(paras);
            if (result != null && result.equals(2)) return RequestResults.success();
            else if (result != null && result.equals(0)) return RequestResults.forbidden("该申请已被处理!");
            return RequestResults.error();
        }
        return RequestResults.wrongParameters();
    }

    @RequestMapping("/rejectTeacherApply")
    public String rejectTeacherApply(String handlerId, String password, String id) {
        if (handlerId != null && password != null && id != null) {
            UserWithPassword admin = SystemStorage.getOnlineUser(handlerId);
            if (admin == null) return RequestResults.needLogin();
            if (!admin.getPassword().equals(password)) return RequestResults.invalidAccountOrPassword();
            if (admin.getType() != User.TYPE_ADMIN) return RequestResults.haveNoRight();
            Date now = new Date();
            ApplyMapper mapper = MybatisProxy.create(ApplyMapper.class);
            Map<String, Object> paras = new HashMap<>();
            paras.put("id", id);
            paras.put("handlerId", handlerId);
            paras.put("handleTime", now);

            Integer result = mapper.rejectApply(paras);
            if (result != null && result.equals(1)) return RequestResults.success();
            else if (result != null && result.equals(0)) return RequestResults.forbidden("该申请已被处理!");
            return RequestResults.error();
        }
        return RequestResults.wrongParameters();
    }
}
