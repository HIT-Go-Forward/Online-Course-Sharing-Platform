package hit.go.forward.test;

import hit.go.forward.service.RedisService;
import hit.go.forward.service.impl.RedisServiceImpl;
import org.junit.Test;

public class RedisServiceImplTest {
    private RedisService service = new RedisServiceImpl();

    @Test
    public void test() {
        String test = service.get("test");
        System.out.println();
    }
}
