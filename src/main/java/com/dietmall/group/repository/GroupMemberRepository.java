package com.dietmall.group.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dietmall.group.entity.GroupMember;

public interface GroupMemberRepository
        extends JpaRepository<GroupMember, Long> {

    Optional<GroupMember> findByGroupIdAndUserId(
            Long groupId,
            Long userId
    );

    Optional<GroupMember> findByGroupIdAndGroupNickname(
            Long groupId,
            String groupNickname
    );

    boolean existsByGroupIdAndUserId(
            Long groupId,
            Long userId
    );

    boolean existsByGroupIdAndGroupNickname(
            Long groupId,
            String groupNickname
    );

    List<GroupMember> findAllByGroupIdOrderByJoinedAtAsc(
            Long groupId
    );

    List<GroupMember> findAllByUserIdOrderByJoinedAtDesc(
            Long userId
    );
}