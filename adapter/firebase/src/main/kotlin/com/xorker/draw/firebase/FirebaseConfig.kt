package com.xorker.draw.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class FirebaseConfig {
    @Value("\${firebase.config}")
    lateinit var config: String

    @PostConstruct
    fun firebaseApp(): FirebaseApp {
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(config.byteInputStream()))
            .build()

        return FirebaseApp.initializeApp(options)
    }
}
