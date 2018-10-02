package hit.to.go.controller;

import hit.to.go.database.dao.CourseMapper;
import hit.to.go.database.mybatis.MybatisProxy;
import hit.to.go.entity.user.User;
import hit.to.go.entity.user.UserWithPassword;
import hit.to.go.platform.PlatformAttrKey;
import hit.to.go.platform.SystemStorage;
import hit.to.go.platform.protocol.RequestResults;
import org.springframework.stereotype.Controller;
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
    @RequestMapping("/addNewCourse")
    public String newCourse(@RequestParam Map<String, String> paras) {
        String id, password, courseName, img;
        id = paras.get("id");
        password = paras.get("password");
        courseName = paras.get("courseName");
        img = paras.get("img");
        if (id != null && password != null && courseName != null) {
            UserWithPassword user = SystemStorage.getOnlineUser(id);
            if (user == null) return RequestResults.needLogin();
            else if (!user.getPassword().equals(password)) return RequestResults.invalidAccountOrPassword();
            else if (user.getType() != User.TYPE_TEACHER) return RequestResults.haveNoRight();
            CourseMapper mapper = MybatisProxy.create(CourseMapper.class);
            if (img == null) paras.put("img", PlatformAttrKey.DEFAULT_COURSE_IMG);

            Integer rows = mapper.addNewCourse(paras);
            if (rows != null && rows.equals(1)) return RequestResults.success(paras.get("course_id"));
            return RequestResults.error();
        }
        return RequestResults.wrongParameters();
    }

    @RequestMapping("/saveCourseDraft")
    public String saveCourseDraft(@RequestParam Map<String, String> paras) {
        String id, password, courseName, img;
        id = paras.get("id");
        password = paras.get("password");
        courseName = paras.get("courseName");
        img = paras.get("img");
        if (id != null && password != null && courseName != null) {
            UserWithPassword user = SystemStorage.getOnlineUser(id);
            if (user == null) return RequestResults.needLogin();
            else if (!user.getPassword().equals(password)) return RequestResults.invalidAccountOrPassword();
            else if (user.getType() != User.TYPE_TEACHER) return RequestResults.haveNoRight();
            CourseMapper mapper = MybatisProxy.create(CourseMapper.class);
            if (img == null) paras.put("img", PlatformAttrKey.DEFAULT_COURSE_IMG);
            Integer rows = mapper.saveDraft(paras);
            if (rows != null && rows.equals(1)) return RequestResults.success(paras.get("course_id"));
            return RequestResults.error();
        }
        return RequestResults.wrongParameters();
    }

    @RequestMapping("/releaseDraftCourse")
    public String releaseDraftCourse(String id, String password, String courseId) {
        if (id != null && password != null && courseId != null) {
            UserWithPassword teacher = SystemStorage.getOnlineUser(id);
            if (teacher == null) return RequestResults.needLogin();
            else if (!teacher.getPassword().equals(password)) return RequestResults.invalidAccountOrPassword();
            else if (teacher.getType() != User.TYPE_TEACHER) return RequestResults.haveNoRight();

            CourseMapper mapper = MybatisProxy.create(CourseMapper.class);
            Map<String, String> paras = new HashMap<>();
            paras.put("id", id);
            paras.put("courseId", courseId);
            Integer rows = mapper.releaseDraftCourse(paras);
            if (rows!= null && rows.equals(1)) return RequestResults.success();
            return RequestResults.error("该课程可能已被处理!");
        }
        return RequestResults.wrongParameters();
    }

    @RequestMapping("/getCourses")
    public String getCourses(String id, String password, String type) {
        if (id != null && password != null) {
            UserWithPassword user = SystemStorage.getOnlineUser(id);
            if (user == null) return RequestResults.needLogin();
            else if (!user.getPassword().equals(password)) return RequestResults.invalidAccountOrPassword();
            CourseMapper mapper = MybatisProxy.create(CourseMapper.class);
            if (user.getType() == User.TYPE_TEACHER) {
                if (type == null || type.equals("all")) return RequestResults.success(mapper.getAllTeacherCourses(id));
                else if (type.equals("draft")) return RequestResults.success();
                else if (type.equals("applying")) return RequestResults.success();
                else if (type.equals("released")) return RequestResults.success();
                return RequestResults.forbidden("错误的type参数!");
            } else {
                if (type == null || type.equals("all")) return RequestResults.success(mapper.getAllStudentCourses(id));
                else if (type.equals("joined")) return RequestResults.success(mapper.getJoinedCourses(id));
                else if (type.equals("learning")) return RequestResults.success(mapper.getLearningCourses(id));
                else if (type.equals("learned")) return RequestResults.success(mapper.getLearnedCourses(id));
                return RequestResults.forbidden("错误的type参数!");
            }
        }
        return RequestResults.wrongParameters();
    }

    @RequestMapping("/handleCourseApply")
    public String handleCourseApply(String id, String password, String courseId, String operation) {
        if (id != null && password != null && courseId != null & operation != null) {
            UserWithPassword admin = SystemStorage.getOnlineUser(id);
            if (admin == null) return RequestResults.needLogin();
            else if (!admin.getPassword().equals(password)) return RequestResults.invalidAccountOrPassword();
            else if (admin.getType() != User.TYPE_ADMIN) return RequestResults.haveNoRight();

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

}
