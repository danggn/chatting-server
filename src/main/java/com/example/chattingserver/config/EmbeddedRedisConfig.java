package com.example.chattingserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
public class EmbeddedRedisConfig {

    @Value("${spring.redis.port}")
    private int redisPort;
    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() {
        redisServer = new RedisServer(redisPort);
        redisServer.start(); //Redis 시작
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop(); //Redis 종료
        }
    }
}
