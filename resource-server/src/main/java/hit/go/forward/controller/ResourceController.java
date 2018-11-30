package hit.go.forward.controller;

import hit.go.forward.protocol.RequestResults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;

/**
 * Created by 班耀强 on 2018/10/18
 */
@Controller
@ResponseBody
@RequestMapping("/resource")
public class ResourceController {
    private ServletContext context;

    public ResourceController(ServletContext context) {
        this.context = context;
    }

    @RequestMapping("/upload")
    public String upload(MultipartFile file, String storePath) {
        storePath = context.getRealPath(storePath);
        File storeFile = new File(storePath);

        String result = "success";
        if (storeFile.exists()) {
            result = "override";
            storeFile.delete();
        }
        storeFile.mkdirs();
        try {
            file.transferTo(storeFile);
            return RequestResults.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return RequestResults.error("存储文件到本地发生异常" + e.getMessage());
        }
    }

    @RequestMapping("/delete")
    public String delete(String storePath) {
        storePath = context.getRealPath(storePath);
        File file = new File(storePath);
        if (file.exists() && file.delete()) return RequestResults.success();
        return RequestResults.error("文件不存在或已被删除");
    }
}
