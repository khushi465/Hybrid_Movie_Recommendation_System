package com.Khushi.recommendation_backend.service;

import com.Khushi.recommendation_backend.dto.InteractionRequestDTO;
import com.Khushi.recommendation_backend.dto.InteractionResponseDTO;
import com.Khushi.recommendation_backend.model.Interaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, InteractionRequestDTO> kafkaTemplate;
    private static final String TOPIC = "interaction-events";
//    private static final Logger log;
    public void sendInteraction(InteractionRequestDTO request){
        System.out.println("Event sent to kafka for user "+request.getUserId());
       kafkaTemplate.send(TOPIC, request);
        //kafka currently used for event processing/ future async processing
    }
}
