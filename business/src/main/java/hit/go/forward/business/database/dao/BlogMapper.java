package hit.go.forward.business.database.dao;

import hit.go.forward.common.entity.blog.Blog;

public interface BlogMapper {
    Integer insertBlog(Blog blog);

    Blog selectBlogById(String id);
    
    Integer updateBlog(Blog blog);

    Integer deleteblogById(String id);
}