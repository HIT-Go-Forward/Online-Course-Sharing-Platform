package hit.go.forward.common.entity.mongo;

import java.util.Date;

import org.bson.Document;

import hit.go.forward.common.entity.user.User;

public class BlogDocument extends Document {
    private Integer id;
    private String title;
    private User user;
    private String blog;
    private Integer status;
    private Integer type;
    private Date uploadDate;
    private Date updateDate;
    private String userId;
    private String label;
    private Integer visitCount;
    private Integer likeCount;
    private Integer dislikeCount;
    private String operation;

    /**
     * @return Integer return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
        append("id", id);
    }

    /**
     * @return String return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
        append("title", title);
    }

    /**
     * @return User return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
        append("user", user);
    }

    /**
     * @return String return the blog
     */
    public String getBlog() {
        return blog;
    }

    /**
     * @param blog the blog to set
     */
    public void setBlog(String blog) {
        this.blog = blog;
        append("blog", blog);
    }

    /**
     * @return Integer return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
        append("status", status);
    }

    /**
     * @return Integer return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
        append("type", type);
    }

    /**
     * @return Date return the uploadDate
     */
    public Date getUploadDate() {
        return uploadDate;
    }

    /**
     * @param uploadDate the uploadDate to set
     */
    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
        append("uploadDate", uploadDate);
    }

    /**
     * @return Date return the updateDate
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
        append("updateDate", updateDate);
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
        append("userId", userId);
    }

    /**
     * @return String return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
        append("label", label);
    }

    /**
     * @return Integer return the visitCount
     */
    public Integer getVisitCount() {
        return visitCount;
    }

    /**
     * @param visitCount the visitCount to set
     */
    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
        append("visitCount", visitCount);
    }

    /**
     * @return Integer return the likeCount
     */
    public Integer getLikeCount() {
        return likeCount;
    }

    /**
     * @param likeCount the likeCount to set
     */
    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
        append("likeCount", likeCount);
    }

    /**
     * @return Integer return the dislikeCount
     */
    public Integer getDislikeCount() {
        return dislikeCount;
    }

    /**
     * @param dislikeCount the dislikeCount to set
     */
    public void setDislikeCount(Integer dislikeCount) {
        this.dislikeCount = dislikeCount;
        append("dislikeCount", dislikeCount);
    }

    /**
     * @return String return the operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * @param operation the operation to set
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

}