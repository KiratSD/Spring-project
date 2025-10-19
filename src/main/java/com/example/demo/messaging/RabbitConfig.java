package com.example.demo.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String POLLS_EXCHANGE = "polls";

    @Bean TopicExchange pollsExchange() { return new TopicExchange(POLLS_EXCHANGE, true, false); }

    @Bean Queue votesQueue() { return new AnonymousQueue(); }

    @Bean Binding bindVotes(Queue votesQueue, TopicExchange pollsExchange) {
        return BindingBuilder.bind(votesQueue).to(pollsExchange).with("poll.*.vote");
    }

    @Bean Jackson2JsonMessageConverter messageConverter() { return new Jackson2JsonMessageConverter(); }

    @Bean RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter conv) {
        RabbitTemplate rt = new RabbitTemplate(cf);
        rt.setMessageConverter(conv);
        return rt;
    }
}
