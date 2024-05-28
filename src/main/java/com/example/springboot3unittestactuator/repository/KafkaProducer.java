package com.example.springboot3unittestactuator.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaProducer {

    public String sendPayload(String msg) {
        log.info("send payload {} to kafka message queue.", msg);
        return "success";
    }
}
