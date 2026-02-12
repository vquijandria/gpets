package com.gpets.gpetsapi.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.databaseUrl:}")
    private String databaseUrlProperty;

    @Value("${firebase.credentialsPath:}")
    private String credentialsPathProperty;

    @PostConstruct
    public void init() throws IOException {

        if (!FirebaseApp.getApps().isEmpty()) {
            return;
        }


        String credentialsPath = System.getenv("FIREBASE_CREDENTIALS_PATH");


        if (credentialsPath == null || credentialsPath.isBlank()) {
            credentialsPath = credentialsPathProperty;
        }

        if (credentialsPath == null || credentialsPath.isBlank()) {
            throw new IllegalStateException("Firebase credentials path not configured");
        }

        GoogleCredentials credentials;
        try (FileInputStream serviceAccount = new FileInputStream(credentialsPath)) {
            credentials = GoogleCredentials.fromStream(serviceAccount);
        }

        FirebaseOptions.Builder builder = FirebaseOptions.builder()
                .setCredentials(credentials);


        String databaseUrl = System.getenv("FIREBASE_DATABASE_URL");

        if (databaseUrl == null || databaseUrl.isBlank()) {
            databaseUrl = databaseUrlProperty;
        }

        if (databaseUrl != null && !databaseUrl.isBlank()) {
            builder.setDatabaseUrl(databaseUrl);
        }

        FirebaseApp.initializeApp(builder.build());
    }
}
