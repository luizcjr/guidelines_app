package com.luiz.guidelines.di

import com.luiz.guidelines.ui.base.BaseViewModel
import dagger.Component

@Component(modules = [FirebaseAuthenticationModule::class, FirebaseDatabaseModule::class])
interface ViewModelComponent {
    fun inject(viewModel: BaseViewModel)
}