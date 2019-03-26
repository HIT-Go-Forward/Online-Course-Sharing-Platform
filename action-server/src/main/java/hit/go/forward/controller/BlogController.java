package hit.go.forward.controller;

import org.bson.Document;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import hit.go.forward.business.database.dao.BlogMapper;
import hit.go.forward.business.database.mongo.MongoDB;
import hit.go.forward.common.entity.blog.Blog;
import hit.go.forward.common.entity.blog.param.BlogPostParam;
import hit.go.forward.common.entity.blog.param.BlogQuery;
import hit.go.forward.common.entity.mongo.BlogDocument;
import hit.go.forward.common.exception.DatabaseWriteException;
import hit.go.forward.common.protocol.RequestResult;
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
    public RequestResult uploadBlog(@RequestBody BlogPostParam blog, String $userId) {
        String operation = blog.getOperation();
        if (operation == null) return RequestResults.lackNecessaryParam("operation");
        else if (operation.equals("draft")) blog.setStatus(Blog.STATUS_DRAFT);
        else if (operation.equals("release")) blog.setStatus(Blog.STATUS_RELEASED);
        else return RequestResults.invalidParamValue("operation=" + operation);
        blog.setUserId($userId);
        if (MapperOpResultUtil.isSucceded(blogMapper.insertBlog(blog))) return RequestResults.success(blog.getId());
        throw new DatabaseWriteException();
    }

    @Transactional
    @RequestMapping("/deleteBlog")
    public RequestResult deleteBlog(BlogQuery blogQuery, String $userId) {
        blogQuery.setUserId($userId);
        if (MapperOpResultUtil.isSucceded(blogMapper.deleteblogById(blogQuery))) return RequestResults.success();
        throw new DatabaseWriteException();
    }

    @Transactional
    @RequestMapping(value = "updateBlog", method = RequestMethod.POST)
    public RequestResult updateBlog(@RequestBody BlogPostParam blog, String $userId) {
        blog.setUserId($userId);
        if (MapperOpResultUtil.isSucceded(blogMapper.updateBlog(blog))) return RequestResults.success();
        throw new DatabaseWriteException();
    }

    @Transactional
    @RequestMapping("/handleBlogApplies")
    public RequestResult handleBlogApplies(BlogQuery blogQuery) {
        if (blogQuery.getBlogIds() == null || blogQuery.getOperation() == null) return RequestResults.lackNecessaryParam("blogIds");
        return RequestResults.success();

    }

    @RequestMapping("/getBlog")
    public RequestResult getBlog(BlogQuery blogQuery, String $userId) {
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
    public RequestResult getBlogListByType(BlogQuery blogQuery, String $userId) {
        blogQuery.setUserId($userId);
        return RequestResults.success(blogMapper.selectBlogListByType(blogQuery));
    }

    @RequestMapping("/getBlogListByState")
    public RequestResult getBlogListByState(BlogQuery blogQuery) {
        
        return RequestResults.success();
    }

    // ######### MongoDB ###############

    @RequestMapping(value = "/saveBlog", method = RequestMethod.POST)
    public RequestResult saveBolg(@RequestBody Blog blog, String $userId) {
        MongoDB.insert(blog);
        return RequestResults.success();
    }

    @RequestMapping("/")
    public RequestResult getUserBlog(String $userId, Integer start, Integer length) {

        return RequestResults.success();
    }
}