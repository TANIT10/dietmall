package com.dietmall.user.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dietmall.user.dto.ExerciseRecordRequest;
import com.dietmall.user.dto.ExerciseRecordResponse;
import com.dietmall.user.dto.ExerciseTodaySummaryResponse;
import com.dietmall.user.entity.ExerciseRecord;
import com.dietmall.user.entity.User;
import com.dietmall.user.repository.ExerciseRecordRepository;
import com.dietmall.user.repository.UserRepository;

@Service
public class ExerciseService {

    private static final ZoneId KOREA_ZONE =
            ZoneId.of("Asia/Seoul");

    private final UserRepository userRepository;
    private final ExerciseRecordRepository exerciseRecordRepository;

    public ExerciseService(
            UserRepository userRepository,
            ExerciseRecordRepository exerciseRecordRepository) {

        this.userRepository = userRepository;
        this.exerciseRecordRepository = exerciseRecordRepository;
    }

    // 운동 기록 추가
    @Transactional
    public ExerciseRecordResponse addExercise(
            Long userId,
            ExerciseRecordRequest request) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "사용자를 찾을 수 없습니다."
                        )
                );

        ExerciseRecord exerciseRecord =
                new ExerciseRecord(
                        user,
                        request.getExerciseType(),
                        request.getDurationMinutes(),
                        request.getNote(),
                        Instant.now()
                );

        ExerciseRecord savedExerciseRecord =
                exerciseRecordRepository.save(
                        exerciseRecord
                );

        return ExerciseRecordResponse.from(
                savedExerciseRecord
        );
    }

    // 내 운동 기록 전체 조회
    @Transactional(readOnly = true)
    public List<ExerciseRecordResponse> getAllExercises(
            Long userId) {

        return exerciseRecordRepository
                .findAllByUserIdOrderByRecordedAtDesc(
                        userId
                )
                .stream()
                .map(ExerciseRecordResponse::from)
                .toList();
    }

    // 특정 날짜 운동 기록 조회
    @Transactional(readOnly = true)
    public List<ExerciseRecordResponse> getExercisesByDate(
            Long userId,
            LocalDate date) {

        List<ExerciseRecord> records =
                getExerciseRecordsByDate(
                        userId,
                        date
                );

        return records
                .stream()
                .map(ExerciseRecordResponse::from)
                .toList();
    }

    // 오늘 운동 기록 조회
    @Transactional(readOnly = true)
    public List<ExerciseRecordResponse> getTodayExercises(
            Long userId) {

        LocalDate today =
                LocalDate.now(KOREA_ZONE);

        return getExercisesByDate(
                userId,
                today
        );
    }

    // 오늘 운동 요약
    @Transactional(readOnly = true)
    public ExerciseTodaySummaryResponse getTodaySummary(
            Long userId) {

        LocalDate today =
                LocalDate.now(KOREA_ZONE);

        List<ExerciseRecord> records =
                getExerciseRecordsByDate(
                        userId,
                        today
                );

        int exerciseCount =
                records.size();

        int totalDurationMinutes =
                records.stream()
                        .mapToInt(
                                ExerciseRecord::getDurationMinutes
                        )
                        .sum();

        Integer totalCaloriesBurned =
                calculateTotalCaloriesBurned(
                        records
                );

        return new ExerciseTodaySummaryResponse(
                exerciseCount,
                totalDurationMinutes,
                totalCaloriesBurned
        );
    }

    // 특정 날짜의 운동 엔티티 조회
    private List<ExerciseRecord> getExerciseRecordsByDate(
            Long userId,
            LocalDate date) {

        Instant start =
                date.atStartOfDay(KOREA_ZONE)
                        .toInstant();

        Instant end =
                date.plusDays(1)
                        .atStartOfDay(KOREA_ZONE)
                        .toInstant()
                        .minusNanos(1);

        return exerciseRecordRepository
                .findAllByUserIdAndRecordedAtBetweenOrderByRecordedAtDesc(
                        userId,
                        start,
                        end
                );
    }

    // 칼로리 합계 계산
    private Integer calculateTotalCaloriesBurned(
            List<ExerciseRecord> records) {

        // 운동 기록이 하나도 없으면 0
        if (records.isEmpty()) {
            return 0;
        }

        // 하나라도 칼로리 분석이 안 된 기록이 있으면
        // 아직 총 칼로리를 확정할 수 없으므로 null
        boolean hasUncalculatedRecord =
                records.stream()
                        .anyMatch(record ->
                                record.getCaloriesBurned() == null
                        );

        if (hasUncalculatedRecord) {
            return null;
        }

        return records.stream()
                .mapToInt(
                        ExerciseRecord::getCaloriesBurned
                )
                .sum();
    }
}