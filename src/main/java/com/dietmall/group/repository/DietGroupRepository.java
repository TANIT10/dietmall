package com.dietmall.group.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dietmall.group.entity.DietGroup;
import com.dietmall.group.entity.GroupVisibility;

public interface DietGroupRepository
        extends JpaRepository<DietGroup, Long> {

    Optional<DietGroup> findByInviteCode(
            String inviteCode
    );

    List<DietGroup> findAllByVisibilityOrderByCreatedAtDesc(
            GroupVisibility visibility
    );
}