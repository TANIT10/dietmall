package com.dietmall.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.notification.entity.PushDevice;
import com.dietmall.notification.repository.PushDeviceRepository;
import com.google.firebase.messaging.FirebaseMessagingException;

@Service
public class NotificationSendService {

    private final PushDeviceRepository pushDeviceRepository;
    private final FirebasePushService firebasePushService;


    public NotificationSendService(
            PushDeviceRepository pushDeviceRepository,
            FirebasePushService firebasePushService) {

        this.pushDeviceRepository = pushDeviceRepository;
        this.firebasePushService = firebasePushService;
    }


    @Transactional(readOnly = true)
    public void sendToUser(
            Long userId,
            String title,
            String body) {

        List<PushDevice> devices =
                pushDeviceRepository
                        .findAllByUserIdAndEnabledTrue(userId);


        for (PushDevice device : devices) {

            try {

                firebasePushService.send(
                        device.getFid(),
                        title,
                        body
                );

            } catch (FirebaseMessagingException e) {

                System.err.println(
                        "푸시 전송 실패 - deviceId: "
                                + device.getId()
                                + ", error: "
                                + e.getMessage()
                );
            }
        }
    }
}