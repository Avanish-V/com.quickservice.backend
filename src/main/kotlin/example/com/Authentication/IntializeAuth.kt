package com.example.Authentication

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import example.com.module
import java.io.InputStream

object FirebaseAdmin {

    val serviceAccount: InputStream = this::class.java.classLoader.getResourceAsStream("quickserve-4bf8c-firebase-adminsdk-9dnxf-1505c0a7de.json")
        ?: throw IllegalStateException("Firebase service account file not found")

    private val options: FirebaseOptions = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()

    fun init(): FirebaseApp = FirebaseApp.initializeApp(options)
}