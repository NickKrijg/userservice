package com.kwetter.userservice.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String TOPIC_EXCHANGE_NAME = "kwetter-rabbit";

    public static final String QUEUE_NAME = "forget";

    public static final String ROUTING_KEY = "forget.user.#";

    @Bean
    Queue queue(){
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    TopicExchange exchange(){
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final var template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

}
