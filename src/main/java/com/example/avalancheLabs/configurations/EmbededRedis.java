package com.example.avalancheLabs.configurations;

import com.example.avalancheLabs.controllers.ExceptionHandlingController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Component
public class EmbededRedis {
    private RedisServer redisServer;

    //private static Logger logger =  LoggerFactory.getLogger(EmbededRedis.class);

    public EmbededRedis(RedisProperties redisProperties) throws IOException {
        int port = redisProperties.getRedisPort();
        String host = redisProperties.getRedisHost();
        //logger.info(host + "  " + port);

        this.redisServer = RedisServer.builder()
                .port(port)
                //.redisExecProvider(customRedisExec) //com.github.kstyrc (not com.orange.redis-embedded)
                .setting("maxmemory 128M") //maxheap 128M
                .build();
        // this.redisServer = new RedisServer(port);
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
