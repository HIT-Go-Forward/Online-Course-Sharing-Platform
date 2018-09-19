package hit.to.go.controller;

import com.google.gson.Gson;
import hit.to.go.database.dao.UserMapper;
import hit.to.go.database.mybatis.MybatisProxy;
import hit.to.go.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 班耀强 on 2018/9/18
 */

@Controller
@ResponseBody
@RequestMapping("/test")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @RequestMapping("/test")
    public String testMethod(String name) {
        logger.debug(name);
        UserMapper mapper = MybatisProxy.create(UserMapper.class);
        User user = mapper.selectUser(1);
        return new Gson().toJson(user);
    }

    @RequestMapping(value = "/test2", produces = "application/json; charset=utf-8")
    public String testMethod2() {
        UserMapper mapper = MybatisProxy.create(UserMapper.class);
        User user = mapper.selectUser(1);
        return new Gson().toJson(user);
    }
}
