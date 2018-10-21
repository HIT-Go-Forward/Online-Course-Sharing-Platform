package hit.to.go.controller;

import hit.to.go.database.dao.CourseMapper;
import hit.to.go.database.mybatis.MybatisProxy;
import hit.to.go.entity.course.Course;
import hit.to.go.entity.user.User;
import hit.to.go.entity.user.UserWithPassword;
import hit.to.go.platform.AttrKey;
import hit.to.go.platform.SystemStorage;
import hit.to.go.platform.protocol.RequestResult;
import hit.to.go.platform.protocol.RequestResults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/10/1
 */
@Controller
@ResponseBody
@RequestMapping("/course")
public class CourseController {

    @RequestMapping("/addNewCourse")
    public String newCourse(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, @RequestParam Map<String, String> paras) {
        String courseName;
        courseName = paras.get("courseName");
        if (courseName != null) {
            CourseMapper mapper = MybatisProxy.create(CourseMapper.class);
            paras.put("id", user.getId().toString());
            Integer rows = mapper.addNewCourse(paras);
            if (rows != null && rows.equals(1)) return RequestResults.success(paras.get("course_id"));
            return RequestResults.error("课程添加失败！");
        }
        return RequestResults.wrongParameters();
    }

    @RequestMapping("/saveCourseDraft")
    public String saveCourseDraft(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, @RequestParam Map<String, String> paras) {
        String courseName;
        courseName = paras.get("courseName");
        if (courseName != null) {
            CourseMapper mapper = MybatisProxy.create(CourseMapper.class);
            paras.put("id", user.getId().toString());

            Integer rows = mapper.saveDraft(paras);
            if (rows != null && rows.equals(1)) return RequestResults.success(paras.get("course_id"));
            return RequestResults.error("课程添加失败！");
        }
        return RequestResults.wrongParameters();
    }

    @RequestMapping("/updateDraftCourse")
    public String updateDraftCourse(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, @RequestParam Map<String, String> paras) {
        String courseId, courseName;
        courseId = paras.get("courseId");
        courseName = paras.get("courseName");
        if (courseId != null && courseName != null) {
            CourseMapper mapper = MybatisProxy.create(CourseMapper.class);
            paras.put("id", user.getId().toString());
            Integer rows = mapper.updateDraftCourse(paras);
            if (rows != null && rows.equals(1)) return RequestResults.success();
            return RequestResults.error();
        }

        return RequestResults.wrongParameters();
    }

    @RequestMapping("/releaseDraftCourse")
    public String releaseDraftCourse(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, String courseId) {
        if (courseId != null) {
            CourseMapper mapper = MybatisProxy.create(CourseMapper.class);
            Map<String, String> paras = new HashMap<>();
            paras.put("id", user.getId().toString());
            paras.put("courseId", courseId);
            Integer rows = mapper.releaseDraftCourse(paras);
            if (rows!= null && rows.equals(1)) return RequestResults.success();
            return RequestResults.error("该课程可能已被处理!");
        }
        return RequestResults.wrongParameters();
    }

    @RequestMapping("/getAllCourses")
    public String getAllCourses(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user) {

        return RequestResults.success();
    }

    @RequestMapping("/getCourses")
    public String getCourses(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, String keyWord) {

        return RequestResults.success();
    }

    @RequestMapping("/getUserCourses")
    public String getUserCourses(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, String type) {
        String id = user.getId().toString();
        CourseMapper mapper = MybatisProxy.create(CourseMapper.class);
        if (user.getType() == User.TYPE_ADMIN) {
            Map<String, String> paras = new HashMap<>();
            paras.put("id", id);
            paras.put("type", type);
            return RequestResults.success(mapper.getManageableCourses(paras));
        } else if (user.getType() == User.TYPE_TEACHER) {
            if (type == null || type.equals("all")) return RequestResults.success(mapper.getAllTeacherCourses(id));
            else if (type.equals("draft")) return RequestResults.success(mapper.getAllDraftCourses(id));
            else if (type.equals("applying")) return RequestResults.success(mapper.getAllApplyingCourses(id));
            else if (type.equals("rejected")) return RequestResults.success(mapper.getAllRejectedCourses(id));
            else if (type.equals("released")) return RequestResults.success(mapper.getAllReleasedCourses(id));
        } else {
            if (type == null || type.equals("all")) return RequestResults.success(mapper.getAllStudentCourses(id));
            else if (type.equals("joined")) return RequestResults.success(mapper.getJoinedCourses(id));
            else if (type.equals("learning")) return RequestResults.success(mapper.getLearningCourses(id));
            else if (type.equals("learned")) return RequestResults.success(mapper.getLearnedCourses(id));
        }
        return RequestResults.forbidden("错误的type参数!");
    }

    @RequestMapping("/getCourseById")
    public String getCourseById(String courseId) {
        if (courseId != null) {
            CourseMapper mapper = MybatisProxy.create(CourseMapper.class);
            Course course = mapper.getCourseById(courseId);
            return RequestResults.success(course);
        }
        return RequestResults.wrongParameters();
    }

    @RequestMapping("/handleCourseApply")
    public String handleCourseApply(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, String courseId, String operation) {
        if (courseId != null & operation != null) {
            CourseMapper mapper = MybatisProxy.create(CourseMapper.class);
            Integer rows;
            if (operation.equals("accept")) {
                rows = mapper.acceptCourseApply(courseId);
                if (rows.equals(1)) return RequestResults.success();
                return RequestResults.error("该申请可能已被处理!");
            } else if (operation.equals("reject")) {
                rows = mapper.rejectCourseApply(courseId);
                if (rows.equals(1)) return RequestResults.success();
                return RequestResults.error("该申请可能已被处理!");
            }
            return RequestResults.forbidden("错误的operation参数!");
        }

        return RequestResults.wrongParameters();
    }

    @RequestMapping("/getAllCourseType")
    public String getAllCourseType() {
        CourseMapper mapper = MybatisProxy.create(CourseMapper.class);
        return RequestResults.success(mapper.getAllCourseType());
    }
}
