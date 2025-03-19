package com.crayon.common.rabbitmq.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Mengdl
 * @date 2025/03/14
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MqConfirmCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

//    private final MessageLogMapper messageLogMapper;

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("✅ 消息到达 Exchange，ID：{}", correlationData.getId());
//            messageLogMapper.updateStatus(correlationData.getId(), MessageStatus.SENT);
        } else {
            log.error("❌ 消息投递失败，ID：{}，原因：{}", correlationData.getId(), cause);
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.error("🚨 消息路由失败！交换机：{}，路由键：{}，消息：{}", returned.getExchange(), returned.getRoutingKey(), new String(returned.getMessage().getBody()));
    }

}
