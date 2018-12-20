package hit.go.forward.rserver.controller;

import hit.go.forward.business.database.dao.CourseMapper;
import hit.go.forward.business.database.dao.FileMapper;
import hit.go.forward.business.database.dao.LessonMapper;
import hit.go.forward.business.database.dao.UserMapper;
import hit.go.forward.common.entity.resource.Resource;
import hit.go.forward.common.exception.DatabaseWriteException;
import hit.go.forward.common.exception.RequestHandleException;
import hit.go.forward.common.protocol.RequestResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@ResponseBody
@RequestMapping("/resource")
public class ResourceUploadController {
    private static final Logger logger = LoggerFactory.getLogger(ResourceUploadController.class);

    private static final String TYPE_USER_IMG = "userImg";
    private static final String TYPE_COURSE_IMG = "courseImg";
    private static final String TYPE_LESSON_VIDEO = "lessonVideo";
    private static final String TYPE_LESSON_FILE = "lessonFile";

    private static final String MEDIA_SERVER_RESOURCE_DIR = "resource"; // 资源服务器资源根目录
    private static final String COURSE_FILE_DIR = "other";  // 课程附件文件夹
    private static final String RESOURCE_FILE_NAME = "$$$resource_file$$$";

    private ServletContext context;
    private FileMapper fileMapper;
    private UserMapper userMapper;
    private CourseMapper courseMapper;
    private LessonMapper lessonMapper;

    public ResourceUploadController(ServletContext context, FileMapper fileMapper, UserMapper userMapper, CourseMapper courseMapper, LessonMapper lessonMapper) {
        this.context = context;
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
        this.courseMapper = courseMapper;
        this.lessonMapper = lessonMapper;
    }

    @Transactional
    @RequestMapping("/uploadResource")
    public String uploadResource(MultipartFile file, String $userId, String courseId, String lessonId, String type) {
        if (file == null || type == null)  return RequestResults.wrongParameters("file || type");
        if ($userId== null) {
            logger.error("未能获取到userId参数");
            return RequestResults.error();
        }
        String fileName  = file.getOriginalFilename();
        String parent = context.getRealPath("/");
        String storePath, storeName;
        Integer fileType;
        switch (type) {
            case TYPE_USER_IMG:
                storePath = buildPath(MEDIA_SERVER_RESOURCE_DIR, $userId);
                fileType = Resource.TYPE_IMG;
                break;
            case TYPE_COURSE_IMG:
                if (courseId ==  null) return RequestResults.wrongParameters("courseId");
                storePath = buildPath(MEDIA_SERVER_RESOURCE_DIR, $userId, courseId);
                fileType = Resource.TYPE_IMG;
                break;
            case TYPE_LESSON_VIDEO:
                if (courseId ==  null || lessonId == null) return RequestResults.wrongParameters("courseId  || lessonId");
                storePath = buildPath(MEDIA_SERVER_RESOURCE_DIR, $userId, courseId, lessonId);
                fileType = Resource.TYPE_VIDEO;
                break;
            case TYPE_LESSON_FILE:
                if (courseId ==  null || lessonId == null) return RequestResults.wrongParameters("courseId  || lessonId");
                storePath = buildPath(MEDIA_SERVER_RESOURCE_DIR, $userId, courseId, lessonId, COURSE_FILE_DIR);
                fileType = Resource.TYPE_OTHER;
                break;
                default:
                return RequestResults.wrongParameters("type");
        }
        logger.debug("构建文件存储路径 {}", storePath);
        storeName = storePath + "/" + RESOURCE_FILE_NAME;
        File fileDir = new File(parent, storePath);
        fileDir.mkdirs();
        File[] files = fileDir.listFiles();

        boolean delPrev = false;
        if (files != null) {
            for (File f : files) {
                if (f.isFile()) {
                    delPrev = true;
                    f.delete();
                }
            }
        }

        File storeFile = new File(fileDir, fileName);
        try {
            file.transferTo(storeFile);
        } catch (Exception e) {
            throw new RequestHandleException("本地文件写入失败!");
        }

        Resource resource = new Resource();
        resource.setDate(new Date());
        resource.setName(storeName);
        resource.setType(fileType);
        resource.setUrl(storePath + "/" + file.getOriginalFilename());
        resource.setUserId(Integer.valueOf($userId));

        Integer rows;
        if (delPrev) rows = fileMapper.updateFile(resource);
        else rows = fileMapper.addNewFile(resource);
        if (rows == null || rows.equals(0)) throw new DatabaseWriteException();

        if (!delPrev) { // 如果原文件不存在，则为新创建的文件，需要课程等引用的修改文件id
            Map<String, Object> paras = new HashMap<>();
            paras.put("img", resource.getId());
            paras.put("teacherId", $userId);
            paras.put("id", $userId);
            paras.put("fileId", resource.getId());
            paras.put("lessonId", lessonId);
            paras.put("courseId", courseId);
            rows = 0;
            switch (type) {
                case TYPE_USER_IMG:
                    rows = userMapper.updateUserImg(paras);
                    break;
                case TYPE_COURSE_IMG:
                    rows = courseMapper.updateCourseImg(paras);
                    break;
                case TYPE_LESSON_VIDEO:
                    rows = lessonMapper.updateLessonVideo(paras);
                    break;
                case TYPE_LESSON_FILE:
                    rows = lessonMapper.updateLessonFile(paras);
            }
            if (rows.equals(1)) return RequestResults.success();
            throw new DatabaseWriteException();
        }
        return RequestResults.success(resource.getUrl());
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
