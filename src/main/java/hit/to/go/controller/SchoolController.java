package hit.to.go.controller;

import hit.to.go.database.dao.SchoolMapper;
import hit.to.go.database.mybatis.MybatisProxy;
import hit.to.go.platform.protocol.RequestResults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 班耀强 on 2018/10/13
 */
@Controller
@ResponseBody
@RequestMapping("/school")
public class SchoolController {

    @RequestMapping("/querySchool")
    public String querySchool(String keyword) {
        SchoolMapper mapper = MybatisProxy.create(SchoolMapper.class);
        return RequestResults.success(mapper.querySchool(keyword));
    }
}
