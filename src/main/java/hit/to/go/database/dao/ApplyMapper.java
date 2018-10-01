package hit.to.go.database.dao;

import java.util.Map;

/**
 * Created by 班耀强 on 2018/10/1
 */
public interface ApplyMapper {
    Integer applyTeacher(Map<String, Object> paras);

    Integer acceptApply(Map<String, Object> paras);

    Integer rejectApply(Map<String, Object> paras);
}
