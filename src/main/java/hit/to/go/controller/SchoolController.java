package hit.to.go.controller;

import hit.to.go.database.dao.SchoolMapper;
import hit.to.go.database.mybatis.MybatisProxy;
import hit.to.go.platform.protocol.RequestResults;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 班耀强 on 2018/10/13
 */
@Controller
@ResponseBody
@Transactional
@RequestMapping("/school")
public class SchoolController {
    private SchoolMapper schoolMapper;

    public SchoolController(SchoolMapper schoolMapper) {
        this.schoolMapper = schoolMapper;
    }

    @RequestMapping("/querySchool")
    public String querySchool(String keyword) {
        if (keyword == null) return RequestResults.success(null);
        return RequestResults.success(schoolMapper.querySchool(keyword));
    }
}
