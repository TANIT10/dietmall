package com.dietmall.group.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.group.dto.GroupChatMessageRequest;
import com.dietmall.group.dto.GroupChatMessageResponse;
import com.dietmall.group.dto.GroupChatPageResponse;
import com.dietmall.group.entity.DietGroup;
import com.dietmall.group.entity.GroupChatMessage;
import com.dietmall.group.entity.GroupMember;
import com.dietmall.group.repository.DietGroupRepository;
import com.dietmall.group.repository.GroupChatMessageRepository;
import com.dietmall.group.repository.GroupMemberRepository;

@Service
public class GroupChatService {

    private final GroupChatMessageRepository groupChatMessageRepository;
    private final DietGroupRepository dietGroupRepository;
    private final GroupMemberRepository groupMemberRepository;


    public GroupChatService(
            GroupChatMessageRepository groupChatMessageRepository,
            DietGroupRepository dietGroupRepository,
            GroupMemberRepository groupMemberRepository) {

        this.groupChatMessageRepository =
                groupChatMessageRepository;

        this.dietGroupRepository =
                dietGroupRepository;

        this.groupMemberRepository =
                groupMemberRepository;
    }


    @Transactional
    public GroupChatMessageResponse sendMessage(
            Long userId,
            Long groupId,
            GroupChatMessageRequest request) {

        DietGroup group =
                getGroup(
                        groupId
                );


        GroupMember member =
                getGroupMember(
                        userId,
                        groupId
                );


        GroupChatMessage message =
                new GroupChatMessage(
                        group,
                        member,
                        normalizeContent(
                                request.getContent()
                        )
                );


        GroupChatMessage savedMessage =
                groupChatMessageRepository.save(
                        message
                );


        return toResponse(
                savedMessage
        );
    }


    @Transactional(readOnly = true)
    public GroupChatPageResponse getMessages(
            Long userId,
            Long groupId,
            int page,
            int size) {

        getGroup(
                groupId
        );


        getGroupMember(
                userId,
                groupId
        );


        int safePage =
                Math.max(
                        page,
                        0
                );


        int safeSize =
                Math.min(
                        Math.max(
                                size,
                                1
                        ),
                        100
                );


        Pageable pageable =
                PageRequest.of(
                        safePage,
                        safeSize,
                        Sort.by(
                                Sort.Direction.DESC,
                                "createdAt"
                        )
                );


        Page<GroupChatMessage> messagePage =
                groupChatMessageRepository
                        .findAllByGroupId(
                                groupId,
                                pageable
                        );


        List<GroupChatMessageResponse> messages =
                messagePage
                        .getContent()
                        .stream()
                        .sorted(
                                Comparator.comparing(
                                        GroupChatMessage::getCreatedAt
                                )
                        )
                        .map(
                                this::toResponse
                        )
                        .toList();


        return new GroupChatPageResponse(
                messages,
                messagePage.getNumber(),
                messagePage.getSize(),
                messagePage.getTotalElements(),
                messagePage.getTotalPages(),
                messagePage.isLast()
        );
    }


    private DietGroup getGroup(
            Long groupId) {

        return dietGroupRepository
                .findById(
                        groupId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "그룹을 찾을 수 없습니다."
                        )
                );
    }


    private GroupMember getGroupMember(
            Long userId,
            Long groupId) {

        return groupMemberRepository
                .findByGroupIdAndUserId(
                        groupId,
                        userId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "가입한 그룹이 아닙니다."
                        )
                );
    }


    private String normalizeContent(
            String content) {

        String trimmed =
                content.trim();


        if (trimmed.isBlank()) {

            throw new IllegalArgumentException(
                    "채팅 메시지는 필수입니다."
            );
        }


        if (trimmed.length() > 500) {

            throw new IllegalArgumentException(
                    "채팅 메시지는 500자 이하여야 합니다."
            );
        }


        return trimmed;
    }


    private GroupChatMessageResponse toResponse(
            GroupChatMessage message) {

        GroupMember author =
                message.getAuthor();


        return new GroupChatMessageResponse(
                message.getId(),
                message.getGroup().getId(),
                author.getId(),
                author.getGroupNickname(),
                buildProfileImageUrl(
                        author
                ),
                message.getContent(),
                message.getCreatedAt()
        );
    }


    private String buildProfileImageUrl(
            GroupMember member) {

        if (member.getProfileImageUrl() == null
                || member.getProfileImageUrl().isBlank()) {

            return null;
        }


        return "/api/groups/"
                + member.getGroup().getId()
                + "/members/"
                + member.getId()
                + "/profile/image";
    }
}