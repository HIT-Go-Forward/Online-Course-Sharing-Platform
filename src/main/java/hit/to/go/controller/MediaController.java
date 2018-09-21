package hit.to.go.controller;

import hit.to.go.platform.protocol.RequestResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 班耀强 on 2018/9/20
 */

@Controller
@ResponseBody
@RequestMapping("/media")
public class MediaController {
    private static final Logger logger = LoggerFactory.getLogger(MediaController.class);

    @RequestMapping("/{id}")
    public String getMedia(@PathVariable Integer id) {
        logger.debug("请求资源 {}", id);
        return RequestResults.success("success");
    }
}
