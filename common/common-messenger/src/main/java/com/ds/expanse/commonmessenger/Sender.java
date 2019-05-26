package com.ds.expanse.commonmessenger;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class Sender {
    @Autowired
    private RabbitMessagingTemplate template;

    @Bean
    Queue queue() {
        return new Queue("PlayerQ", false);
    }

    public void send(String message) {
        System.out.println("sending message " + message);
        template.convertAndSend("PlayerQ", message);
    }
}
