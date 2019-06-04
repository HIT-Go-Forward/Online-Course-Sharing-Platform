package hit.go.forward.controller;

import java.util.Date;

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
import hit.go.forward.common.entity.blog.BlogUserSummary;
import hit.go.forward.common.entity.blog.param.BlogPostParam;
import hit.go.forward.common.entity.blog.param.BlogQuery;
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

    @Deprecated
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
        if (blog.getOperation() == null) return RequestResults.lackNecessaryParam("operation");
        blog.setUserId($userId);
        blog.setUpdateDate(new Date());
        if (blog.getOperation().equals("draft")) {

            blog.setStatus(Blog.STATUS_DRAFT);
            if (blog.getId() != null) {
                MongoDB.updateBlog(blog, $userId);
                return RequestResults.success();
            }
        }
        else if (blog.getOperation().equals("release")) {
            blog.setStatus(Blog.STATUS_PENDING);
        } else return RequestResults.invalidParamValue("operation");
        blog.setDislikeCount(0);
        blog.setLikeCount(0);
        blog.setVisitCount(0);
        blog.setUploadDate(new Date());
        return RequestResults.success(MongoDB.insert(blog));
    }

    @RequestMapping("/queryBlogByUser")
    public RequestResult queryBlogByUser(String $userId, Integer start, Integer length) {
        return RequestResults.success(MongoDB.queryBlogByUser($userId, start, length));
    }

    @RequestMapping("/queryBlogByType")
    public RequestResult viewBlogByType(String $userId, Integer $userType, Integer typeId, Integer start, Integer length) {
        return RequestResults.success(MongoDB.getBlogByType(typeId, $userType, $userId, start, length));
    }

    @RequestMapping("/queryBlog")
    public RequestResult queryBlog(String $userId, Integer $userType, Integer start, Integer length) {
        if (start == null || length == null) return RequestResults.lackNecessaryParam("start || length");
        return RequestResults.success(MongoDB.getBlog(start, length));
    }

    @RequestMapping("/viewBlogById")
    public RequestResult viewBlogById(String $userId, Integer $userType, String blogId) {
        Document result = MongoDB.getBlogById(blogId, $userId, $userType);
        MongoDB.increaseField(blogId, "visitCount");
        MongoDB.incBlogUserField($userId, "visitCount");
        return RequestResults.success(result);
    }

    @RequestMapping(value = "/editBlog", method = RequestMethod.POST)
    public RequestResult editBlog(String $userId, @RequestBody Blog blog) {
        if (blog.getId() == null) return RequestResults.lackNecessaryParam("id");
        blog.setStatus(Blog.STATUS_PENDING);
        blog.setUserId($userId);
        blog.setUpdateDate(new Date());
        if (MongoDB.updateBlog(blog)) return RequestResults.success();
        return RequestResults.serverError();
    }

    @RequestMapping("/deleteBlogById")
    public RequestResult deleteBlogById(String $userId, String blogId) {
        if (MongoDB.deleteById(blogId, $userId)) return RequestResults.success();
        return RequestResults.serverError();
    }

    @RequestMapping("/likeBlog")
    public RequestResult likeBlog(String $userId, String blogId, String operation) {
        if (operation == null) return RequestResults.lackNecessaryParam("operation");
        else if (operation.equals("like")) {
            if (MongoDB.likeBlog($userId, blogId)) {
                MongoDB.increaseField(blogId, "likeCount");
                MongoDB.incBlogUserField($userId, "likeCount");
                return RequestResults.success();
            }
            return RequestResults.operationDenied("你已经点过赞啦");
        }
        else if (operation.equals("cancel")) {
            if (MongoDB.cancelLikeBlog($userId, blogId)) {
                MongoDB.decreaseField(blogId, "likeCount");
                MongoDB.decBlogUserField($userId, "likeCount");
                return RequestResults.success();
            }
            return RequestResults.operationDenied("你已取消赞啦");
        }
        return RequestResults.invalidParamValue("operation=" + operation);
    }

    @RequestMapping("/getUserBlogSummary")
    public RequestResult getUserBlogSummary(String userId) {
        BlogUserSummary summary = MongoDB.getBlogUserSummary(userId);
        if (summary == null) summary = MongoDB.calcBlogUserSummary(userId);
        return RequestResults.success(summary);
    }
}