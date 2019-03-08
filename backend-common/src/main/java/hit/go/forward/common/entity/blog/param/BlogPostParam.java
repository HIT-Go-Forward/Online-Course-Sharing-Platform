package hit.go.forward.common.entity.blog.param;

import hit.go.forward.common.entity.blog.Blog;

public class BlogPostParam extends Blog{
    private String operation;

    /**
     * @return String return the opertaion
     */
    public String getOperation() {
        return operation;
    }

    /**
     * @param opertaion the opertaion to set
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }
}