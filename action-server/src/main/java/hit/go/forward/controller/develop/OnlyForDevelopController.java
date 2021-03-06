package hit.go.forward.controller.develop;

import hit.go.forward.common.protocol.RequestResult;
import hit.go.forward.common.protocol.RequestResults;
import hit.go.forward.business.database.dao.ActionMapper;
import hit.go.forward.common.config.PlatformConfig;
import hit.go.forward.common.entity.user.UserWithPassword;
import hit.go.forward.platform.AttrKey;
import hit.go.forward.platform.SystemConfig;

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

    private ActionMapper actionMapper;

    public OnlyForDevelopController(ActionMapper actionMapper) {
        this.actionMapper = actionMapper;
    }

    @RequestMapping("/testCookie")
    public RequestResult testCoolie(String id, String password) {
        Map<String, String> paras = new HashMap<>();
        paras.put("id", id);
        paras.put("password", password);
        return RequestResults.success(paras);
    }

    @RequestMapping("/getAllActions")
    public RequestResult getAllActions() {
        return RequestResults.success(actionMapper.getAllActions());
    }

    @RequestMapping("/testSession")
    public RequestResult testSession(@SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user) {
        return RequestResults.success(user);
    }

    @RequestMapping("/testConfig")
    public RequestResult testConfig() {
        return RequestResults.success(PlatformConfig.testRead());
    }

    @RequestMapping("/reconfigSystem")
    public RequestResult reconfigSystem() {
        SystemConfig.config();
        return RequestResults.success();
    }

    @RequestMapping("/systemVersion")
    public RequestResult systemVersion() {
        return RequestResults.success(SystemConfig.getActionServerConfig().getVersion());
    }
}
