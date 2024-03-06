package com.uriel.travel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value(value = "${spring.data.redis.password}")
    private String redisPassword;

//    private Environment environment;

    // LettuceConnectionFactory 객체를 생성하여 반환하는 메서드
    // 이 객체는 Redis Java 클라이언트 라이브러리인 Lettuce를 사용하여 Redis 서버와 연결해 줌
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // RedisStandaloneConfiguration를 통해 redis 접속 정보(host, port 등)를 갖고 있는 객체를 생성
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPort(redisPort);
        redisStandaloneConfiguration.setPassword(redisPassword);
        // Redis 설정정보를 LettuceConnectionFactory에 담아서 반환
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    // Redis 작업을 수행하기 위해 RedisTemplate 객체를 생성하여 반환하는 메서드
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        return redisTemplate;
    }

    // Redis를 캐시로 사용하기 위한 CacheManager 빈 생성
    @Bean
    public CacheManager cacheManager() {
        // RedisCacheManagerBuilder를 사용하여 RedisConnectionFactory를 설정하고, RedisCacheConfiguration 구성
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory());
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                // Redis의 Key와 Value의 직렬화 방식 설정
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .prefixCacheNameWith("cache:") // Key의 접두사로 "cache:"를 앞에 붙여 저장
                .entryTtl(Duration.ofMinutes(30)); // 캐시 수명(유효기간)을 30분으로 설정
        builder.cacheDefaults(configuration);

        return builder.build(); // cacheDefaults를 설정하여 만든 RedisCacheManager 반환
    }
}
