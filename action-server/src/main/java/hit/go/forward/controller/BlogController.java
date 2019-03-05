package hit.go.forward.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import hit.go.forward.business.database.dao.BlogMapper;
import hit.go.forward.common.entity.blog.Blog;
import hit.go.forward.common.entity.blog.param.BlogQuery;
import hit.go.forward.common.exception.DatabaseWriteException;
import hit.go.forward.common.protocol.RequestResults;
import hit.go.forward.common.util.MapperOpResultUtil;
import hit.go.forward.platform.SystemStorage;

@Controller
@ResponseBody
@RequestMapping("/blog")
public class BlogController {
    private BlogMapper blogMapper;
    
    public BlogController(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;
    }

    @Transactional
    @RequestMapping(value = "/uploadBlog", method = RequestMethod.POST)
    public String uploadBlog(@RequestBody Blog blog, String $userId) {
        blog.setUserId($userId);
        if (MapperOpResultUtil.isSucceded(blogMapper.insertBlog(blog))) return RequestResults.success(blog.getId());
        throw new DatabaseWriteException();
    }

    @Transactional
    @RequestMapping("/deleteBlog")
    public String deleteBlog(BlogQuery blogQuery, String $userId) {
        blogQuery.setUserId($userId);
        if (MapperOpResultUtil.isSucceded(blogMapper.deleteblogById(blogQuery))) return RequestResults.success();
        throw new DatabaseWriteException();
    }

    @Transactional
    @RequestMapping(value = "updateBlog", method = RequestMethod.POST)
    public String updateBlog(@RequestBody Blog blog, String $userId) {
        blog.setUserId($userId);
        if (MapperOpResultUtil.isSucceded(blogMapper.updateBlog(blog))) return RequestResults.success();
        throw new DatabaseWriteException();
    }

    @Transactional
    @RequestMapping("/handleBlogApplies")
    public String handleBlogApplies(BlogQuery blogQuery) {
        if (blogQuery.getBlogIds() == null || blogQuery.getOperation() == null) return RequestResults.lackNecessaryParam("blogIds");
        return RequestResults.success();

    }

    @RequestMapping("/getBlog")
    public String getBlog(BlogQuery blogQuery, String $userId) {
        blogQuery.setUserId($userId);
        if (blogQuery.getId() == null) return RequestResults.lackNecessaryParam("id");
        Blog blog = SystemStorage.getBlogCache(blogQuery.getId());
        if (blog == null) {
            blog = blogMapper.selectBlogById(blogQuery);
            SystemStorage.cacheBlog(blog);
        }
        blog.setVisitCount(blog.getVisitCount() + 1);
        return RequestResults.success(blog);
    }

    @RequestMapping("/getBlogListByType")
    public String getBlogListByType(BlogQuery blogQuery, String $userId) {
        blogQuery.setUserId($userId);
        return RequestResults.success(blogMapper.selectBlogListByType(blogQuery));
    }

    @RequestMapping("/getBlogListByState")
    public String getBlogListByState(BlogQuery blogQuery) {
        
        return RequestResults.success();
    }

}