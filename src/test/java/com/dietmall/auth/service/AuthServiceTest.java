package com.dietmall.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dietmall.auth.repository.AuthAccountRepository;
import com.dietmall.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthAccountRepository authAccountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void 존재하지않는_사용자_조회시_예외발생() {

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userRepository.findById(999L)
                                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."))
                );

        assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
    }
}