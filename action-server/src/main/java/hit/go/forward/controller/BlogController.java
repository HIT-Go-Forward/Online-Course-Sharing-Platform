package hit.go.forward.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import hit.go.forward.business.database.dao.BlogMapper;
import hit.go.forward.common.entity.blog.Blog;
import hit.go.forward.common.protocol.RequestResults;

@Controller
@ResponseBody
@RequestMapping("/blog")
public class BlogController {
    private BlogMapper blogMapper;
    
    public BlogController(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;
    }

    @RequestMapping("/uploadBlog")
    public String uploadBlog(Blog blog, String $userId) {
        
        return RequestResults.success();
    }

    @RequestMapping("/getBlog")
    public String getBlog(String id, String $userId) {

        return RequestResults.success();
    }

    @RequestMapping("/deleteBlog")
    public String deleteBlog(String id, String $userId) {

        return RequestResults.success();
    }

    @RequestMapping("updateBlog")
    public String updateBlog(Blog blog, String $userId) {
        
        return RequestResults.success();
    }
}