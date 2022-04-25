package top.lijieyao.datasync.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Description: redis模板客户端缓存
 * @Author: LiJieYao
 * @Date: 2022/4/24 23:02
 */
@Slf4j
public class RedisTemplateThreadLocal {

    private static final InheritableThreadLocal<RedisTemplate<String, String>> redisTemplate = new InheritableThreadLocal<>();

    public static RedisTemplate<String, String> get() {
        return redisTemplate.get();
    }


    public static void set(RedisTemplate<String, String> objectRedisTemplate) {
        redisTemplate.set(objectRedisTemplate);
    }

    public static void clean() {
        redisTemplate.remove();
        log.debug("RedisTemplateThreadLocal..clean succeed!!!!");
    }
}
