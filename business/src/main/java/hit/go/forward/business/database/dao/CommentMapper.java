package hit.go.forward.business.database.dao;

import hit.go.forward.common.entity.comment.Comment;
import hit.go.forward.common.entity.comment.CommentEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/11/1
 */
public interface CommentMapper {
    Integer insertComment(Comment comment);

    Integer deleteComment(Comment comment);

    List<CommentEntity> selectCommentByCourseId(Map<String, Object> paras);

    List<CommentEntity> selectCommentByLessonId(Map<String, Object> paras);

    List<CommentEntity> selectCommentByUserId(Map<String, Object> paras);

    List<CommentEntity> selectCommentByBlogId(Map<String, Object> paras);
}
