package hit.go.forward.database.dao;

import hit.go.forward.common.entity.school.School;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 班耀强 on 2018/10/13
 */
public interface SchoolMapper {
    List<School> querySchool(@Param("keyword") String keyword);
}
