package com.simplysocial.network

import com.simplysocial.di.SupabaseModule
import io.github.jan.supabase.auth.auth
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header

// This function configures the Ktor HTTP Client to auto-attach the token
//fun HttpClient.Config.installAuth() {
//    install(DefaultRequest) {
//        // Get the current session token from Supabase
//        val token = SupabaseModule.client.auth.currentSessionOrNull()?.accessToken
//        if (token != null) {
//            header("Authorization", "Bearer $token")
//        }
//    }
//}
fun HttpClientConfig<*>.installAuth() {
    install(DefaultRequest) {
        val token = SupabaseModule.client.auth.currentSessionOrNull()?.accessToken
        if (token != null) {
            header("Authorization", "Bearer $token")
        }
    }
}