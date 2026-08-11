package com.dietmall.user.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class MealImageStorageServiceTest {

    @Test
    void 허용되지않은_확장자_업로드시_예외발생() {

        MealImageStorageService service =
                new MealImageStorageService("build/test-uploads");

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "test.exe",
                        "application/octet-stream",
                        "fake".getBytes()
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.saveImage(file)
        );
    }
}