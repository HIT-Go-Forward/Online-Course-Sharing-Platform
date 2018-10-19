package hit.to.go.controller;

import hit.to.go.entity.user.User;
import hit.to.go.platform.AttrKey;
import hit.to.go.platform.protocol.RequestResult;
import hit.to.go.platform.protocol.RequestResults;
import hit.to.go.platform.util.ResourceTransmissionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;

/**
 * Created by 班耀强 on 2018/10/13
 */
@Controller
@ResponseBody
@RequestMapping("/upload")
public class UploadController {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    private static String TEMP_FILE_DIR = null;

    @RequestMapping("/uploadUserImg")
    public String uploadUserImg(MultipartFile file, @SessionAttribute(AttrKey.ATTR_USER) User user, HttpSession session) {
        ServletContext context = session.getServletContext();
        if (file == null) return RequestResults.wrongParameters();
        if (TEMP_FILE_DIR == null) TEMP_FILE_DIR = context.getRealPath("/temp");

        String storePath = buildPath(user.getId().toString(), file.getOriginalFilename().replaceAll("^\\S+(\\.\\w+)$", "head$1"));
        File tmpFile = new File(TEMP_FILE_DIR, storePath);
        if (tmpFile.exists()) return RequestResults.error("文件已存在");
        tmpFile.mkdirs();
        try {
            file.transferTo(tmpFile);
        } catch (Exception e) {
            e.printStackTrace();
            return RequestResults.error();
        }
        RequestResult result = ResourceTransmissionUtil.uploadToMediaServer(tmpFile, storePath);
        if (result == null) return RequestResults.error("空的响应");
        else if (result.getStatus() == 200) {
            if (tmpFile.delete()) {
                logger.debug("文件 {} 上传成功， 清除本地缓存", storePath);
                return RequestResults.success();
            } else return RequestResults.error("本地缓存清除失败！");
        }
        return RequestResults.error(result.getData());
    }

    @RequestMapping("/uploadCourseImg")
    public String uploadPic(MultipartFile file, String courseId, @SessionAttribute(AttrKey.ATTR_USER) User user, HttpSession session) {
        ServletContext context = session.getServletContext();
        if (file == null || courseId == null) return RequestResults.wrongParameters();
        if (TEMP_FILE_DIR == null) TEMP_FILE_DIR = context.getRealPath("/temp");

        String storePath = buildPath(user.getId().toString(), courseId, file.getOriginalFilename());
        File tmpFile = new File(TEMP_FILE_DIR, storePath);
        if (tmpFile.exists()) return RequestResults.error("文件已存在");
        tmpFile.mkdirs();
        try {
            file.transferTo(tmpFile);
        } catch (Exception e) {
            e.printStackTrace();
            return RequestResults.error();
        }
        RequestResult result = ResourceTransmissionUtil.uploadToMediaServer(tmpFile, storePath);
        if (result == null) return RequestResults.error("空的响应");
        else if (result.getStatus() == 200) {
            if (tmpFile.delete()) {
                logger.debug("文件 {} 上传成功， 清除本地缓存", storePath);
                return RequestResults.success();
            } else return RequestResults.error("本地缓存清除失败！");
        }
        return RequestResults.error(result.getData());
    }

    @RequestMapping("/uploadVideo")
    public String uploadVideo(MultipartFile file, String courseId, String lessonId, @SessionAttribute(AttrKey.ATTR_USER) User user, HttpSession session) {
        ServletContext context = session.getServletContext();
        if (file == null || courseId == null || lessonId == null) return RequestResults.wrongParameters();
        if (TEMP_FILE_DIR == null) TEMP_FILE_DIR = context.getRealPath("/temp");

        String storePath = buildPath(user.getId().toString(), courseId, lessonId, file.getOriginalFilename());
        File tmpFile = new File(TEMP_FILE_DIR, storePath);
        if (tmpFile.exists()) return RequestResults.error("文件已存在");
        tmpFile.mkdirs();
        try {
            file.transferTo(tmpFile);
        } catch (Exception e) {
            e.printStackTrace();
            return RequestResults.error();
        }
        RequestResult result = ResourceTransmissionUtil.uploadToMediaServer(tmpFile, storePath);
        if (result == null) return RequestResults.error("空的响应");
        else if (result.getStatus() == 200) {
            if (tmpFile.delete()) {
                logger.debug("文件 {} 上传成功， 清除本地缓存", storePath);
                return RequestResults.success();
            } else return RequestResults.error("本地缓存清除失败！");
        }
        return RequestResults.error(result.getData());
    }

    private String buildPath(String ...paths) {
        return "/" + String.join("/", paths);
    }
}
