package com.mafei.service1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;
    @Value("${kafka.reply.topic}")
    private String replyTopic;
    @Bean
    public ReplyingKafkaTemplate<String, Student, ResultResponse> replyingKafkaTemplate(ProducerFactory<String, Student> pf,
                                                                                        ConcurrentKafkaListenerContainerFactory<String, ResultResponse> factory) {
        ConcurrentMessageListenerContainer<String, ResultResponse> replyContainer = factory.createContainer(replyTopic);
        replyContainer.getContainerProperties().setMissingTopicsFatal(false);
        replyContainer.getContainerProperties().setGroupId(groupId);
        return new ReplyingKafkaTemplate<>(pf, replyContainer);
    }
    @Bean
    public KafkaTemplate<String, ResultResponse> replyTemplate(ProducerFactory<String, ResultResponse> pf,
                                                               ConcurrentKafkaListenerContainerFactory<String, ResultResponse> factory) {
        KafkaTemplate<String, ResultResponse> kafkaTemplate = new KafkaTemplate<>(pf);
        factory.getContainerProperties().setMissingTopicsFatal(false);
        factory.setReplyTemplate(kafkaTemplate);
        return kafkaTemplate;
    }
}