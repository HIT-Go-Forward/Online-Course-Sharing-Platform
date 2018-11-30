package hit.go.forward.controller;

import hit.go.forward.protocol.RequestResult;
import hit.go.forward.protocol.RequestResults;
import hit.go.forward.protocol.RequestWrapper;
import hit.go.forward.database.dao.FileMapper;
import hit.go.forward.common.entity.resource.Resource;
import hit.go.forward.common.entity.user.User;
import hit.go.forward.common.entity.user.UserWithPassword;
import hit.go.forward.platform.AttrKey;
import hit.go.forward.platform.exception.RequestHandleException;

import hit.go.forward.platform.util.ResourceTransmissionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;

/**
 * Created by 班耀强 on 2018/10/13
 */
@Controller
@ResponseBody
@Transactional
@RequestMapping("/resource")
public class UploadController {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

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

    public UploadController(ServletContext context, FileMapper fileMapper) {
        this.context = context;
        this.fileMapper = fileMapper;
    }

    @Transactional
    @RequestMapping("upload")
    public String upload(MultipartFile file, String type, String courseId, String lessonId, @SessionAttribute(AttrKey.ATTR_USER) UserWithPassword user, HttpServletRequest request, HttpServletResponse response) {
        if (type == null || file == null) return RequestResults.wrongParameters();
        if (TEMP_FILE_DIR == null) TEMP_FILE_DIR = context.getRealPath("/temp");
        String storePath, dispatchPath;
        Integer fileType;
        switch (type) {
            case TYPE_USER_IMG:
                storePath =  buildPath(MEDIA_SERVER_RESOURCE_DIR, user.getId().toString(), file.getOriginalFilename());
                fileType = Resource.TYPE_IMG;
                dispatchPath = "/authority/updateUserImg.action";
                break;
            case TYPE_COURSE_IMG:
                if (user.getType() > User.TYPE_TEACHER) return RequestResults.haveNoRight();
                if (courseId == null) return RequestResults.wrongParameters("courseId");
                storePath = buildPath(MEDIA_SERVER_RESOURCE_DIR, user.getId().toString(), courseId, file.getOriginalFilename());
                fileType = Resource.TYPE_IMG;
                dispatchPath = "/course/updateCourseImg.action";
                break;
            case TYPE_LESSON_VIDEO:
                if (user.getType() > User.TYPE_TEACHER) return RequestResults.haveNoRight();
                if (courseId == null || lessonId == null) return RequestResults.wrongParameters();
                storePath = buildPath(MEDIA_SERVER_RESOURCE_DIR, user.getId().toString(), courseId, lessonId, file.getOriginalFilename());
                fileType = Resource.TYPE_VIDEO;
                dispatchPath = "/course/updateLessonVideo.action";
                break;
            case TYPE_LESSON_FILE:
                if (user.getType() > User.TYPE_TEACHER) return RequestResults.haveNoRight();
                if (courseId == null || lessonId == null) return RequestResults.wrongParameters();
                storePath = buildPath(MEDIA_SERVER_RESOURCE_DIR, user.getId().toString(), courseId, lessonId, COURSE_FILE_DIR, file.getOriginalFilename());
                fileType = Resource.TYPE_OTHER;
                dispatchPath = "/course/updateLessonFile.action";
                break;
                default:
                    return RequestResults.wrongParameters("type");
        }
        storePath = storePath.replace(file.getOriginalFilename(), RESOURCE_NAME);
        File tmpFile = new File(TEMP_FILE_DIR, storePath);
        if (tmpFile.exists()) tmpFile.delete();
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
            Resource resource = new Resource();
            resource.setDate(new Date());
            resource.setName(file.getOriginalFilename());
            resource.setType(fileType);
            resource.setUrl(storePath);
            resource.setUserId(user.getId());

            Integer rows = null;

            if (tmpFile.delete()) logger.debug("文件 {} 上传成功， 清除本地缓存", storePath);
            else logger.debug("文件 {} 本地缓存清除失败", storePath);

            if (result.getData().equals("success")) rows = fileMapper.addNewFile(resource);
            else if (result.getData().equals("override")) {
                rows = fileMapper.updateFile(resource);
                if (rows == null || !rows.equals(1)) rows = fileMapper.addNewFile(resource);
            }
            if (rows != null && rows.equals(1)) {
                String fileId = fileMapper.selectFileIdByUrl(storePath);
                RequestWrapper requestWrapper = new RequestWrapper(request);
                requestWrapper.addParameter("fileId", fileId);
                requestWrapper.addParameter("courseId", courseId);
                requestWrapper.addParameter("lessonId", lessonId);
                try {
                    logger.debug("转发请求到{}", dispatchPath);
                    request.getRequestDispatcher(dispatchPath).forward(requestWrapper, response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            // TODO delete media server file
            throw new RequestHandleException(RequestResults.dataBaseWriteError());
        }
        return RequestResults.error(result.getData());
    }

    @RequestMapping("/delete")
    public String delete() {

        return RequestResults.error();
    }

    private String buildPath(String ...paths) {
        return "/" + String.join("/", paths);
    }
}
