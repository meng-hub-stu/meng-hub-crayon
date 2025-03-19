//package com.crayon.common.rabbitmq.consumer;
//
//import com.rabbitmq.client.AMQP;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.support.AmqpHeaders;
//import org.springframework.core.annotation.Order;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * @author Mengdl
// * @date 2025/03/18
// */
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class ReceiveMessage {
//
//    private final OrderService orderService;
//
//    private final RedisTemplate<String, String> redisTemplate;
//
//    @RabbitListener(queues = "order.queue")
//    public void handleOrder(Order order, AMQP.Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
//        try {
//            String redisKey = "order:" + order.getOrderNo();
//            if (redisTemplate.opsForValue().setIfAbsent(redisKey, "processing", 30, TimeUnit.MINUTES)) {
//                orderService.process(order);
//                redisTemplate.delete(redisKey);
//                channel.basicAck(tag, false);
//                log.info("🎉 订单处理成功：{}", order.getOrderNo());
//            } else {
//                log.warn("⚠️ 订单正在处理中，直接ACK：{}", order.getOrderNo());
//                channel.basicAck(tag, false);
//            }
//        } catch (Exception e) {
//            log.error("❌ 订单处理失败：{}", order.getOrderNo(), e);
//            channel.basicNack(tag, false, true);
//        }
//    }
//
//}
