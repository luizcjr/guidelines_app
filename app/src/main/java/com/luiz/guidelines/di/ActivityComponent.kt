package com.luiz.guidelines.di

import com.luiz.guidelines.ui.base.BaseActivity
import dagger.Component

@Component(modules = [FirebaseAuthenticationModule::class, FirebaseDatabaseModule::class])
interface ActivityComponent {
    fun inject(activity: BaseActivity)
}