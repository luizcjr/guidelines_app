package com.luiz.guidelines.di

import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides

@Module
open class FirebaseDatabaseModule {
    @Provides
    fun providesFirebaseDatabase() : FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }
}