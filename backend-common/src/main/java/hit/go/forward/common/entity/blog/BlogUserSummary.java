package hit.go.forward.common.entity.blog;

import org.bson.Document;

public class BlogUserSummary {
    private String userId;
    private Integer visitCount;
    private Integer commentCount;
    private Integer likeCount;
    private Integer blogCount;

    public Document toDocument() {
        Document doc = new Document();
        doc.put("userId", userId);
        doc.put("visitCount", visitCount);
        doc.put("commentCount", commentCount);
        doc.put("likeCount", likeCount);
        doc.put("blogCount", blogCount);
        return doc;
    }

    public void fromDocument(Document document) {
        userId = (String) document.get("userId");
        visitCount = (Integer) document.get("visitCount");
        commentCount = (Integer) document.get("commentCount");
        likeCount = (Integer) document.get("likeCount");
        blogCount = (Integer) document.get("blogCount");
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
     * @return Integer return the commentCount
     */
    public Integer getCommentCount() {
        return commentCount;
    }

    /**
     * @param commentCount the commentCount to set
     */
    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
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
     * @return Integer return the blogCount
     */
    public Integer getBlogCount() {
        return blogCount;
    }

    /**
     * @param blogCount the blogCount to set
     */
    public void setBlogCount(Integer blogCount) {
        this.blogCount = blogCount;
    }

}