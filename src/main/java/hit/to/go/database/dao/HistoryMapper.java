package hit.to.go.database.dao;

import hit.to.go.entity.history.CourseHistory;
import hit.to.go.entity.history.History;

import java.util.List;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/10/3
 */
public interface HistoryMapper {
    List<CourseHistory> getCourseHistory(String id);

    Integer addNewHistory(Map<String, Object> paras);
}
