package com.dietmall;

import com.google.firebase.FirebaseApp;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class DietmallApplicationTests {

    @MockitoBean
    private FirebaseApp firebaseApp;

    @Test
    void contextLoads() {
    }
}