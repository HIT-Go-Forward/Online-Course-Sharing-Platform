package hit.go.forward.im;

import hit.go.forward.im.system.OnlineSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketTest extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketTest.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        logger.debug("Session id = {}", session.getId());
        OnlineSession.store(session.getId(), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println(message.getPayload());
    }


}
