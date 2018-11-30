package hit.go.forward.controller;

import hit.go.forward.protocol.RequestResults;
import hit.go.forward.database.dao.HistoryMapper;
import hit.go.forward.entity.history.CourseHistory;
import hit.go.forward.entity.user.UserWithPassword;
import hit.go.forward.platform.AttrKey;
import hit.go.forward.platform.exception.RequestHandleException;
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
 * Created by 班耀强 on 2018/10/3
 */
@Controller
@ResponseBody
@RequestMapping("/history")
public class HistoryController {
    private HistoryMapper historyMapper;

    public HistoryController(HistoryMapper historyMapper) {
        this.historyMapper = historyMapper;
    }

    @Transactional
    @RequestMapping("/addNewHistory")
    public String addNewHistory(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, String courseId) {
        if (courseId != null) {
            Date now = new Date();
            Map<String, Object> paras = new HashMap<>();
            paras.put("id", user.getId());
            paras.put("courseId", courseId);
            paras.put("date", now);
            Integer rows = historyMapper.addNewHistory(paras);
            if (rows != null && rows.equals(1)) return RequestResults.success();
            throw new RequestHandleException(RequestResults.error());
        }
        return RequestResults.wrongParameters();
    }

    @RequestMapping("/getCourseHistory")
    public String getCourseHistory(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user) {
        List<CourseHistory> result = historyMapper.getCourseHistory(user.getId().toString());
        return RequestResults.success(result);
    }
}
