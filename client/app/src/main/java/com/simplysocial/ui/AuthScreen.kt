package com.simplysocial.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.simplysocial.di.SupabaseModule
import com.simplysocial.network.installAuth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.post
import kotlinx.coroutines.launch

@Composable
fun AuthScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Ready") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text("SimplySocial", style = MaterialTheme.typography.headlineLarge)

        Spacer(Modifier.height(32.dp))

        TextField(value = email, onValueChange = { email = it }, label = { Text("School Email") })
        Spacer(Modifier.height(8.dp))
        TextField(value = password, onValueChange = { password = it }, label = { Text("Password") })

        Spacer(Modifier.height(16.dp))

        // REGISTER BUTTON
        Button(onClick = {
            scope.launch {
                status = "Signing Up..."
                try {
                    // Updated Syntax: use Providers.Email explicitly
                    SupabaseModule.client.auth.signUpWith(Email) {
                        this.email = email
                        this.password = password
                    }
                    status = "Check your email for confirmation!"
                } catch (e: Exception) {
                    status = "Error: ${e.message}"
                }
            }
        }) {
            Text("Register")
        }

        // LOGIN BUTTON
        Button(onClick = {
            scope.launch {
                status = "Logging In..."
                try {
                    // 1. Sign In
                    SupabaseModule.client.auth.signInWith(Email) {
                        this.email = email
                        this.password = password
                    }

                    // 2. Sync (using our secure token logic)
                    syncWithBackend()

                    status = "Success! Connected to Institution."
                } catch (e: Exception) {
                    status = "Login Failed: ${e.message}"
                }
            }
        }) {
            Text("Login")
        }

        Spacer(Modifier.height(16.dp))
        Text(status)
    }
}

// SECURE SYNC FUNCTION (Uses the AuthInterceptor logic previously defined)
suspend fun syncWithBackend() {
    // Create a fresh Ktor client here with the Auth Interceptor applied
    val client = HttpClient(Android) {
        installAuth() // Attaches Bearer Token
    }

    // POST to backend (Empty body, purely for token validation)
    val response = client.post("http://10.0.2.2:8080/api/auth/sync")

    if (response.status.value != 200) {
        throw Exception("Backend Sync failed: ${response.status}")
    }
}