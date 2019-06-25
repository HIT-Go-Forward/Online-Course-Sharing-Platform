package hit.go.forward.barrage.controller;

import hit.go.forward.common.protocol.RequestResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import hit.go.forward.business.database.mongo.MongoDB;

@RequestMapping("/sprv1")
@ResponseBody
@Controller
public class BarrageController extends MongoDB {
    @RequestMapping("getBarrages")
    public RequestResult getBarrages(String lessonId) {
        return null;
    }

    @RequestMapping("/sendComment")
    public RequestResult sendComment() {
        return null;
    }
}