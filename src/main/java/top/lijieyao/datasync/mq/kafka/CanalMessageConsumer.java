package top.lijieyao.datasync.mq.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import top.lijieyao.datasync.constant.IntConstantPool;
import top.lijieyao.datasync.constant.KafkaConstantPool;
import top.lijieyao.datasync.constant.StringConstantPool;
import top.lijieyao.datasync.mq.kafka.entity.CanalMessage;
import top.lijieyao.datasync.utils.RedisUtil;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Description: Canal消息消费者
 * @Author: LiJieYao
 * @Date: 2022/4/23 15:58
 */
@Slf4j
@Component
public class CanalMessageConsumer {

    @KafkaListener(topics = {KafkaConstantPool.HK_DATABASE_TOPIC}, groupId = KafkaConstantPool.KAFKA_DEFAULT_GROUP)
    public void hkDatabaseMessage(ConsumerRecord<String, String> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("CanalMessageConsumer..检测到数据库数据变更..record:{}", record);
        CanalMessage canalMessage = Optional.ofNullable(record.value())
                .map(str -> JSON.parseObject(str, CanalMessage.class))
                .orElse(null);
        // 检查消息情况
        if (canalMessage == null || CollectionUtils.isEmpty(canalMessage.getData())) {
            log.info("CanalMessageConsumer..消息异常..canalMessage:{}", canalMessage);
            ack.acknowledge();
            return;
        }

        String pkName = Optional.ofNullable(canalMessage.getPkNames())
                .map(pkList -> pkList.get(IntConstantPool.COLLECT_OF_FIRST))
                .orElse(StringConstantPool.BLANK);
        if (StringUtils.isBlank(pkName)) {
            log.info("CanalMessageConsumer..消息异常..数据表主键为空..canalMessage:{}", canalMessage);
            ack.acknowledge();
            return;
        }

        // 获取主键并组装信息
        Map<String, JSONObject> dataMap = canalMessage.getData().stream()
                .map(JSON::toJSONString)
                .map(JSON::parseObject)
                .collect(Collectors.toMap(
                        a -> String.format("%s:%s", canalMessage.getTable(), a.getLong(pkName)),
                        v -> v,
                        (v1, v2) -> v2));


        if (MapUtils.isEmpty(dataMap)) {
            log.info("CanalMessageConsumer..消息格式异常..数据表主键异常..canalMessage:{}", canalMessage);
            ack.acknowledge();
            return;
        }

        try {
            RedisUtil.multiSetObj(dataMap);
            ack.acknowledge();
        } catch (Exception e) {
            log.warn("CanalMessageConsumer..redis数据增量更新异常..dataMap:{}..canalMessage:{}", dataMap, canalMessage);
            ack.nack(5000);
        }
    }


}
