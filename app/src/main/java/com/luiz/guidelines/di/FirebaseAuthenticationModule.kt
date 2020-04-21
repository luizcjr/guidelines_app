package com.luiz.guidelines.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

@Module
open class FirebaseAuthenticationModule {

    @Provides
    fun providesFirebaseAuthentication() : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}