package com.dietmall.config;

import java.security.Principal;
import java.util.Collections;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import com.dietmall.group.repository.GroupMemberRepository;

@Component
public class WebSocketAuthInterceptor
        implements ChannelInterceptor {


    private final JwtDecoder jwtDecoder;

    private final GroupMemberRepository groupMemberRepository;


    public WebSocketAuthInterceptor(
            JwtDecoder jwtDecoder,
            GroupMemberRepository groupMemberRepository) {

        this.jwtDecoder = jwtDecoder;
        this.groupMemberRepository = groupMemberRepository;
    }



    @Override
    public Message<?> preSend(
            Message<?> message,
            MessageChannel channel) {


        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(
                        message,
                        StompHeaderAccessor.class
                );


        if (accessor == null) {
            return message;
        }



        // WebSocket 연결 시 JWT 인증
        if (StompCommand.CONNECT.equals(
                accessor.getCommand())) {


            String authorization =
                    accessor.getFirstNativeHeader(
                            "Authorization"
                    );


            if (authorization == null
                    || !authorization.startsWith("Bearer ")) {

                throw new IllegalArgumentException(
                        "WebSocket 인증 토큰이 필요합니다."
                );
            }


            String token =
                    authorization.substring(7).trim();



            Jwt jwt =
                    jwtDecoder.decode(token);



            String userId =
                    jwt.getSubject();



            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            Collections.emptyList()
                    );


            accessor.setUser(authentication);
        }




        // 그룹 채팅 구독 권한 검사
        if (StompCommand.SUBSCRIBE.equals(
                accessor.getCommand())) {


            validateGroupSubscription(accessor);
        }


        return message;
    }




    private void validateGroupSubscription(
            StompHeaderAccessor accessor) {


        String destination =
                accessor.getDestination();



        if (destination == null) {
            return;
        }



        String prefix =
                "/topic/groups/";

        String suffix =
                "/chat";



        if (!destination.startsWith(prefix)
                || !destination.endsWith(suffix)) {

            return;
        }



        String groupIdText =
                destination.substring(
                        prefix.length(),
                        destination.length() - suffix.length()
                );



        Long groupId =
                Long.valueOf(groupIdText);



        Principal principal =
                accessor.getUser();



        if (principal == null) {

            throw new IllegalArgumentException(
                    "로그인 정보가 없습니다."
            );
        }



        Long userId =
                Long.valueOf(
                        principal.getName()
                );



        boolean isMember =
                groupMemberRepository
                        .existsByGroupIdAndUserId(
                                groupId,
                                userId
                        );



        if (!isMember) {

            throw new IllegalArgumentException(
                    "가입한 그룹만 채팅 가능합니다."
            );
        }

    }

}