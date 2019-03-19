package hit.go.forward.common.entity.comment;

import java.util.Date;

/**
 * Created by 班耀强 on 2018/11/1
 */
public class Comment {
    public static final int TYPE_LESSON = 0;
    public static final int TYPE_COURSE = 1;
    public static final int TYPE_COMMENT = 2;

    private Integer id;
    private Integer userId;
    private Integer courseId;
    private Integer type;
    private Integer commentId;
    private Integer lessonId;
    private String content;
    private Date date;
    private String note;
    private Integer state;
    private Integer under;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * @return Integer return the under
     */
    public Integer getUnder() {
        return under;
    }

    /**
     * @param under the under to set
     */
    public void setUnder(Integer under) {
        this.under = under;
    }

}
