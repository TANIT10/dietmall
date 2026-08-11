package com.dietmall.group.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dietmall.group.entity.GroupChatMessage;

public interface GroupChatMessageRepository
        extends JpaRepository<GroupChatMessage, Long> {

    Page<GroupChatMessage> findAllByGroupId(
            Long groupId,
            Pageable pageable
    );
}