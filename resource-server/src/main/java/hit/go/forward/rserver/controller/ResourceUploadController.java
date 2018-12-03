package hit.go.forward.rserver.controller;

import hit.go.forward.business.database.dao.FileMapper;
import hit.go.forward.common.protocol.RequestResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;

@Controller
@ResponseBody
@RequestMapping("/")
public class ResourceUploadController {
    private static final Logger logger = LoggerFactory.getLogger(ResourceUploadController.class);

    private static String TEMP_FILE_DIR = null;

    private static final String TYPE_USER_IMG = "userImg";
    private static final String TYPE_COURSE_IMG = "courseImg";
    private static final String TYPE_LESSON_VIDEO = "lessonVideo";
    private static final String TYPE_LESSON_FILE = "lessonFile";

    private static final String RESOURCE_NAME = "$$$resource_file$$$";  // 统一资源名称
    private static final String MEDIA_SERVER_RESOURCE_DIR = "resource"; // 资源服务器资源根目录
    private static final String COURSE_FILE_DIR = "other";  // 课程附件文件夹

    private ServletContext context;
    private FileMapper fileMapper;

    public ResourceUploadController(ServletContext context, FileMapper fileMapper) {
        this.context = context;
        this.fileMapper = fileMapper;
    }

    @RequestMapping("/uploadResource")
    public String uploadResource() {

        return RequestResults.success();
    }

    @RequestMapping("/test")
    public String test() {
        return RequestResults.success(fileMapper.queryUrlById("34"));
    }

    @RequestMapping("/delete")
    public String delete() {

        return RequestResults.error();
    }

    private String buildPath(String ...paths) {
        return "/" + String.join("/", paths);
    }
}
