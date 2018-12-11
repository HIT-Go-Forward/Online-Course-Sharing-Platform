package hit.go.forward.im.system;

import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

public final class OnlineSession {
    private static final Map<String, WebSocketSession> storage = new HashMap<>();

    public synchronized static void store(String id, WebSocketSession socketSession) {
        storage.put(id, socketSession);
    }

    public static WebSocketSession get(String id) {
        return storage.get(id);
    }
}
