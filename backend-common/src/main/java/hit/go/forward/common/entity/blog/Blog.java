package hit.go.forward.common.entity.blog;

import java.util.Date;

import hit.go.forward.common.entity.user.User;

public class Blog {
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_DRAFT = 1;
    public static final int STATUS_RELEASED = 2;
    public static final int STATUS_REJECTED = 3;

    private String id;
    private String title;
    private User user;
    private String blog;
    private Integer status;
    private Integer type;   // 
    private Date uploadDate;
    private Date updateDate;
    private String userId;
    private String label;
    private Integer visitCount;
    private Integer likeCount;
    private Integer dislikeCount;
    private String operation;
    private Boolean open;
    private String kind;

    /**
     * @return Integer return the id
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
    }

    /**
     * @return Date return the upoloadDate
     */
    public Date getUploadDate() {
        return uploadDate;
    }

    /**
     * @param upoloadDate the upoloadDate to set
     */
    public void setUploadDate(Date upoloadDate) {
        this.uploadDate = upoloadDate;
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


    /**
     * @return Boolean return the open
     */
    public Boolean isOpen() {
        return open;
    }

    /**
     * @param open the open to set
     */
    public void setOpen(Boolean open) {
        this.open = open;
    }

    /**
     * @return String return the kind
     */
    public String getKind() {
        return kind;
    }

    /**
     * @param kind the kind to set
     */
    public void setKind(String kind) {
        this.kind = kind;
    }

}