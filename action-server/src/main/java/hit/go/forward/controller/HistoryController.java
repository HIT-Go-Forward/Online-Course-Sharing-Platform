package hit.go.forward.controller;

import hit.go.forward.common.protocol.RequestResults;
import hit.go.forward.business.database.dao.HistoryMapper;
import hit.go.forward.common.entity.history.CourseHistory;
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
    public String addNewHistory(String $userId, String courseId) {
        if (courseId != null) {
            Date now = new Date();
            Map<String, Object> paras = new HashMap<>();
            paras.put("id", $userId);
            paras.put("courseId", courseId);
            paras.put("date", now);
            Integer rows = historyMapper.addNewHistory(paras);
            if (rows != null && rows.equals(1)) return RequestResults.success();
            throw new RequestHandleException(RequestResults.error());
        }
        return RequestResults.wrongParameters();
    }

    @RequestMapping("/getCourseHistory")
    public String getCourseHistory(String $userId) {
        List<CourseHistory> result = historyMapper.getCourseHistory($userId);
        return RequestResults.success(result);
    }
}
