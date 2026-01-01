package com.lumine.notification;

import com.lumine.notification.event.OrderPlaceEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerConfig {

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlaceEvent orderPlaceEvent){
        log.info("Received Notification for Order - {}", orderPlaceEvent.getOrderNumber());
    }

}