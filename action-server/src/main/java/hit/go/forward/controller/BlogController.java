package hit.go.forward.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import hit.go.forward.business.database.dao.BlogMapper;
import hit.go.forward.common.entity.blog.Blog;
import hit.go.forward.common.entity.blog.param.BlogQuery;
import hit.go.forward.common.exception.DatabaseWriteException;
import hit.go.forward.common.protocol.RequestResults;
import hit.go.forward.common.util.MapperOpResultUtil;

@Controller
@ResponseBody
@RequestMapping("/blog")
public class BlogController {
    private BlogMapper blogMapper;
    
    public BlogController(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;
    }

    @Transactional
    @RequestMapping(value = "/uploadBlog", method = {RequestMethod.POST})
    public String uploadBlog(Blog blog, String $userId) {
        blog.setUserId($userId);
        if (MapperOpResultUtil.isSucceded(blogMapper.insertBlog(blog))) return RequestResults.success(blog.getId());
        throw new DatabaseWriteException();
    }

    @Transactional
    @RequestMapping("/deleteBlog")
    public String deleteBlog(Blog blog, String $userId) {
        blog.setUserId($userId);
        if (MapperOpResultUtil.isSucceded(blogMapper.deleteblogById(blog))) return RequestResults.success();
        throw new DatabaseWriteException();
    }

    @Transactional
    @RequestMapping(value = "updateBlog", method = {RequestMethod.POST})
    public String updateBlog(Blog blog, String $userId) {
        blog.setUserId($userId);
        if (MapperOpResultUtil.isSucceded(blogMapper.updateBlog(blog))) return RequestResults.success();
        throw new DatabaseWriteException();
    }

    @RequestMapping("/getBlog")
    public String getBlog(BlogQuery blogQuery, String $userId) {
        blogQuery.setUserId($userId);
        Blog blog = blogMapper.selectBlogById(blogQuery);
        return RequestResults.success(blog);
    }

    @RequestMapping("/getBlogListByType")
    public String getBlogListByType(BlogQuery blogQuery, String $userId) {
        blogQuery.setUserId($userId);
        return RequestResults.success(blogMapper.selectBlogListByType(blogQuery));
    }
}