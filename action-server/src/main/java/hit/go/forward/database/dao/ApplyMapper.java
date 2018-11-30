package hit.go.forward.database.dao;

import hit.go.forward.entity.Apply;

import java.util.List;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/10/1
 */

public interface ApplyMapper {
    Integer applyTeacher(Map<String, Object> paras);

    Integer applyToBeTeacher(Map<String, Object> paras);

    Integer acceptApply(Map<String, Object> paras);

    Integer rejectApply(Map<String, Object> paras);

    List<Apply> getAllApplies(Map<String, String> paras);

    List<Apply> getAllUnhandledApplies(Map<String, String> paras);

    List<Apply> getAllHandledApplies(Map<String, String> paras);
}
