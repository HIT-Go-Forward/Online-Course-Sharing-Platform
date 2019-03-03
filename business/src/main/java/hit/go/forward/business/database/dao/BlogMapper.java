package hit.go.forward.business.database.dao;

import java.util.List;

import hit.go.forward.common.entity.blog.Blog;
import hit.go.forward.common.entity.blog.BlogSummary;
import hit.go.forward.common.entity.blog.param.BlogQuery;

public interface BlogMapper {
    Integer insertBlog(Blog blog);
    
    Integer updateBlog(Blog blog);

    Integer deleteblogById(BlogQuery blogQuery);

    Integer handleBlogApplies(BlogQuery blogQuery);

    Blog selectBlogById(BlogQuery blogQuery);

    List<BlogSummary> selectBlogListByType(BlogQuery blogQuery);

    List<BlogSummary> selectBlogListByState(BlogQuery blogQuery);

}