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

    public static void deleteById(String id) {
        collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }

    public static Blog getBlogById(String id, String userId, String level) {
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

    public static List<Document> getBlogByType(Integer type, String level, String userId, Integer start, Integer length) {
        FindIterable<Document> result = filterByUserPage(collection.find(Filters.eq("type", type)), userId, level, start, length);
        if (result == null) return null;

        Iterator<Document> itr = result.iterator();
        List<Document> list = new ArrayList<>();
        while (itr.hasNext()) {
            list.add(itr.next());
        }
        return list;
    }

    private static FindIterable<Document> filterByUser(FindIterable<Document> docs, String userId, String type) {
        switch (type) {
            case "admin":
                break;
            case "user":
                docs = docs.filter(Filters.or(Filters.eq("userId", userId), Filters.eq("status", Blog.STATUS_RELEASED)));
                break;
            case "visitor":
                docs = docs.filter(Filters.eq("status", Blog.STATUS_RELEASED));
                break;
            default:
                break;
        }

        return docs;
    }

    private static FindIterable<Document> filterByUserPage(FindIterable<Document> docs, String userId, String type, Integer start, Integer length) {
        if (start == null || length == null) return null;
        return filterByUser(docs, userId, type).sort(Filters.eq("id", 1)).skip(start).limit(length);
    }


}