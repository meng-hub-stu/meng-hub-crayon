package com.crayon.common.rabbitmq.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Mengdl
 * @date 2025/03/18
 */
@Component
@RequiredArgsConstructor
public class SendMessage {

    private final RabbitTemplate rabbitTemplate;

    public void sendOrder(Object order) {
        String msgId = UUID.randomUUID().toString();
//        messageLogMapper.insert(new MessageLog(msgId, order, MessageStatus.SENDING));
        rabbitTemplate.convertAndSend(
                "order-exchange",
                "order.create",
                order,
                message -> {
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    message.getMessageProperties().setMessageId(msgId);
                    return message;
                },
                new CorrelationData(msgId)
        );
    }

}
