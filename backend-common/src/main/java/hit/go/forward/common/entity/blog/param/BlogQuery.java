package hit.go.forward.common.entity.blog.param;

public class BlogQuery {
    private Integer typeId;
    private Integer start;
    private Integer length;
    private Integer id;
    private String userId;

    /**
     * @return Integer return the typeId
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * @param typeId the typeId to set
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    /**
     * @return Integer return the start
     */
    public Integer getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(Integer start) {
        this.start = start;
    }

    /**
     * @return Integer return the length
     */
    public Integer getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(Integer length) {
        this.length = length;
    }

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
    }

    /**
     * @return String return the $userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param $userId the $userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

}