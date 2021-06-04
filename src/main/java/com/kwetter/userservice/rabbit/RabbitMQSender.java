package com.kwetter.userservice.rabbit;

import com.kwetter.userservice.entity.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    private String exchange = RabbitMQConfig.topicExchangeName;

    private String routingKey = RabbitMQConfig.routingKey;

    public void send(String username) {
        rabbitTemplate.convertAndSend(exchange, routingKey, username);
        System.out.println("Send msg = " + username);
    }
}
