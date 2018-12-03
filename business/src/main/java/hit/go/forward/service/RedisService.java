package hit.go.forward.service;

import java.util.List;
import java.util.Map;

public interface RedisService {
    String get(String key);

    String set(String key, String value);

    List<String> hmget(String key, String ...fields);

    String hmset(String key, Map<String, String> data);


}
