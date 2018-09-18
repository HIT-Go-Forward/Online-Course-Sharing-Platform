package hit.to.go.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 班耀强 on 2018/9/18
 */

@Controller
@RequestMapping("/test")
public class TestController {

    @ResponseBody
    @RequestMapping("/test")
    public String testMethod() {
        return "{\"msg\": \"It works\"}";
    }
}
