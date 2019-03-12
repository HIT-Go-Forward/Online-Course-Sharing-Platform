package hit.go.forward.business.database.dao;

import hit.go.forward.common.entity.history.CourseHistory;
import hit.go.forward.common.entity.history.param.HistoryQueryParam;

import java.util.List;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/10/3
 */
public interface HistoryMapper {
    List<CourseHistory> getCourseHistory(HistoryQueryParam param);

    Integer addNewHistory(Map<String, Object> paras);
}
