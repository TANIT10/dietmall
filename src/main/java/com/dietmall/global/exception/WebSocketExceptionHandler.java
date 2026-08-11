package com.dietmall.global.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class WebSocketExceptionHandler {


    @MessageExceptionHandler(IllegalArgumentException.class)
    @SendToUser("/queue/errors")
    public Map<String, Object> handleIllegalArgumentException(
            IllegalArgumentException e) {

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "code",
                "BAD_REQUEST"
        );

        response.put(
                "message",
                e.getMessage()
        );

        return response;
    }


    @MessageExceptionHandler(IllegalStateException.class)
    @SendToUser("/queue/errors")
    public Map<String, Object> handleIllegalStateException(
            IllegalStateException e) {

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "code",
                "WEBSOCKET_ERROR"
        );

        response.put(
                "message",
                e.getMessage()
        );

        return response;
    }
}