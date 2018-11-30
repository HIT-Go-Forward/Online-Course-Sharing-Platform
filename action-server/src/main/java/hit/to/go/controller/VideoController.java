package hit.to.go.controller;

import hit.go.forward.protocol.RequestResults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/10/5
 */
@Controller
@ResponseBody
@RequestMapping("/video")
public class VideoController {
    @RequestMapping("/course{id}")
    public String resolveVideo(@PathVariable Integer id, Integer p) {
        Map<String, Object> paras = new HashMap<>();
        paras.put("id", id);
        paras.put("p", p);
        return RequestResults.success(paras);
    }
}
