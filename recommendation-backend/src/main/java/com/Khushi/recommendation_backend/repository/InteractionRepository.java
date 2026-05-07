package com.Khushi.recommendation_backend.repository;

import com.Khushi.recommendation_backend.model.Interaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {
    //get user interactions(for ml)
    List<Interaction> findByUserId(Long userId);
    //count interactions(for dynamic weighting)
    int countByUserId(Long userId);
    @Query("SELECT i FROM Interaction i ORDER BY i.timestamp DESC")
    List<Interaction> findRecentInteractions(Pageable pageable);
}
