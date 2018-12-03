package hit.go.forward.service.impl;

import hit.go.forward.ServiceConfig;
import hit.go.forward.service.RedisService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

@Service
public class RedisServiceImpl implements RedisService {
    private static final Jedis client = new Jedis(ServiceConfig.REDIS_HOST, ServiceConfig.REDIS_PORT);

    @Override
    public String get(String key) {
        return client.get(key);
    }

    @Override
    public String set(String key, String value) {
        return client.set(key, value);
    }

    @Override
    public List<String> hmget(String key, String ...fields) {
        return client.hmget(key, fields);
    }

    @Override
    public String hmset(String key, Map<String, String> data) {
        return client.hmset(key, data);
    }
}
