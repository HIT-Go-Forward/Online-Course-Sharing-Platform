package hit.to.go.database.dao;

import hit.to.go.entity.course.Course;
import hit.to.go.entity.course.CourseChapter;
import hit.to.go.entity.course.CourseType;
import hit.to.go.entity.course.StudyCourse;

import java.util.List;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/10/2
 */
public interface CourseMapper {
    Integer addNewCourse(Map<String, Object> paras);

    Integer updateCourse(Map<String, Object> paras);

    Integer acceptCourseApply(String id);

    Integer rejectCourseApply(String id);

    List<StudyCourse> getAllStudentCourses(String id);

    List<StudyCourse> getJoinedCourses(String id);

    List<StudyCourse> getLearningCourses(String id);

    List<StudyCourse> getLearnedCourses(String id);

    List<Course> getAllTeacherCourses(String id);

    List<Course> getAllDraftCourses(String id);

    List<Course> getAllApplyingCourses(String id);

    List<Course> getAllReleasedCourses(String id);

    List<Course> getAllRejectedCourses(String id);

    List<Course> getManageableCourses(Map<String, Object> paras);

    List<Course> getCourseByType(Map<String, Object> paras);

    List<Course> getCourses(Map<String, Object> paras);

    Course getCourseById(String id);

    List<CourseType> getAllCourseType();

    Integer updateCourseImg(Map<String, Object> paras);

    List<CourseChapter> getCourseChapters(String courseId);
}
