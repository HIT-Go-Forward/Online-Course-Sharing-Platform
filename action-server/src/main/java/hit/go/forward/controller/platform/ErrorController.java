package hit.go.forward.controller.platform;

import hit.go.forward.common.protocol.RequestResult;
import hit.go.forward.common.protocol.RequestResults;
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
    public RequestResult haveNoRight() {
        return RequestResults.accessDenied();
    }

    @RequestMapping("/needLogin")
    public RequestResult needLogin() {
        return RequestResults.accessDenied();
    }
}
