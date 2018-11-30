package hit.go.forward.service.impl;

import hit.go.forward.ServiceConfig;
import hit.go.forward.service.RedisService;
import redis.clients.jedis.Jedis;

public class RedisServiceImpl implements RedisService {
    private static final Jedis client = new Jedis(ServiceConfig.REDIS_HOST, ServiceConfig.REDIS_PORT);

    public String get(String key) {

        return client.get(key);
    }
}
