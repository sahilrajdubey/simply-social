package com.simplysocial.di

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient


object SupabaseModule {
    val client = createSupabaseClient(
        supabaseUrl = "https://sqzmgskbkpwilkwqldpr.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNxem1nc2tia3B3aWxrd3FsZHByIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjY5MjAwNDgsImV4cCI6MjA4MjQ5NjA0OH0.Jyojp_JXrVcZUk91F_eHOluRIKvc_3vkgdcUxbSsj1k"
    ) {
        install(Auth)
    }
}