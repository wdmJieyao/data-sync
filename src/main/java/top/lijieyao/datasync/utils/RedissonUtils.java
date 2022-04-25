package top.lijieyao.datasync.utils;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import top.lijieyao.datasync.comment.RedisTemplateThreadLocal;

import java.util.Map;
import java.util.Optional;

/**
 * @Description: redisson工具类
 * @Author: LiJieYao
 * @Date: 2022/4/25 9:28
 */
@Component
@Slf4j
public class RedissonUtils {

    private RedissonUtils() {
    }

    private static final Map<Integer, RedisTemplate<String, String>> cache = Maps.newConcurrentMap();

    private static RedissonClient redissonClient;

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {
        RedissonUtils.redissonClient = redissonClient;
    }

    /**
     * 切换数据库 并缓存
     * @param dbIndex 数据库序号
     */
    protected synchronized static void switchDatabase(int dbIndex) {
        RedisTemplate<String, String> indexRedisTemplate = Optional.ofNullable(cache.get(dbIndex))
                .orElseGet(() -> {
                    // 加载对应的数据库
                    Config config = redissonClient.getConfig();
                    SingleServerConfig serverConfig = config.useSingleServer();
                    serverConfig.setDatabase(dbIndex);
                    RedissonClient client = Redisson.create(config);
                    RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
                    redisTemplate.setConnectionFactory(new RedissonConnectionFactory(client));
                    //key序列化方式,存string
                    redisTemplate.setKeySerializer(new StringRedisSerializer());
                    //value序列化方式,存string
                    redisTemplate.setValueSerializer(new StringRedisSerializer());
                    //hash key序列化方式,存string
                    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
                    //hash value序列化方式,存string
                    redisTemplate.setHashValueSerializer(new StringRedisSerializer());
                    // 刷新配置
                    redisTemplate.afterPropertiesSet();
                    cache.put(dbIndex, redisTemplate);
                    return redisTemplate;
                });
        // 切换数据库
        RedisTemplateThreadLocal.set(indexRedisTemplate);
    }
}
