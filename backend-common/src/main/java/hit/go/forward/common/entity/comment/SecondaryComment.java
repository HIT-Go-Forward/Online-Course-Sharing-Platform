package hit.go.forward.common.entity.comment;

import java.util.Date;

import org.bson.types.ObjectId;

public class SecondaryComment {
    private String id;
    private ObjectId objectId;
    private String replyTo;
    private String content;
    private String userId;
    private String userName;
    private String userAvatar;
    private Date commentDate;
    private String under;

    /**
     * @return String return the replyTo
     */
    public String getReplyTo() {
        return replyTo;
    }

    /**
     * @param replyTo the replyTo to set
     */
    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    /**
     * @return String return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return String return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return String return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return String return the userAvatar
     */
    public String getUserAvatar() {
        return userAvatar;
    }

    /**
     * @param userAvatar the userAvatar to set
     */
    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    /**
     * @return Date return the commentDate
     */
    public Date getCommentDate() {
        return commentDate;
    }

    /**
     * @param commentDate the commentDate to set
     */
    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }


    /**
     * @return String return the under
     */
    public String getUnder() {
        return under;
    }

    /**
     * @param under the under to set
     */
    public void setUnder(String under) {
        this.under = under;
    }


    /**
     * @return String return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return ObjectId return the objectId
     */
    public ObjectId getObjectId() {
        return objectId;
    }

    /**
     * @param objectId the objectId to set
     */
    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }

}