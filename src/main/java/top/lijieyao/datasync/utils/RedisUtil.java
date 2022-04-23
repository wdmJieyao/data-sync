package top.lijieyao.datasync.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @description: redis工具类
 * @author: LiJieYao
 * @date: 2021/6/29
 */
@Component
@Slf4j
public class RedisUtil {

    private RedisUtil() {
    }


    private static RedisTemplate<String, String> redisTemplate;

    @Autowired
    public void setRedisTemplate(@Qualifier("stringSerializeRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    /**
     * set string
     * @param key
     * @param value
     */
    public static void setString(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * set string
     * @param key
     * @param value
     * @param ttl      过期时间
     * @param timeUnit 过期时间单位
     */
    public static void setString(String key, String value, long ttl, TimeUnit timeUnit) {
        log.info("key:{}  value:{}  ttl:{}", key, value, ttl);
        if (ttl > 0) {
            redisTemplate.opsForValue().set(key, value, ttl, timeUnit);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }

    }

    /**
     * cast obj to json and set redis
     * @param key
     * @param obj
     */
    public static void setObj(String key, Object obj) {
        String json = JSON.toJSONString(obj);
        setString(key, json);
    }


    /**
     * cast obj to json and set redis
     * @param key
     * @param obj
     * @param ttl      过期时间
     * @param timeUnit 过期时间单位
     */
    public static void setObj(String key, Object obj, long ttl, TimeUnit timeUnit) {
        String json = JSON.toJSONString(obj);
        setString(key, json, ttl, timeUnit);
    }

    /**
     * get string
     * @param key
     * @return
     */
    public static String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * get json string and cast to obj
     * @param key
     * @param classOfT class of the target object
     * @param <T>
     * @return
     */
    public static <T> T get(String key, Class<T> classOfT) {
        String json = redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return JSON.parseObject(json, classOfT);
    }

    /**
     * get json string and cast to obj
     * @param key
     * @param classOfT class of the target object
     * @param <T>
     * @return
     */
    public static <T> List<T> getArrayObj(String key, Class<T> classOfT) {
        String json = redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return JSON.parseObject(json, new TypeReference<List<T>>(classOfT) {
        });
    }


    /**
     * delete key
     * @param key
     * @return
     */
    public static Boolean del(String key) {
        return redisTemplate.delete(key);
    }


    /**
     * sadd strings
     * @param key
     * @param values
     * @return
     */
    public static Long sAdd(String key, String... values) {
        return redisTemplate.opsForSet().add(key, values);
    }


    /**
     * zset pop
     * @param key
     * @return String
     */
    public static String zPop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }


    /**
     * incr
     * @param key
     * @return
     */
    public static Long incr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }


    /**
     * get current value of the increment key
     * @param key
     * @return
     */
    public static Long getIncr(String key) {
        RedisSerializer<String> stringSerializer = redisTemplate.getStringSerializer();
        byte[] serialize = stringSerializer.serialize(key);
        String count = stringSerializer.deserialize(redisTemplate.getConnectionFactory().getConnection().get(serialize));
        if (StringUtils.isEmpty(count)) {
            return 0L;
        }
        return Long.valueOf(count);
    }


    /**
     * right push to list
     * @param key
     * @param value
     * @return
     */
    public static Long rPush(String key, String value) {
        ListOperations<String, String> opsForList = redisTemplate.opsForList();
        return opsForList.rightPush(key, value);
    }


    /**
     * left pop of list
     * @param key
     * @return
     */
    public static String lPop(String key) {
        ListOperations<String, String> opsForList = redisTemplate.opsForList();
        return opsForList.leftPop(key);
    }


    /**
     * length of list
     * @param key
     * @return
     */
    public static Long lLen(String key) {
        ListOperations<String, String> opsForList = redisTemplate.opsForList();
        return opsForList.size(key);
    }


    /**
     * zrank
     * @param key
     * @param member
     * @return
     */
    public static Long zRank(String key, String member) {
        ZSetOperations<String, String> opsForZSet = redisTemplate.opsForZSet();
        return opsForZSet.rank(key, member);
    }


    /**
     * zReverseRank
     * @param key
     * @param member
     * @return
     */
    public static Long zReverseRank(String key, String member) {
        ZSetOperations<String, String> opsForZSet = redisTemplate.opsForZSet();
        return opsForZSet.reverseRank(key, member);
    }

    /**
     * zadd
     * @param key
     * @param member
     * @param score
     * @param ttl
     */
    public static void zAdd(String key, String member, long score, long ttl, TimeUnit timeUnit) {
        ZSetOperations<String, String> opsForZSet = redisTemplate.opsForZSet();
        opsForZSet.add(key, member, score);
        expire(key, ttl, timeUnit);
    }


    /**
     * zincr
     * @param key
     * @param member
     * @param ttl
     * @return
     */
    public static Long zIncr(String key, String member, long ttl, TimeUnit timeUnit) {
        ZSetOperations<String, String> opsForZSet = redisTemplate.opsForZSet();
        Double score = opsForZSet.incrementScore(key, member, 1);
        expire(key, ttl, timeUnit);
        if (score == null) {
            return null;
        }
        return score.longValue();
    }


    /**
     * expire
     * @param key
     * @param ttl
     * @return
     */
    public static Boolean expire(String key, long ttl, TimeUnit timeUnit) {
        if (ttl > 0) {
            return redisTemplate.expire(key, ttl, timeUnit);
        }
        return false;
    }


    /**
     * zrange
     * @param key
     */
    public static Set<ZSetOperations.TypedTuple<String>> zRange(String key, long start, long end) {
        ZSetOperations<String, String> opsForZSet = redisTemplate.opsForZSet();
        return opsForZSet.rangeWithScores(key, start, end);
    }


    /**
     * zReverseRange
     * @param key
     */
    public static Set<ZSetOperations.TypedTuple<String>> zReverseRange(String key, long start, long end) {
        ZSetOperations<String, String> opsForZSet = redisTemplate.opsForZSet();
        return opsForZSet.reverseRangeWithScores(key, start, end);
    }


    /**
     * multiple get,using pipeline
     * @param keys
     * @param <T>
     * @return
     */
    public static <T> List<T> multiGet(List<String> keys, Class<T> classOfT) {
        List<Object> jsonList = redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            for (String key : keys) {
                connection.get(key.getBytes());
            }
            return null;
        }, redisTemplate.getValueSerializer());
        List<T> resultList = new ArrayList<>();
        if (CollectionUtils.isEmpty(jsonList)) {
            return resultList;
        }
        for (Object json : jsonList) {
            resultList.add(JSON.parseObject((String) json, classOfT));
        }
        return resultList;
    }


    /**
     * multiple set Object,using pipeline
     * @param map
     * @param ttl
     * @param timeUnit
     */
    public static void multiSetObj(Map<String, ? extends Object> map, long ttl, TimeUnit timeUnit) {
        redisTemplate.executePipelined((RedisCallback<Void>) connection -> {
            for (Map.Entry<String, ?> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (ttl > 0) {
                    connection.set(key.getBytes(), JSON.toJSONString(value).getBytes(), Expiration.from(ttl, timeUnit), RedisStringCommands.SetOption.UPSERT);
                } else {
                    connection.set(key.getBytes(), JSON.toJSONString(value).getBytes());
                }
            }
            log.info("multiSetObj count:{}", map.size());
            return null;
        }, redisTemplate.getValueSerializer());
    }


    /**
     * multiple set Object,using pipeline
     * @param map
     */
    public static void multiSetObj(Map<String, ? extends Object> map) {
        multiSetObj(map, 0, null);
    }

    /**
     * multiple set String,using pipeline
     * @param map
     * @param ttl
     * @param timeUnit
     */
    public static void multiSetStr(Map<String, String> map, long ttl, TimeUnit timeUnit) {
        redisTemplate.executePipelined((RedisCallback<Void>) connection -> {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (ttl > 0) {
                    connection.set(key.getBytes(), value.getBytes(), Expiration.from(ttl, timeUnit), RedisStringCommands.SetOption.UPSERT);
                } else {
                    connection.set(key.getBytes(), value.getBytes());
                }

            }
            log.info("multiSetStr count:{}", map.size());
            return null;
        }, redisTemplate.getValueSerializer());
    }


    /**
     * multiple set String,using pipeline
     * @param map
     */
    public static void multiSetStr(Map<String, String> map) {
        multiSetStr(map, 0, null);
    }


    /**
     * get length of zset
     * @param key
     * @return
     */
    public static Long zCard(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }


    public static Long incrBy(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }


    /**
     * increment with expire
     * @param key
     * @return
     */
    public static Long incrAndExpire(String key, long ttl, TimeUnit timeUnit) {
        Long incr = redisTemplate.opsForValue().increment(key);
        expire(key, ttl, timeUnit);
        return incr;
    }


    /**
     * sismember
     * @param setKey
     * @param member
     */
    public static Boolean sIsMember(String setKey, String member) {
        return redisTemplate.opsForSet().isMember(setKey, member);
    }


}
