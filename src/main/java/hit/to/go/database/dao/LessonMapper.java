package hit.to.go.database.dao;

import hit.to.go.entity.course.Lesson;

import java.util.List;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/10/20
 */
public interface LessonMapper {
    List<Lesson> getAllLessons(String courseId);

    Lesson getLessonById(String lessonId);

    Integer addNewLesson(Map<String, Object> paras);
}
