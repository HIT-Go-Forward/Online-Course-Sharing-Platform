package hit.to.go.database.dao;

import hit.to.go.entity.course.Course;
import hit.to.go.entity.course.StudyCourse;

import java.util.List;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/10/2
 */
public interface CourseMapper {
    Integer addNewCourse(Map<String, String> paras);

    Integer saveDraft(Map<String, String> paras);

    Integer releaseDraftCourse(Map<String, String> paras);

    Integer acceptCourseApply(String id);

    Integer rejectCourseApply(String id);

    List<StudyCourse> getAllStudentCourses(String id);

    List<StudyCourse> getJoinedCourses(String id);

    List<StudyCourse> getLearningCourses(String id);

    List<StudyCourse> getLearnedCourses(String id);

    List<Course> getAllTeacherCourses(String id);
}
