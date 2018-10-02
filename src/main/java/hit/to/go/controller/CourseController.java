package hit.to.go.controller;

import hit.to.go.entity.user.UserWithPassword;
import hit.to.go.platform.SystemStorage;
import hit.to.go.platform.protocol.RequestResult;
import hit.to.go.platform.protocol.RequestResults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
        String id, password, courseName, courseImg;
        id = paras.get("id");
        password = paras.get("password");
        courseImg = paras.get("courseImg");
        courseName = paras.get("courseName");
        if (id != null && password != null && courseName != null && courseImg != null) {
            UserWithPassword user = SystemStorage.getOnlineUser(id);
            if (user == null) return RequestResults.needLogin();
            else if (!user.getPassword().equals(password)) return RequestResults.invalidAccountOrPassword();

        }


        return RequestResults.wrongParameters();
    }

    @RequestMapping("/getCourses")
    public String getCourses(String id, String password, String type) {



        return RequestResults.wrongParameters();
    }

}
