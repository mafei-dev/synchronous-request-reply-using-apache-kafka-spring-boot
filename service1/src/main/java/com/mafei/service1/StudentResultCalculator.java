package com.mafei.service1;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class StudentResultCalculator {
    @KafkaListener(topics = "${kafka.reuest.topic}")
    @SendTo
    public ResultResponse handle(Student student) {
        System.out.println("Calculating Result...1");
        double total = ThreadLocalRandom.current().nextDouble(2.5, 9.9);
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setName(student.getName());
        resultResponse.setResult((total > 3.5) ? "Pass" : "Fail");
        resultResponse.setPercentage(String.valueOf(total * 10).substring(0, 4) + "%");
        return resultResponse;
    }

}