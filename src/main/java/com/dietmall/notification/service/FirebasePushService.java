package com.dietmall.notification.service;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
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
            ObjectProvider<FirebaseApp> firebaseAppProvider,
            @Value("${firebase.enabled:true}")
            boolean firebaseEnabled) {

        FirebaseApp firebaseApp =
                firebaseAppProvider.getIfAvailable();

        if (firebaseEnabled && firebaseApp != null) {
            this.firebaseMessaging =
                    FirebaseMessaging.getInstance(firebaseApp);
        } else {
            this.firebaseMessaging = null;
        }
    }

    public String send(
            String fid,
            String title,
            String body)
            throws FirebaseMessagingException {

        if (firebaseMessaging == null) {
            return "FIREBASE_DISABLED";
        }

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