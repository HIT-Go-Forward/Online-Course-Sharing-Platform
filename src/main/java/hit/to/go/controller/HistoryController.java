package hit.to.go.controller;

import hit.to.go.database.dao.HistoryMapper;
import hit.to.go.entity.history.CourseHistory;
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
 * Created by 班耀强 on 2018/10/3
 */
@Controller
@ResponseBody
@Transactional
@RequestMapping("/history")
public class HistoryController {
    private HistoryMapper historyMapper;

    public HistoryController(HistoryMapper historyMapper) {
        this.historyMapper = historyMapper;
    }

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
            return RequestResults.error();
        }
        return RequestResults.wrongParameters();
    }

    @RequestMapping("/getCourseHistory")
    public String getCourseHistory(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user) {
        List<CourseHistory> result = historyMapper.getCourseHistory(user.getId().toString());
        return RequestResults.success(result);
    }
}
