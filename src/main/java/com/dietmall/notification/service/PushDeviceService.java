package com.dietmall.notification.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.notification.dto.PushDeviceRegisterRequest;
import com.dietmall.notification.entity.PushDevice;
import com.dietmall.notification.repository.PushDeviceRepository;
import com.dietmall.user.entity.User;
import com.dietmall.user.repository.UserRepository;

@Service
public class PushDeviceService {

    private final PushDeviceRepository pushDeviceRepository;
    private final UserRepository userRepository;


    public PushDeviceService(
            PushDeviceRepository pushDeviceRepository,
            UserRepository userRepository) {

        this.pushDeviceRepository = pushDeviceRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public void register(
            Long userId,
            PushDeviceRegisterRequest request) {

        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "사용자를 찾을 수 없습니다."
                                )
                        );


        PushDevice existingDevice =
                pushDeviceRepository
                        .findByFid(
                                request.getFid()
                        )
                        .orElse(null);


        if (existingDevice != null) {

            existingDevice.update(
                    user,
                    request.getFid(),
                    request.getDeviceType()
            );

            return;
        }


        PushDevice newDevice =
                new PushDevice(
                        user,
                        request.getFid(),
                        request.getDeviceType()
                );


        pushDeviceRepository.save(newDevice);
    }


    @Transactional
    public void unregister(
            Long userId,
            String fid) {

        PushDevice device =
                pushDeviceRepository
                        .findByFid(fid)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "등록된 기기를 찾을 수 없습니다."
                                )
                        );


        if (!device.getUser().getId().equals(userId)) {

            throw new IllegalArgumentException(
                    "현재 사용자의 기기가 아닙니다."
            );
        }


        device.disable();
    }
}