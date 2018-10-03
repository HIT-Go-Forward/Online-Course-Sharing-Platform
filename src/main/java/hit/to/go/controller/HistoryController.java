package hit.to.go.controller;

import hit.to.go.database.dao.HistoryMapper;
import hit.to.go.database.mybatis.MybatisProxy;
import hit.to.go.entity.history.CourseHistory;
import hit.to.go.entity.history.History;
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
 * Created by 班耀强 on 2018/10/3
 */
@Controller
@ResponseBody
@RequestMapping("/history")
public class HistoryController {

    @RequestMapping("/addNewHistory")
    public String addNewHistory(String id, String password, String courseId) {
        if (id != null && password != null && courseId != null) {
            UserWithPassword user = SystemStorage.getOnlineUser(id);
            if (user == null) return RequestResults.needLogin();
            else if (!user.getPassword().equals(password)) return RequestResults.invalidAccountOrPassword();
            Date now = new Date();
            Map<String, Object> paras = new HashMap<>();
            paras.put("id", id);
            paras.put("courseId", courseId);
            paras.put("date", now);
            HistoryMapper mapper = MybatisProxy.create(HistoryMapper.class);
            Integer rows = mapper.addNewHistory(paras);
            if (rows != null && rows.equals(1)) return RequestResults.success();
            return RequestResults.error();
        }
        return RequestResults.wrongParameters();
    }

    @RequestMapping("/getCourseHistory")
    public String getCourseHistory(String id, String password) {
        if (id != null && password != null) {
            UserWithPassword user = SystemStorage.getOnlineUser(id);
            if (user == null) return RequestResults.needLogin();
            else if (!user.getPassword().equals(password)) return RequestResults.invalidAccountOrPassword();
            HistoryMapper mapper = MybatisProxy.create(HistoryMapper.class);
            return RequestResults.success(mapper.getCourseHistory(id));
        }
        return RequestResults.wrongParameters();
    }
}
