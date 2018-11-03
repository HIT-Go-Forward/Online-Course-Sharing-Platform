package hit.to.go.platform.util.Comment;

import hit.to.go.entity.comment.Comment;

/**
 * Created by 班耀强 on 2018/11/1
 */
public class CommentUtil {
    public static boolean isInsertValid(Comment comment) {
        Integer type = comment.getType();
        if (type != null && comment.getContent() != null) {
            switch (type) {
                case Comment.TYPE_LESSON:
                    return comment.getCourseId() != null && comment.getLessonId() != null;
                case Comment.TYPE_COURSE:
                    return comment.getCourseId() != null;
                case Comment.TYPE_COMMENT:
                    return comment.getCourseId() != null && comment.getCommentId() != null;
                    default:
                        return false;
            }
        }
        return false;
    }
}
