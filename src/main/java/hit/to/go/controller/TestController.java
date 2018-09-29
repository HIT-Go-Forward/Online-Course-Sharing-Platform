package hit.to.go.controller;

import hit.to.go.database.dao.UserMapper;
import hit.to.go.database.mybatis.MybatisProxy;
import hit.to.go.entity.user.User;
import hit.to.go.platform.protocol.RequestResults;
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

    @RequestMapping("/queryUser")
    public String testMethod(Integer id) {
        if (id != null) {
            UserMapper mapper = MybatisProxy.create(UserMapper.class);
            User user = mapper.queryById(id);
            if (user != null) return RequestResults.success(user);
        }
        return RequestResults.notFound(null);
    }

}
