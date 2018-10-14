package hit.to.go.controller;

import hit.to.go.platform.protocol.RequestResults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;

/**
 * Created by 班耀强 on 2018/10/13
 */
@Controller
@ResponseBody
@RequestMapping("/upload")
public class UploadController {
    @RequestMapping("/uploadPic")
    public String uploadPic() {

        return RequestResults.success();
    }

    @RequestMapping("/uploadVideo")
    public String uploadVideo(MultipartFile uploadFile, HttpSession session) {
        String fileName = uploadFile.getOriginalFilename();
        String filePath = session.getServletContext().getRealPath("/videos");
        File file = new File(filePath, fileName);
        boolean mk = file.mkdirs();
        if (mk) {
            try {
                uploadFile.transferTo(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return RequestResults.success();
        }
        return RequestResults.error("路径创建失败");
    }
}
