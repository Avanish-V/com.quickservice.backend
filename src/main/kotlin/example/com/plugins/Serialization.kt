package com.example.plugins

import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

fun Application.configureSerialization() {

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()

        }
        Json {
            ignoreUnknownKeys = true // Ignore unknown fields from the JSON response
            isLenient = true
        }

    }
}
