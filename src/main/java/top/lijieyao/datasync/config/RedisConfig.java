package top.lijieyao.datasync.config;

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
public class RedisConfig {

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
