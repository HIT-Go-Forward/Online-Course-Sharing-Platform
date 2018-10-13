package hit.to.go.controller.develop;

import hit.to.go.database.dao.ActionMapper;
import hit.to.go.database.mybatis.MybatisProxy;
import hit.to.go.entity.user.UserWithPassword;
import hit.to.go.platform.AttrKey;
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

    @RequestMapping("/testCookie")
    public String testCoolie(String id, String password) {
        Map<String, String> paras = new HashMap<>();
        paras.put("id", id);
        paras.put("password", password);
        return RequestResults.success(paras);
    }

    @RequestMapping("/getAllActions")
    public String getAllActions() {
        ActionMapper actionMapper = MybatisProxy.create(ActionMapper.class);
        return RequestResults.success(actionMapper.getAllActions());
    }

    @RequestMapping("/testSession")
    public String testSession(@SessionAttribute(AttrKey.ATTR_USER)UserWithPassword user) {
        return RequestResults.success(user);
    }
}