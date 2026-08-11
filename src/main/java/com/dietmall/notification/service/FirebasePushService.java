package com.dietmall.notification.service;

import org.springframework.stereotype.Service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

@Service
public class FirebasePushService {

    private final FirebaseMessaging firebaseMessaging;


    public FirebasePushService(
            FirebaseApp firebaseApp) {

        this.firebaseMessaging =
                FirebaseMessaging.getInstance(firebaseApp);
    }


    public String send(
            String fid,
            String title,
            String body)
            throws FirebaseMessagingException {

        Notification notification =
                Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build();


        Message message =
                Message.builder()
                        .setFid(fid)
                        .setNotification(notification)
                        .build();


        return firebaseMessaging.send(message);
    }
}