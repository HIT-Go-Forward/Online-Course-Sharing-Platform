package hit.go.forward.controller;

import hit.go.forward.common.entity.operation.OperationResult;
import hit.go.forward.common.protocol.RequestResults;
import hit.go.forward.business.database.dao.CourseMapper;
import hit.go.forward.business.database.dao.LessonMapper;
import hit.go.forward.common.entity.course.Course;
import hit.go.forward.common.entity.user.User;
import hit.go.forward.common.exception.RequestHandleException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/10/1
 */
@Controller
@ResponseBody
@RequestMapping("/course")
public class CourseController {

    private CourseMapper courseMapper;
    private LessonMapper lessonMapper;

    public CourseController(CourseMapper courseMapper, LessonMapper lessonMapper) {
        this.courseMapper = courseMapper;
        this.lessonMapper = lessonMapper;
    }

    @Transactional
    @RequestMapping("/addNewCourse")
    public String newCourse(@RequestParam Map<String, Object> paras, String $userId) {
        if (paras.get("courseName") != null) {
            paras.put("teacherId", $userId);
            Integer rows = courseMapper.addNewCourse(paras);
            if (rows != null && rows.equals(1)) return RequestResults.success(paras);
            throw new RequestHandleException(RequestResults.operationDenied("课程添加失败！"));
        }
        return RequestResults.lackNecessaryParam("courseName");
    }

    @Transactional
    @RequestMapping("/updateCourse")
    public String updateCourse(@RequestParam Map<String, Object> paras, String $userId) {
        if (paras.get("courseName") == null) return RequestResults.lackNecessaryParam("courseName");
        paras.put("teacherId", $userId);
        Integer rows = courseMapper.updateCourse(paras);
        if (rows != null && rows.equals(1)) return RequestResults.success();
        throw new RequestHandleException(RequestResults.requestCausedDBWritesError("数据库写入失败！"));
    }

    @Transactional
    @RequestMapping("/updateCourseImg")
    public String updateCourseImg(String fileId, String courseId, String $userId) {
        if (fileId == null || courseId == null) return RequestResults.lackNecessaryParam("fileId || courseId");
        Map<String, Object> paras = new HashMap<>();
        paras.put("img", fileId);
        paras.put("courseId", courseId);
        paras.put("teacherId", $userId);
        Integer rows = courseMapper.updateCourseImg(paras);
        if (rows != null && rows.equals(1)) return RequestResults.success();
        throw new RequestHandleException("更新失败!");
    }

    @RequestMapping("/getCourses")
    public String getCourses(Integer start, Integer length) {
        Map<String, Object> paras = new HashMap<>();
        paras.put("start", start);
        paras.put("length", length);
        return RequestResults.success(courseMapper.getCourses(paras));
    }

    @RequestMapping("/getCourseByType")
    public String getCourseByType(String typeId, String $userId, Integer $userType, Integer start, Integer length) {
        if (typeId == null) return RequestResults.lackNecessaryParam("typeId");
        Map<String, Object> paras = new HashMap<>();
        paras.put("start", start);
        paras.put("length", length);
        paras.put("typeId", typeId);
        paras.put("userType", $userType);
        paras.put("userId", $userId);
        return RequestResults.success(courseMapper.getCourseByType(paras));
    }

    @RequestMapping("/getUserCourses")
    public String getUserCourses(String $userId, Integer $userType, String type, Integer start, Integer length) {
        if (type == null) return RequestResults.lackNecessaryParam("type");

        Map<String, Object> paras = new HashMap<>();
        paras.put("userId", $userId);
        paras.put("type", type);
        paras.put("start", start);
        paras.put("length", length);

        if (type.equals("learning") || type.equals("learned")) {
            return RequestResults.success(courseMapper.getStudentCourses(paras));
        } else if ($userType <= User.TYPE_TEACHER && (type.equals("applying") || type.equals("rejected") || type.equals("released"))) {
            return RequestResults.success(courseMapper.getTeacherCourses(paras));
        } else if ($userType <= User.TYPE_ADMIN && (type.equals("pending") || type.equals("accepted") || type.equals("rebutted"))) {
            return RequestResults.success(courseMapper.getAdminCourses(paras));
        }
        return RequestResults.invalidParamValue("type=" + type);
    }

    @RequestMapping("/getCourseById")
    public String getCourseById(String courseId) {
        if (courseId != null) {
            Course course = courseMapper.getCourseById(courseId);
            return RequestResults.success(course);
        }
        return RequestResults.lackNecessaryParam("courseId");
    }

    @Transactional
    @RequestMapping("/handleCourseApply")
    public String handleCourseApply(String courseId, String operation) {
        if (courseId != null & operation != null) {
            Integer rows;
            if (operation.equals("accept")) {
                rows = courseMapper.acceptCourseApply(courseId);
                if (rows >= 1) return RequestResults.success();
                throw new RequestHandleException(RequestResults.operationDenied("该申请可能已被处理!"));
            } else if (operation.equals("reject")) {
                rows = courseMapper.rejectCourseApply(courseId);
                if (rows >= 1) return RequestResults.success();
                throw new RequestHandleException(RequestResults.operationDenied("该申请可能已被处理!"));
            }
            return RequestResults.invalidParamValue("operation=" + operation);
        }
        return RequestResults.lackNecessaryParam("courseId || opertaion");
    }

    @RequestMapping("/getAllCourseType")
    public String getAllCourseType() {
        return RequestResults.success(courseMapper.getAllCourseType());
    }

    @RequestMapping("/getCourseOutline")
    public String getCourseOutline(String courseId, Integer $userType) {
        if (courseId == null) return RequestResults.lackNecessaryParam("courseId");
        else if ($userType == null || $userType > User.TYPE_TEACHER) return RequestResults.success(courseMapper.getCourseChapters(courseId));
        return RequestResults.success(courseMapper.getTeacherCourseChapters(courseId));
    }

    @RequestMapping("/getCourseOutlineDetail")
    public String getCourseOutlineDetail(String courseId, Integer $userType, Integer start, Integer length) {
        if (courseId == null) return RequestResults.lackNecessaryParam("courseId");
        Map<String, Object> param = new HashMap<>();
        param.put("courseId", courseId);
        param.put("start", start);
        param.put("length", length);
        if ($userType != null && $userType <= User.TYPE_TEACHER) param.put("type", "all");

        return RequestResults.success(courseMapper.getCourseChaptersDetail(param));
    }

    @RequestMapping("/getManageableCourseLessons")
    public String getManageableCourseLessons(Integer start, Integer length) {
        Map<String, Object> paras = new HashMap<>();
        paras.put("start", start);
        paras.put("length", length);
        return RequestResults.success(courseMapper.getManageableCourseLessons(paras));
    }

    @RequestMapping("/hasJoined")
    public String hasJoined(String courseId, String $userId) {
        if (courseId == null) return RequestResults.lackNecessaryParam("courseId");
        else if ($userId == null) return RequestResults.success("用户未参加该课程!");

        Map<String, Object> paras = new HashMap<>();
        paras.put("courseId", courseId);
        paras.put("userId", $userId);
        OperationResult result = courseMapper.getWhetherUserJoinedCourse(paras);
        if (result == null) return RequestResults.success("用户未参加该课程!");
        return RequestResults.success();
    }

    @RequestMapping("/joinCourse")
    public String joinCourse(String courseId, String $userId) {
        if (courseId == null) return RequestResults.lackNecessaryParam("courseId");

        Map<String, Object> paras = new HashMap<>();
        paras.put("courseId", courseId);
        paras.put("userId", $userId);
        Integer rows = courseMapper.insertStudy(paras);
        if (rows != null && rows.equals(1)) return RequestResults.success();
        throw new RequestHandleException(RequestResults.operationDenied("用户可能已经开始学习该课程"));
    }

    @RequestMapping("/completeCourseStudy")
    public String completeCourse(String courseId, String $userId) {
        if (courseId == null) return RequestResults.lackNecessaryParam("courseId");

        Map<String, Object> paras = new HashMap<>();
        paras.put("courseId", courseId);
        paras.put("userId", $userId);
        Integer rows = courseMapper.updateCompleteStudy(paras);
        if (rows != null && rows.equals(1)) return RequestResults.success();
        throw new RequestHandleException(RequestResults.operationDenied("用户可能已完成该课程"));
    }

    /**
     * ============== Lesson 相关action ====================
     */
    @Transactional
    @RequestMapping("/addNewLesson")
    public String addNewLesson(@RequestParam Map<String, Object> paras, String $userId) {
        Object num = paras.get("num");
        Object title = paras.get("title");
        Object chapterNum = paras.get("chapterNum");
        Object courseId = paras.get("courseId");
        if (num == null || title == null || chapterNum == null || courseId == null) return RequestResults.lackNecessaryParam("num || title || chapterNum || courseId");
        paras.put("teacherId", $userId);
        Integer rows = lessonMapper.insertNewLesson(paras);
        if (rows != null && rows.equals(1)) return RequestResults.success(paras.get("id"));
        throw new RequestHandleException(RequestResults.requestCausedDBWritesError("lesson无法写入，请修改lesson信息后再试！"));
    }

    @Transactional
    @RequestMapping("/updateLesson")
    public String updateLesson(@RequestParam Map<String, Object> paras, String $userId) {
        Object num = paras.get("num");
        Object title = paras.get("title");
        Object chapterNum = paras.get("chapterNum");
        Object courseId = paras.get("courseId");
        Object lessonId = paras.get("lessonId");
        if (num == null || title == null || chapterNum == null || courseId == null || lessonId == null) return RequestResults.lackNecessaryParam("num || title || chapterNum || courseId || lessonId");
        paras.put("teacherId", $userId);
        Integer rows = lessonMapper.updateLesson(paras);
        if (rows != null && rows.equals(1)) return RequestResults.success();
        throw new RequestHandleException(RequestResults.requestCausedDBWritesError("更新失败，请尝试修改信息之后再试！"));
    }

    @Transactional
    @RequestMapping("/updateLessonVideo")
    public String updateLessonVideo(@RequestParam Map<String, Object> paras, String $userId) {
        Object lessonId = paras.get("lessonId");
        Object fileId = paras.get("fileId");
        if (lessonId == null || fileId == null) return RequestResults.lackNecessaryParam("lessonId || fileId");
        paras.put("teacherId", $userId);
        Integer rows = lessonMapper.updateLessonVideo(paras);
        if (rows != null && rows.equals(1)) return RequestResults.success();
        throw new RequestHandleException(RequestResults.requestCausedDBWritesError("文件更新失败！请稍后重试"));
    }

    @Transactional
    @RequestMapping("/updateLessonFile")
    public String updateLessonFile(@RequestParam Map<String, Object> paras, String $userId) {
        Object lessonId = paras.get("lessonId");
        Object fileId = paras.get("fileId");
        if (lessonId == null || fileId == null) return RequestResults.lackNecessaryParam("lessonId || fileId");
        paras.put("teacherId", $userId);
        Integer rows = lessonMapper.updateLessonFile(paras);
        if (rows != null && rows.equals(1)) return RequestResults.success();
        throw new RequestHandleException(RequestResults.requestCausedDBWritesError());
    }

    @RequestMapping("/startLearnLesson")
    public String startLearnLesson(String lessonId, String $userId) {
        if (lessonId == null) return RequestResults.lackNecessaryParam("lessonId");

        Map<String, Object> paras = new HashMap<>();
        paras.put("lessonId", lessonId);
        paras.put("userId", $userId);

        Integer rows = lessonMapper.insertLessonLearn(paras);
        if (rows != null && rows.equals(1)) return RequestResults.success();
        throw new RequestHandleException("操作失败！用户可能已开始学习该lesson");
    }

    @RequestMapping("/completeLessonLearn")
    public String completeLessonLearn(String lessonId, String $userId) {
        if (lessonId == null) return RequestResults.lackNecessaryParam("lessonId");

        Map<String, Object> paras = new HashMap<>();
        paras.put("lessonId", lessonId);
        paras.put("userId", $userId);

        Integer rows = lessonMapper.updateCompleteLessonLearn(paras);
        if (rows != null && rows.equals(1)) return RequestResults.success();
        throw new RequestHandleException("操作失败！用户可能已完成该lesson的学习");
    }

    @RequestMapping("/getCourseLessons")
    public String getCourseLessons(String  courseId) {
        return RequestResults.success(lessonMapper.getCourseLessons(courseId));
    }

    @RequestMapping("/getLessonById")
    public String getLessonById(String lessonId, Integer $userType) {

        if (lessonId == null) return RequestResults.lackNecessaryParam("lessonId");
        else if ($userType == null || $userType >= User.TYPE_STUDENT) return RequestResults.success(lessonMapper.getLessonById(lessonId));
        else {
            Map<String, String> paras = new HashMap<>();
            paras.put("lessonId", lessonId);
            if ($userType == User.TYPE_TEACHER) paras.put("limit", "teacher");

            return RequestResults.success(lessonMapper.getAdminLessonById(paras));
        }
    }

    @Transactional
    @RequestMapping("/handleLessonApplies")
    public String handleLessonApplies(@RequestParam("lessonIds[]") Integer[] lessonIds, String operation) {
        if (lessonIds == null || operation == null) return RequestResults.lackNecessaryParam("lessonIds||operation");
        else if (!operation.equals("accept") && !operation.equals("reject")) return RequestResults.invalidParamValue("operation");
        Map<String, Object> paras = new HashMap<>();
        paras.put("lessonIds", lessonIds);
        paras.put("operation", operation);
        Integer rows = lessonMapper.updateLessonState(paras);
        if (rows == null) return RequestResults.serverError();
        else if (rows.equals(lessonIds.length)) return RequestResults.success();
        return RequestResults.success(rows + "条操作成功，" + (lessonIds.length - rows) + "操作失败！请核对。");
    }
}
