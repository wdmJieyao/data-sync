package top.lijieyao.datasync.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Author lijieyao
 * @Date 2020/6/9 14:28
 **/
@Configuration
@Slf4j
public class RedisConfig {

    @Autowired
    RedissonClient redissonClient;

    @Bean(name = "stringSerializeRedisTemplate")
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //key序列化方式,存string
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //value序列化方式,存string
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        //hash key序列化方式,存string
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //hash value序列化方式,存string
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

}
