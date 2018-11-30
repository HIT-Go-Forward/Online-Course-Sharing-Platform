package hit.go.forward.controller;

import hit.go.forward.protocol.RequestResults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/")
public class UploadController {

    @RequestMapping("/upload")
    public String upload() {
        return RequestResults.success("This is just a simple test..");
    }
}
