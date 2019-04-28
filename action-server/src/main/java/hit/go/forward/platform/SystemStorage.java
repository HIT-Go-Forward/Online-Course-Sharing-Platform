package hit.go.forward.platform;

import hit.go.forward.common.entity.blog.Blog;
import hit.go.forward.common.entity.validate.ValidateCode;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/10/1
 */
public class SystemStorage {
    private static Map<String, Integer> actionPower = new HashMap<>();
    private static final Map<String, ValidateCode> vdMap = new HashMap<>();
    private static final Map<String, Blog> blogCache = new HashMap<>();
    
    static {
        InputStream in = SystemStorage.class.getClassLoader().getResourceAsStream("actions.conf");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                String[] tmp = line.split(",");
                actionPower.put(tmp[0], Integer.valueOf(tmp[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Integer getActionPower(String url) {
        return actionPower.get(url);
    }

    public synchronized static void store(String email, ValidateCode code) {
        vdMap.put(email, code);
    }

    public static ValidateCode getValidateCode(String email) {
        return vdMap.get(email);
    }

    public static Blog getBlogCache(Integer id) {
        return blogCache.get(id);
    }

    public static void cacheBlog(Blog blog) {
        if (blog.getId() == null) return;
        blogCache.put(blog.getId(), blog);
    }
}
