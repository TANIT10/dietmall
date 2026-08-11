package com.dietmall.group.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.dietmall.group.dto.GroupChatMessageRequest;
import com.dietmall.group.dto.GroupChatMessageResponse;
import com.dietmall.group.dto.GroupChatSocketRequest;
import com.dietmall.group.service.GroupChatService;


@Controller
public class GroupChatSocketController {


    private final GroupChatService groupChatService;



    public GroupChatSocketController(
            GroupChatService groupChatService) {

        this.groupChatService =
                groupChatService;
    }




    @MessageMapping("/groups/{groupId}/chat")
    @SendTo("/topic/groups/{groupId}/chat")
    public GroupChatMessageResponse sendMessage(
            @DestinationVariable Long groupId,
            GroupChatSocketRequest request,
            Principal principal) {



        if (principal == null) {

            throw new IllegalStateException(
                    "사용자 정보가 없습니다."
            );
        }



        Long userId =
                Long.valueOf(
                        principal.getName()
                );



        GroupChatMessageRequest messageRequest =
                new GroupChatMessageRequest();



        messageRequest.setContent(
                request.getContent()
        );



        return groupChatService.sendMessage(
                userId,
                groupId,
                messageRequest
        );

    }

}