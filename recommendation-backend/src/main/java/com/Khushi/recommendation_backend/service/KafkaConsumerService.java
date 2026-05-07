package com.Khushi.recommendation_backend.service;

import com.Khushi.recommendation_backend.dto.InteractionRequestDTO;
import com.Khushi.recommendation_backend.dto.InteractionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final InteractionService interactionService;

    @RetryableTopic(
            attempts = "3",
            dltTopicSuffix = "-dlt"
    )
    @KafkaListener(
            topics = "interaction-events",
            groupId = "recommendation-group"
    )
    public void consumeInteraction(
            InteractionRequestDTO requestDTO){

        System.out.println(
                "Consuming event for user "
                        + requestDTO.getUserId());

//        throw new RuntimeException("Kafka retry test");
        interactionService.saveInteraction(requestDTO);
    }

    @DltHandler
    public void handleDLT(
            InteractionRequestDTO requestDTO){

        System.out.println(
                "SENT TO DLT -> user "
                        + requestDTO.getUserId());
    }
}