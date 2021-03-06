package hit.go.forward.common.util.blog;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import hit.go.forward.common.entity.blog.Blog;

public final class BlogEntityUtil {
    public static Document toDocument(Blog blog) {
        Document result = new Document();
        result.append("title", blog.getTitle())
            .append("userId", blog.getUserId())
            .append("blog", blog.getBlog())
            .append("status", blog.getStatus())
            .append("type", blog.getType())
            .append("uploadDate", blog.getUploadDate().getTime())
            .append("updateDate", blog.getUpdateDate().getTime())
            .append("label", blog.getLabel())
            .append("visitCount", blog.getVisitCount())
            .append("likeCount", blog.getLikeCount())
            .append("dislikeCount", blog.getDislikeCount())
            .append("open", blog.isOpen())
            .append("kind", blog.getKind());
        return result;
    }

    public static Blog fromDocument(Document map) throws Exception {
        Blog blog = null;
        if (map != null) {
            blog = new Blog();
            blog.setId((String)map.get("id"));
            blog.setTitle((String)map.get("title"));
            blog.setUserId((String)map.get("userId"));
            blog.setBlog((String)map.get("blog"));
            blog.setStatus((Integer)map.get("status"));
            blog.setType((Integer)map.get("type"));
            blog.setUploadDate(new Date((Integer)map.get("uploadDate")));
            blog.setUpdateDate(new Date((Integer)map.get("updateDate")));
            blog.setLabel((String)map.get("label"));
            blog.setVisitCount((Integer)map.get("visitCount"));
            blog.setLikeCount((Integer)map.get("likeCount"));
            blog.setDislikeCount((Integer)map.get("dislikeCount"));
            blog.setOpen((Boolean)map.get("open"));
            blog.setKind((String)map.get("kind"));
        }
        return blog;
    }

    public boolean insertCheck(Blog blog) {
        
        return true;
    }
}