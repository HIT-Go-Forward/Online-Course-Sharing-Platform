package hit.go.forward.controller;

import hit.go.forward.business.database.dao.CommentMapper;
import hit.go.forward.common.entity.comment.Comment;
import hit.go.forward.common.exception.DatabaseWriteException;
import hit.go.forward.platform.util.Comment.CommentUtil;
import hit.go.forward.common.protocol.RequestResult;
import hit.go.forward.common.protocol.RequestResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public RequestResult getComment(String type, String courseId, String lessonId, Integer start, Integer length) {
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
                paras.put("lessonId", courseId);
                paras.put("start", start);
                paras.put("length", length);
                return RequestResults.success(commentMapper.selectCommentByCourseId(paras));
                default:
                    return RequestResults.invalidParamValue("type");
        }
    }
}
