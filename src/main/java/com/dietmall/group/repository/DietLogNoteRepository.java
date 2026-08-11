package com.dietmall.group.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dietmall.group.entity.DietLogNote;

public interface DietLogNoteRepository
        extends JpaRepository<DietLogNote, Long> {

    List<DietLogNote> findAllByLogIdOrderByCreatedAtAsc(
            Long logId
    );

    Optional<DietLogNote> findByLogIdAndAuthorId(
            Long logId,
            Long authorId
    );

    boolean existsByLogIdAndAuthorId(
            Long logId,
            Long authorId
    );
}