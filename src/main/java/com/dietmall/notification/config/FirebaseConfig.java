package com.dietmall.notification.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

    @Value("${FIREBASE_SERVICE_ACCOUNT_JSON:}")
    private String firebaseServiceAccountJson;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {

        if (!FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.getInstance();
        }

        GoogleCredentials credentials;

        if (firebaseServiceAccountJson != null
                && !firebaseServiceAccountJson.isBlank()) {

            ByteArrayInputStream inputStream =
                    new ByteArrayInputStream(
                            firebaseServiceAccountJson
                                    .getBytes(StandardCharsets.UTF_8)
                    );

            credentials =
                    GoogleCredentials.fromStream(inputStream);

        } else {

            credentials =
                    GoogleCredentials.getApplicationDefault();
        }

        FirebaseOptions options =
                FirebaseOptions.builder()
                        .setCredentials(credentials)
                        .build();

        return FirebaseApp.initializeApp(options);
    }
}