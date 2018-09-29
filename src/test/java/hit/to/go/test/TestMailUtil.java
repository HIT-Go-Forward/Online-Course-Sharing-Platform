package hit.to.go.test;

import hit.to.go.platform.util.MailUtil;
import org.junit.Test;

/**
 * Created by 班耀强 on 2018/9/29
 */
public class TestMailUtil {

    @Test
    public void testSend() {
        MailUtil.send("1103255088@qq.com", "It works!");
    }
}
