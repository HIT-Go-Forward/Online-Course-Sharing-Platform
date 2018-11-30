package hit.go.forward.controller.develop;

import hit.go.forward.database.dao.ActionMapper;
import hit.go.forward.entity.user.UserWithPassword;
import hit.go.forward.platform.AttrKey;
import hit.go.forward.platform.exception.InvalidParametersException;
import hit.to.go.platform.protocol.RequestResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/10/2
 */
@Controller
@ResponseBody
@RequestMapping("/develop")
public class OnlyForDevelopController {
    private static final Logger logger = LoggerFactory.getLogger(OnlyForDevelopController.class);

    private ActionMapper actionMapper;

    public OnlyForDevelopController(ActionMapper actionMapper) {
        this.actionMapper = actionMapper;
    }

    @RequestMapping("/testCookie")
    public String testCoolie(String id, String password) {
        Map<String, String> paras = new HashMap<>();
        paras.put("id", id);
        paras.put("password", password);
        return RequestResults.success(paras);
    }

    @RequestMapping("/getAllActions")
    public String getAllActions() {
        return RequestResults.success(actionMapper.getAllActions());
    }

    @RequestMapping("/testSession")
    public String testSession(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user) {
        return RequestResults.success(user);
    }

    @RequestMapping("/testException")
    public String testException() {
        throw new InvalidParametersException();
    }
}
