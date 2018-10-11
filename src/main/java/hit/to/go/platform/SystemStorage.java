package hit.to.go.platform;

import hit.to.go.database.dao.ActionMapper;
import hit.to.go.database.mybatis.MybatisProxy;
import hit.to.go.entity.action.Action;
import hit.to.go.entity.user.UserWithPassword;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 班耀强 on 2018/10/1
 */
public class SystemStorage {
    private static final Map<String, UserWithPassword> onlineUsers = new HashMap<>();
    private static final Map<String, Integer> actionPower = new HashMap<>();

    static {
        ActionMapper actionMapper = MybatisProxy.create(ActionMapper.class);
        List<Action> actions = actionMapper.getAllActions();
        for (Action action : actions) actionPower.put(action.getUrl(), action.getPower());
    }


    public static UserWithPassword getOnlineUser(String id) {
        return onlineUsers.get(id);
    }

    public static void setOnlineUser(String id, UserWithPassword user) {
        onlineUsers.put(id, user);
    }

    public static void removeOnlineUser(String id) {
        onlineUsers.remove(id);
    }

    public static Integer getActionPower(String url) {
        return actionPower.get(url);
    }
}
