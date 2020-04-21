package com.luiz.guidelines.ui.base

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.luiz.guidelines.di.DaggerViewModelComponent
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    lateinit var context: Context

    val firebaseUser by lazy { MutableLiveData<FirebaseUser>() }
    val loadError by lazy { MutableLiveData<String>() }
    val loadSuccess by lazy { MutableLiveData<String>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    protected lateinit var mDatabaseReference: DatabaseReference

    @Inject
    protected lateinit var firebaseAuth: FirebaseAuth
    @Inject
    protected lateinit var mDatabase: FirebaseDatabase

    init {
        DaggerViewModelComponent.create().inject(this)
    }
}