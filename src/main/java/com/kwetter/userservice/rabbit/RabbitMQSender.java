package com.kwetter.userservice.rabbit;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class RabbitMQSender {
    private static final Logger LOGGER = Logger.getLogger(RabbitMQSender.class.getName());;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    private String exchange = RabbitMQConfig.TOPIC_EXCHANGE_NAME;

    private String routingKey = RabbitMQConfig.ROUTING_KEY;

    public void send(String username) {
        rabbitTemplate.convertAndSend(exchange, routingKey, username);
        LOGGER.log(Level.FINE, "Send msg = " + username);
    }
}
