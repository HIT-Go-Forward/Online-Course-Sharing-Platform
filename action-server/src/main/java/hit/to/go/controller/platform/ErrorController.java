package hit.to.go.controller.platform;

import hit.to.go.platform.protocol.RequestResults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 班耀强 on 2018/10/5
 */
@Controller
@ResponseBody
@RequestMapping("/error")
public class ErrorController {
    @RequestMapping("/haveNoRight")
    public String haveNoRight() {
        return RequestResults.haveNoRight();
    }

    @RequestMapping("/needLogin")
    public String needLogin() {
        return RequestResults.needLogin();
    }
}
