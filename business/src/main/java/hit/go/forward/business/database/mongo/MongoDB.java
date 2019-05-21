package hit.go.forward.business.database.mongo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.types.ObjectId;

import hit.go.forward.common.entity.blog.Blog;
import hit.go.forward.common.entity.blog.BlogUserSummary;
import hit.go.forward.common.entity.comment.PrimaryComment;
import hit.go.forward.common.entity.comment.SecondaryComment;
import hit.go.forward.common.entity.user.User;
import hit.go.forward.common.util.blog.BlogEntityUtil;

public class MongoDB {
    private static final String BASE_NAME = "hgf";
    private static final String BLOG_COLLECTION_NAME = "blog";
    private static final String BLOG_LIKE_COLLECTION_NAME = "blog_like";
    private static final String COMMENT_COLLECTION_NAME = "comment";
    private static final String BLOG_USER_COLLECTION_NAME = "blog_user";


    private static MongoClient client;
    private static MongoDatabase db;
    private static MongoCollection<Document> collection;
    private static MongoCollection<Document> likeCollection;
    private static MongoCollection<Document> commentCollection;
    private static MongoCollection<Document> blogUserCollection;

    // static {
    //     try {
    //         client = new MongoClient(SERVER_HOST, SERVER_PORT);
    //         db = client.getDatabase(BASE_NAME);
    //         collection = db.getCollection(BLOG_COLLECTION_NAME);
    //         likeCollection = db.getCollection(BLOG_LIKE_COLLECTION_NAME);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    public static void main(String[] args) {
        connect("localhost", 27017);
        Document doc = new Document("test", "1");
        collection.insertOne(doc);
        System.out.println();
    }

    public static void connect(String host, int port) {
        try {
            client = new MongoClient(host, port);
            db = client.getDatabase(BASE_NAME);
            getCollections();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void connect(String uri) {
        try {
            client = new MongoClient(uri);
            db = client.getDatabase(BASE_NAME);
            getCollections();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getCollections() {
        collection = db.getCollection(BLOG_COLLECTION_NAME);
        likeCollection = db.getCollection(BLOG_LIKE_COLLECTION_NAME);
        commentCollection = db.getCollection(COMMENT_COLLECTION_NAME);
        blogUserCollection = db.getCollection(BLOG_USER_COLLECTION_NAME);

        if (collection == null) {
            db.createCollection(BLOG_COLLECTION_NAME);
            collection = db.getCollection(BLOG_COLLECTION_NAME);
        }
        if (likeCollection == null) {
            db.createCollection(BLOG_LIKE_COLLECTION_NAME);
            likeCollection = db.getCollection(BLOG_LIKE_COLLECTION_NAME);
        }
        if (commentCollection == null) {
            db.createCollection(COMMENT_COLLECTION_NAME);
            commentCollection = db.getCollection(COMMENT_COLLECTION_NAME);
        }
        if (blogUserCollection == null) {
            db.createCollection(BLOG_USER_COLLECTION_NAME);
            commentCollection = db.getCollection(BLOG_USER_COLLECTION_NAME);
        }
    }

    public static String insert(Blog blog) {
        Document doc = BlogEntityUtil.toDocument(blog);
        collection.insertOne(doc);
        return ((ObjectId)doc.get("_id")).toHexString();
    }

    public static boolean deleteById(String id) {
        return collection.deleteOne(Filters.eq("_id", new ObjectId(id))).getDeletedCount() > 0;
    }

    public static boolean deleteById(String id, String userId) {
        return collection.deleteOne(Filters.and(Filters.eq("_id", new ObjectId(id)), Filters.eq("userId", userId))).getDeletedCount() > 0;
    }

    public static boolean updateBlog(Blog blog) {
        return collection.updateOne(Filters.eq("_id", new ObjectId(blog.getId())), new Document("$set", BlogEntityUtil.toDocument(blog))).getModifiedCount() > 0;
    }

    public static boolean updateBlog(Blog blog, String userId) {
        return collection.updateOne(Filters.and(Filters.eq("_id", new ObjectId(blog.getId())), Filters.eq("userId", userId)), new Document("$set", BlogEntityUtil.toDocument(blog))).getModifiedCount() > 0;
    }

    public static long updateBlog(String[] ids, String field, Object value) {
        ObjectId[] oids = new ObjectId[ids.length];
        for (int i = 0;i < ids.length;i ++) oids[i] = new ObjectId(ids[i]);
        Document doc = new Document();
        doc.put(field, value);
        return collection.updateMany(Filters.all("_id", oids), new Document("$set", doc)).getModifiedCount();
    }

    public static boolean updateBlog(String id, String field, Object value) {
        Document doc = new Document();
        doc.put(field, value);
        return collection.updateOne(Filters.eq("_id", new ObjectId(id)), new Document("$set", doc)).getModifiedCount() == 1;
    }

    public static boolean increaseField(String blogId, String field) {
        return collection.updateOne(Filters.eq("_id", new ObjectId(blogId)), new Document("$inc", new Document(field, 1))).getModifiedCount() == 1;
    }

    public static boolean decreaseField(String blogId, String field) {
        return collection.updateOne(Filters.eq("_id", new ObjectId(blogId)), new Document("$inc", new Document(field, -1))).getModifiedCount() == 1;
    }

    public static Document getBlogById(String id, String userId, Integer level) {
        ObjectId objectId = new ObjectId(id);
        String idt = objectId.toHexString();
        FindIterable<Document> result = filterByUser(collection.find(Filters.eq("_id", objectId)), userId, level);
        Document doc = result.first();
        if (doc != null) {
            doc.put("id", idt);
            doc.remove("_id");
        }
        return doc;
    }

    public static List<Document> getBlogByType(Integer type, Integer level, String userId, Integer start, Integer length) {
        FindIterable<Document> result = filterByUserPage(collection.find(Filters.eq("type", type)), userId, level, start, length);
        if (result == null) return null;
        return simplify(docItrToList(result.iterator()));
    }

    public static List<Document> getBlogByField(String field, Object value, Integer start, Integer length) {
        FindIterable<Document> result;
        if (start != null && length != null) result = collection.find(Filters.eq(field, value)).sort(Filters.eq("_id", 1)).skip(start).limit(length);
        else result = collection.find(Filters.eq(field, value));
        return simplify(docItrToList(result.iterator()));
    }

    public static List<Document> queryBlogByUser(String userId, Integer start, Integer length) {
        FindIterable<Document> result;
        if (start != null && length != null) result = collection.find(Filters.eq("userId", userId)).sort(Filters.eq("_id", 1)).skip(start).limit(length);
        else result = collection.find(Filters.eq("userId", userId));
        if (result == null) return null;
        return simplify(docItrToList(result.iterator()));
    }

    private static List<Document> simplify(List<Document> docs) {
        for (Document doc : docs) {
            String blog = (String) doc.get("blog");
            if (blog != null && blog.length() > 40) blog = blog.substring(0, 40); 
            doc.put("blog", blog);
        }
        return docs;
    }

    public static boolean likeBlog(String userId, String blogId) {
        FindIterable<Document> result = likeCollection.find(Filters.and(Filters.eq("userId", userId), Filters.eq("blogId", blogId)));
        Document record = result.first();
        if (record == null) {
            record = new Document();
            record.put("userId", userId);
            record.put("blogId", blogId);
            likeCollection.insertOne(record);
            return true;
        }
        return false;
    }

    public static boolean cancelLikeBlog(String userId, String blogId) {
        return likeCollection.deleteOne(Filters.and(Filters.eq("userId", userId), Filters.eq("blogId", blogId))).getDeletedCount() > 0;
    }

    public static boolean insertComment(PrimaryComment comment) {
        Document doc = new Document();
        doc.put("replyTo", comment.getReplyTo());
        doc.put("content", comment.getContent());
        doc.put("commentDate", comment.getCommentDate());
        doc.put("replies", comment.getReplies());
        doc.put("type", comment.getType());
        doc.put("userName", comment.getUserName());
        doc.put("userAvatar", comment.getUserAvatar());
        doc.put("userId", comment.getUserId());
        commentCollection.insertOne(doc);
        return false;
    }

    public static boolean insertComment(SecondaryComment comment) {
        Document doc = new Document();
        doc.put("replyTo", comment.getReplyTo());
        doc.put("content", comment.getContent());
        doc.put("commentDate", comment.getCommentDate());
        doc.put("userName", comment.getUserName());
        doc.put("userAvatar", comment.getUserAvatar());
        doc.put("userId", comment.getUserId());
        
        return commentCollection.updateOne(Filters.eq("_id", new ObjectId(comment.getUnder())), new Document("$push", new Document("replies", doc))).getModifiedCount() >= 1;
    }

    public static BlogUserSummary getBlogUserSummary(String id) {
        Document document = blogUserCollection.find(Filters.eq("userId", id)).first();
        if (document == null) return null;
        BlogUserSummary summary = new BlogUserSummary();
        summary.fromDocument(document);
        return summary;
    }

    public static BlogUserSummary calcBlogUserSummary(String userId) {
        BlogUserSummary summary = new BlogUserSummary();
        FindIterable<Document> result = collection.find(Filters.eq("userId", userId));
        List<Document> docs = docItrToList(result.iterator());
        int visitCount = 0;
        int likeCount = 0;
        int blogCount = 0;
        int commentCount = 0;

        for (Document doc : docs) {
            visitCount += (Integer) doc.get("visitCount");
            likeCount += (Integer) doc.get("likeCount");
        }

        blogCount = docs.size();

        summary.setBlogCount(blogCount);
        summary.setCommentCount(commentCount);
        summary.setLikeCount(likeCount);
        summary.setVisitCount(visitCount);
        summary.setUserId(userId);
        return summary;
    }

    public static void insertBlogUserSummary(BlogUserSummary summary) {
        blogUserCollection.insertOne(summary.toDocument());
    }

    public static boolean incBlogUserField(String id, String field) {
        return blogUserCollection.updateOne(Filters.eq("-id", new ObjectId(id)), new Document("$inc", new Document(field, 1))).getModifiedCount() >= 1;
    }

    public static boolean decBlogUserField(String id, String field) {
        return blogUserCollection.updateOne(Filters.eq("-id", new ObjectId(id)), new Document("$inc", new Document(field, -1))).getModifiedCount() >= 1;
    }

    private static List<Document> docItrToList(Iterator<Document> itr) {
        List<Document> list = new ArrayList<>();
        while (itr.hasNext()) { 
            Document doc = itr.next();
            doc.put("id", ((ObjectId)doc.get("_id")).toHexString());
            list.add(doc);
        }
        return list;
    }

    private static FindIterable<Document> filterByUser(FindIterable<Document> docs, String userId, Integer userType) {
        switch (userType) {
            case User.TYPE_ADMIN:
                break;
            case User.TYPE_STUDENT:
            case User.TYPE_TEACHER:
                docs = docs.filter(Filters.or(Filters.eq("userId", userId), Filters.eq("status", Blog.STATUS_RELEASED)));
                break;
            case User.TYPE_VISITOR:
                docs = docs.filter(Filters.eq("status", Blog.STATUS_RELEASED));
                break;
            default:
                break;
        }

        return docs;
    }

    private static FindIterable<Document> filterByUserPage(FindIterable<Document> docs, String userId, Integer userType, Integer start, Integer length) {
        if (start == null || length == null) return filterByUser(docs, userId, userType);
        return filterByUser(docs, userId, userType).sort(Filters.eq("_id", 1)).skip(start).limit(length);
    }
}