package top.lijieyao.datasync;

import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import top.lijieyao.datasync.utils.RedisUtil;

import java.util.concurrent.CountDownLatch;

@SpringBootTest
class DataSyncApplicationTests {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    public void contextLoads() throws Exception {
        CountDownLatch downLatch = new CountDownLatch(2);

        new Thread(() -> {
            RedisUtil.switchDatabase(2);
            RedisUtil.setObj("Thread-A", "AAAAAAAAAAA");
            downLatch.countDown();
        }, "Thread-A").start();


        new Thread(() -> {
            // RedisUtil.switchDatabase(1);
            RedisUtil.setObj("Thread-B", "BBBBBBBBB");
            downLatch.countDown();
        }, "Thread-A").start();

        downLatch.await();
        System.out.println("执行完毕");
    }

    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(512));
    }

}
