package com.dietmall.user.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.user.dto.WeightRecordRequest;
import com.dietmall.user.dto.WeightRecordResponse;
import com.dietmall.user.entity.User;
import com.dietmall.user.entity.WeightRecord;
import com.dietmall.user.repository.UserRepository;
import com.dietmall.user.repository.WeightRecordRepository;

@Service
public class WeightService {

    private final UserRepository userRepository;
    private final WeightRecordRepository weightRecordRepository;

    public WeightService(
            UserRepository userRepository,
            WeightRecordRepository weightRecordRepository) {

        this.userRepository = userRepository;
        this.weightRecordRepository = weightRecordRepository;
    }

    // 새 체중 기록 추가
    @Transactional
    public WeightRecordResponse addWeight(
            Long userId,
            WeightRecordRequest request) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "사용자를 찾을 수 없습니다."
                        )
                );

        WeightRecord weightRecord = new WeightRecord(
                user,
                request.getWeight(),
                Instant.now()
        );

        WeightRecord savedWeightRecord =
                weightRecordRepository.save(weightRecord);

        return WeightRecordResponse.from(savedWeightRecord);
    }

    // 가장 최근 체중 조회
    @Transactional(readOnly = true)
    public WeightRecordResponse getCurrentWeight(Long userId) {

        WeightRecord weightRecord =
                weightRecordRepository
                        .findTopByUserIdOrderByRecordedAtDesc(userId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "체중 기록을 찾을 수 없습니다."
                                )
                        );

        return WeightRecordResponse.from(weightRecord);
    }

    // 체중 기록 전체 조회
    @Transactional(readOnly = true)
    public List<WeightRecordResponse> getWeightHistory(Long userId) {

        return weightRecordRepository
                .findAllByUserIdOrderByRecordedAtDesc(userId)
                .stream()
                .map(WeightRecordResponse::from)
                .toList();
    }
}