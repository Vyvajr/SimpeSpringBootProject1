package com.example.avalancheLabs.configurations;

import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class EmbededRedis {
    private RedisServer redisServer;

    public EmbededRedis(RedisProperties redisProperties) {
        int port = redisProperties.getRedisPort();
        this.redisServer = new RedisServer(port);
    }

    @PostConstruct
    public void postConstruct() {
        redisServer.start();
    }

    @PreDestroy
    public void preDestroy() {
        redisServer.stop();
    }
}
