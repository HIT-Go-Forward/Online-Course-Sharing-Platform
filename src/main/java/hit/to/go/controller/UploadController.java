package hit.to.go.controller;

import hit.to.go.database.dao.FileMapper;
import hit.to.go.database.mybatis.MybatisProxy;
import hit.to.go.entity.resource.Resource;
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
import java.util.Date;

/**
 * Created by 班耀强 on 2018/10/13
 */
@Controller
@ResponseBody
@RequestMapping("/resource")
public class UploadController {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    private static String TEMP_FILE_DIR = null;

    private static final String RESOURCE_NAME = "%%%resource_file%%%";
    private static final String MEDIA_SERVER_RESOURCE_DIR = "resource";

    private ServletContext context;

    public UploadController(ServletContext context) {
        this.context = context;
    }

    @RequestMapping("upload")
    public String upload(MultipartFile file, String type, String courseId, String lessonId, @SessionAttribute(AttrKey.ATTR_USER) User user) {
        if (type == null || file == null) return RequestResults.wrongParameters();
        if (TEMP_FILE_DIR == null) TEMP_FILE_DIR = context.getRealPath("/temp");
        String storePath;
        Integer fileType;
        switch (type) {
            case "userImg":
                storePath =  buildPath(MEDIA_SERVER_RESOURCE_DIR, user.getId().toString(), file.getOriginalFilename());
                fileType = Resource.TYPE_IMG;
                break;
            case "courseImg":
                if (!user.getType().equals(User.TYPE_TEACHER)) return RequestResults.haveNoRight();
                if (courseId == null) return RequestResults.wrongParameters("courseId");
                storePath = buildPath(MEDIA_SERVER_RESOURCE_DIR, user.getId().toString(), courseId, file.getOriginalFilename());
                fileType = Resource.TYPE_IMG;

                break;
            case "courseVideo":
                if (!user.getType().equals(User.TYPE_TEACHER)) return RequestResults.haveNoRight();
                if (courseId == null || lessonId == null) return RequestResults.wrongParameters();
                storePath = buildPath(MEDIA_SERVER_RESOURCE_DIR, user.getId().toString(), courseId, lessonId, file.getOriginalFilename());
                fileType = Resource.TYPE_VIDEO;
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

            FileMapper mapper = MybatisProxy.create(FileMapper.class);
            Integer rows = null;

            if (tmpFile.delete()) logger.debug("文件 {} 上传成功， 清除本地缓存", storePath);
            else logger.debug("文件 {} 本地缓存清除失败", storePath);

            if (result.getData().equals("success")) rows = mapper.addNewFile(resource);
            else if (result.getData().equals("override")) rows = mapper.updateFile(resource);
            if (rows != null && rows.equals(1)) return RequestResults.success(resource);
            return RequestResults.dataBaseWriteError();
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
