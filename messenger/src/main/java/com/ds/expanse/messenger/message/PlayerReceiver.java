package com.ds.expanse.messenger.message;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class PlayerReceiver {
    @Bean
    Queue queue() {
        return new Queue("PlayerQ", false);
    }

    @RabbitListener(queues="PlayerQ")
    public void processMessage(String email) {
        System.out.println("email " + email);
    }
}
