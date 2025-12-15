package com.example.lab04.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE = "grades_queue";
    public static final String EXCHANGE = "grades_exchange";
    public static final String ROUTING_KEY = "grades_routingKey";

    public static final String DLQ_QUEUE = "grades_queue.dlq";
    public static final String DLQ_EXCHANGE = "grades_exchange.dlq";
    public static final String DLQ_ROUTING_KEY = "grades_dlk";

    @Bean
    public Queue gradesQueue() { // Renamed from queue()
        return QueueBuilder.durable(QUEUE)
                .withArgument("x-dead-letter-exchange", DLQ_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public TopicExchange gradesExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding gradesBinding(Queue gradesQueue, TopicExchange gradesExchange) {
        return BindingBuilder.bind(gradesQueue).to(gradesExchange).with(ROUTING_KEY);
    }

    @Bean public Queue gradesDlq() { return new Queue(DLQ_QUEUE); }
    @Bean public TopicExchange gradesDlqExchange() { return new TopicExchange(DLQ_EXCHANGE); }
    @Bean public Binding gradesDlqBinding() {
        return BindingBuilder.bind(gradesDlq()).to(gradesDlqExchange()).with(DLQ_ROUTING_KEY);
    }

    @Bean
    public Queue verificationRequestQueue() {
        return new Queue("verification_queue");
    }

    @Bean
    public Queue verificationResultQueue() {
        return new Queue("result_queue");
    }

    @Bean
    public TopicExchange sagaExchange() {
        return new TopicExchange("saga_exchange");
    }

    @Bean
    public Binding requestBinding(Queue verificationRequestQueue, TopicExchange sagaExchange) {
        return BindingBuilder.bind(verificationRequestQueue)
                .to(sagaExchange)
                .with("verification.request");
    }

    @Bean
    public Binding resultBinding(Queue verificationResultQueue, TopicExchange sagaExchange) {
        return BindingBuilder.bind(verificationResultQueue)
                .to(sagaExchange)
                .with("verification.result");
    }


    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}