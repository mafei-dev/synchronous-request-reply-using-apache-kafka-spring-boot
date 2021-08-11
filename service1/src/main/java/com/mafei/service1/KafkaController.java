package com.mafei.service1;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class KafkaController {
    private final ReplyingKafkaTemplate<String, Student, ResultResponse> replyingKafkaTemplate;
    @Value("${kafka.reuest.topic}")
    private String requestTopic;

    @PostMapping("/get-result")
    public ResponseEntity<ResultResponse> getObject(@RequestBody Student student)
            throws InterruptedException, ExecutionException {
        ProducerRecord<String, Student> record = new ProducerRecord<>(requestTopic, null, student.getRegistrationNumber(), student);
        RequestReplyFuture<String, Student, ResultResponse> future = replyingKafkaTemplate.sendAndReceive(record);
        ConsumerRecord<String, ResultResponse> response = future.get();
        return new ResponseEntity<>(response.value(), HttpStatus.OK);
    }
}