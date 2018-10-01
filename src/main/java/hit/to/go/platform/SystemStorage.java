package hit.to.go.platform;

import hit.to.go.entity.user.UserWithPassword;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/10/1
 */
public class SystemStorage {
    private static final Map<String, UserWithPassword> onlineUsers = new HashMap<>();


    public static UserWithPassword getOnlineUser(String id) {
        return onlineUsers.get(id);
    }

    public static void setOnlineUser(String id, UserWithPassword user) {
        onlineUsers.put(id, user);
    }

    public static void removeOnlineUser(String id) {
        onlineUsers.remove(id);
    }
}
