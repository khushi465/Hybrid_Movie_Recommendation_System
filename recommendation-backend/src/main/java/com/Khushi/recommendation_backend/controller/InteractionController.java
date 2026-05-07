package com.Khushi.recommendation_backend.controller;

import com.Khushi.recommendation_backend.dto.InteractionRequestDTO;
import com.Khushi.recommendation_backend.dto.InteractionResponseDTO;
import com.Khushi.recommendation_backend.dto.MovieResponseDTO;
import com.Khushi.recommendation_backend.model.Interaction;
import com.Khushi.recommendation_backend.service.InteractionService;
import com.Khushi.recommendation_backend.service.KafkaProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/interactions")
@RequiredArgsConstructor
public class InteractionController {
    private final InteractionService interactionService;
    private final KafkaProducerService kafkaProducerService;

    @PostMapping
    public String addInteraction(@Valid @RequestBody InteractionRequestDTO requestDTO){
        kafkaProducerService.sendInteraction(requestDTO);
        return "Event sent to kafka";
//        return interactionService.saveInteraction(requestDTO);
//        return "Event sent to Kafka";
    }
    @GetMapping
    public List<InteractionResponseDTO> getAllInteractions() {

        return interactionService.getAllInteractions();
    }

    @GetMapping("/user/{userId}")
    public List<InteractionResponseDTO> getByUser(@PathVariable Long userId) {
        return interactionService.getByUser(userId);
    }
    @GetMapping("/{id}")
    public InteractionResponseDTO getInteraction(@PathVariable Long id){
        return interactionService.getInteraction(id);
    }

}
