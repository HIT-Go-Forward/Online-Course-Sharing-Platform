package hit.go.forward.im.controller;

import hit.go.forward.common.protocol.RequestResults;
import hit.go.forward.im.system.OnlineSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Controller
@ResponseBody
@RequestMapping("/imMsg")
public class IMController {
    @RequestMapping("/sendMsg")
    public String sendMsg(String id, String msg) {
        WebSocketSession socketSession = OnlineSession.get(id);
        if (socketSession != null) {
            try {
                TextMessage message = new TextMessage(msg);
                socketSession.sendMessage(message);
                return RequestResults.success();
            } catch (Exception e) {
                e.printStackTrace();
                return RequestResults.error("消息发送失败");
            }
        }
        return RequestResults.error();
    }
}
