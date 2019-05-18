package hit.go.forward.common.entity.comment;

import java.util.List;

public class PrimaryComment {
    public static final String TYPE_COURSE = "course";
    public static final String TYPE_LESSON = "lesson";
    public static final String TYPE_BLOG = "blog";

    private String type;
    private String replyTo;
    private Boolean secondary;
    private String content;
    private String userId;
    private Comment[] replies;
    
}