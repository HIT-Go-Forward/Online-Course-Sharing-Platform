package hit.to.go.database.dao;

import hit.to.go.entity.course.Lesson;

import java.util.List;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/10/20
 */
public interface LessonMapper {
    Integer insertNewLesson(Map<String, Object> paras);

    Integer updateLesson(Map<String, Object> paras);

    Integer updateLessonVideo(Map<String, Object> paras);

    Integer updateLessonFile(Map<String, Object> paras);

    List<Lesson> getCourseLessons(String courseId);

    List<Lesson> getLessonsAccepted(String courseId);

    List<Lesson> getLessonsTeacherOwns(Map<String, Object> paras);

    Lesson getLessonById(String lessonId);
}
