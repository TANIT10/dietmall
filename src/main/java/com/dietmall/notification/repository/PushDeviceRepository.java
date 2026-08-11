package com.dietmall.notification.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dietmall.notification.entity.PushDevice;

public interface PushDeviceRepository
        extends JpaRepository<PushDevice, Long> {

    Optional<PushDevice> findByFid(String fid);

    List<PushDevice> findAllByUserIdAndEnabledTrue(Long userId);
}