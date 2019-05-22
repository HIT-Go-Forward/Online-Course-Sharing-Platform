package hit.go.forward.controller;

import hit.go.forward.business.database.dao.CommentMapper;
import hit.go.forward.business.database.mongo.MongoDB;
import hit.go.forward.common.entity.comment.Comment;
import hit.go.forward.common.entity.comment.PrimaryComment;
import hit.go.forward.common.entity.comment.SecondaryComment;
import hit.go.forward.common.entity.user.User;
import hit.go.forward.common.exception.DatabaseWriteException;
import hit.go.forward.platform.SystemStorage;
import hit.go.forward.platform.util.Comment.CommentUtil;
import hit.go.forward.common.protocol.RequestResult;
import hit.go.forward.common.protocol.RequestResults;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/11/1
 */
@Controller
@ResponseBody
@RequestMapping("/CommentAndBarrage")
public class CommentBarrageController {
    private static final Logger logger = LoggerFactory.getLogger(CommentBarrageController.class);

    private CommentMapper commentMapper;

    public CommentBarrageController(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Transactional
    @RequestMapping("/sendComment")
    public RequestResult sendComment(Comment comment, String $userId) {
        logger.debug(comment.getContent());
        logger.debug("CourseId: {}", comment.getCourseId());
        logger.debug("type: {}", comment.getType());
        if (!CommentUtil.isInsertValid(comment)) return RequestResults.lackNecessaryParam("缺少必须参数");
        comment.setUserId(Integer.valueOf($userId));
        Integer rows = commentMapper.insertComment(comment);
        if (rows != null && rows.equals(1)) return RequestResults.success();
        throw new DatabaseWriteException();
    }

    @RequestMapping("/getComment")
    public RequestResult getComment(String type, String courseId, String lessonId, String blogId, Integer start, Integer length) {
        Map<String, Object> paras;
        if (type == null) return RequestResults.lackNecessaryParam("type");
        switch (type) {
            case "course":
                if (courseId == null) return RequestResults.lackNecessaryParam("courseId");
                paras = new HashMap<>();
                paras.put("courseId", courseId);
                paras.put("start", start);
                paras.put("length", length);
                return RequestResults.success(commentMapper.selectCommentByCourseId(paras));
            case "lesson":
                if (lessonId == null) return RequestResults.lackNecessaryParam("lessonId");
                paras = new HashMap<>();
                paras.put("lessonId", lessonId);
                paras.put("start", start);
                paras.put("length", length);
                return RequestResults.success(commentMapper.selectCommentByLessonId(paras));
            case "blog":
                if (blogId == null) return RequestResults.lackNecessaryParam("blogId");
                paras = new HashMap<>();
                paras.put("blogId", blogId);
                paras.put("start", start);
                paras.put("length", length);
                return RequestResults.success(commentMapper.selectCommentByBlogId(paras));
                default:
                    return RequestResults.invalidParamValue("type");
        }
    }

    @RequestMapping("/comment")
    public RequestResult comment(CommentParam com, String $userId) {
        if (com.getGet() == null) {
            User user = SystemStorage.getUser($userId);
            if (CommentParam.isValid(com)) {
                if (com.isSecondary()) {
                    SecondaryComment comm = new SecondaryComment();
                    ObjectId objectId = new ObjectId();
                    comm.setCommentDate(new Date());
                    comm.setContent(com.getContent());
                    comm.setReplyTo(com.getReplyTo());
                    comm.setUserAvatar(user.getImg());
                    comm.setUserId($userId);
                    comm.setUserName(user.getName());
                    comm.setObjectId(objectId);
                    comm.setId(objectId.toHexString());
                    comm.setUnder(com.getUnderId());
                    if (MongoDB.insertComment(comm)) return RequestResults.success();
                    return RequestResults.serverError();
                } else {
                    PrimaryComment comm = new PrimaryComment();
                    comm.setCommentDate(new Date());
                    comm.setContent(com.getContent());
                    comm.setReplyTo(com.getReplyTo());
                    comm.setUserAvatar(user.getImg());
                    comm.setUserId($userId);
                    comm.setUserName(user.getName());
                    comm.setType(com.getType());
                    MongoDB.insertComment(comm);
                    return RequestResults.success();
                }
            }
        } else if (com.getType() != null && com.getReplyTo() != null) {
            return RequestResults.success(MongoDB.getComment(com.getType(), com.getReplyTo()));
        }
        return RequestResults.invalidParam();
    }
}

class CommentParam {
    private String type;
    private String replyTo;
    private String content;
    private Boolean secondary = false;
    private String underId;
    private String get;

    public static boolean isValid(CommentParam param) {
        if (param.getReplyTo() != null && param.getContent() != null) {
            if (param.isSecondary()) return param.getUnderId() != null;
            return param.getType() != null;
        }
        return false;
    }

    public String getGet() {
        return get;
    }

    public void setGet(String get) {
        this.get = get;
    }
    
    public Boolean isSecondary() {
        return secondary;
    }

    public void setSecondary(Boolean secondary) {
        this.secondary = secondary;
    }

    public String getUnderId() {
        return underId;
    }

    public void setUnderId(String underId) {
        this.underId = underId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
