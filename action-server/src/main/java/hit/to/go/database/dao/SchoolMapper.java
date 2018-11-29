package hit.to.go.database.dao;

import hit.to.go.entity.school.School;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 班耀强 on 2018/10/13
 */
public interface SchoolMapper {
    List<School> querySchool(@Param("keyword") String keyword);
}
