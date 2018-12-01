package hit.go.forward.controller;

import hit.go.forward.database.dao.SchoolMapper;
import hit.go.forward.common.protocol.RequestResults;
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
