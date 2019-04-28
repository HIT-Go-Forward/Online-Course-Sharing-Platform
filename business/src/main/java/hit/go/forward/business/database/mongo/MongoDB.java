package hit.go.forward.business.database.mongo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import hit.go.forward.common.entity.blog.Blog;
import hit.go.forward.common.entity.mongo.BlogDocument;
import hit.go.forward.common.entity.user.User;
import hit.go.forward.common.util.blog.BlogEntityUtil;

public class MongoDB {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 27017;
    private static final String BASE_NAME = "hgf";
    private static final String COLLECTION_NAME = "blog";


    private static MongoClient client;
    private static MongoDatabase db;
    private static MongoCollection<Document> collection;

    static {
        try {
            client = new MongoClient(SERVER_HOST, SERVER_PORT);
            db = client.getDatabase(BASE_NAME);
            collection = db.getCollection(COLLECTION_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insert(Blog blog) {
        collection.insertOne(BlogEntityUtil.toDocument(blog));
    }

    public static boolean deleteById(String id) {
        return collection.deleteOne(Filters.eq("_id", new ObjectId(id))).wasAcknowledged();
    }

    public static boolean deleteById(String id, String userId) {
        return collection.deleteOne(Filters.and(Filters.eq("_id", new ObjectId(id)), Filters.eq("userId", userId))).wasAcknowledged();
    }

    public static boolean updateBlog(Blog blog) {
        return collection.updateOne(Filters.eq("_id", new ObjectId(blog.getId())), BlogEntityUtil.toDocument(blog)).wasAcknowledged();
    }

    public static boolean updateBlog(Blog blog, String userId) {
        return collection.updateOne(Filters.and(Filters.eq("_id", new ObjectId(blog.getId())), Filters.eq("userId", userId)), BlogEntityUtil.toDocument(blog)).wasAcknowledged();
    }

    public static boolean increaseField(String blogId, String field) {
        Document doc = collection.find(Filters.eq("_id", new ObjectId(blogId))).first();
        doc.put(field, ((Integer)doc.get(field)) + 1);
        return collection.updateOne(Filters.eq("_id", new ObjectId(blogId)), doc).wasAcknowledged();
    }

    public static Blog getBlogById(String id, String userId, Integer level) {
        FindIterable<Document> result = filterByUser(collection.find(Filters.eq("_id", new ObjectId(id))), userId, level);
        Document doc = result.first();
        Blog blog = null;
        try {
            blog = BlogEntityUtil.fromDocument(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blog;
    }

    public static List<Document> getBlogByType(Integer type, Integer level, String userId, Integer start, Integer length) {
        FindIterable<Document> result = filterByUserPage(collection.find(Filters.eq("type", type)), userId, level, start, length);
        if (result == null) return null;

        return docItrToList(result.iterator());
    }

    public static List<Document> queryBlogByUser(String userId, Integer start, Integer length) {
        FindIterable<Document> result = collection.find(Filters.eq("userId", userId)).sort(Filters.eq("_id", 1)).skip(start).limit(length);
        if (result == null) return null;
        return docItrToList(result.iterator());
    }

    private static List<Document> docItrToList(Iterator<Document> itr) {
        List<Document> list = new ArrayList<>();
        while (itr.hasNext()) {
            list.add(itr.next());
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
        if (start == null || length == null) return null;
        return filterByUser(docs, userId, userType).sort(Filters.eq("_id", 1)).skip(start).limit(length);
    }


}