package com.luiz.guidelines.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.luiz.guidelines.di.DaggerActivityComponent
import com.luiz.guidelines.util.Utils
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    val firebaseUser by lazy { MutableLiveData<FirebaseUser>() }

    protected lateinit var mDatabaseReference: DatabaseReference

    @Inject
    protected lateinit var firebaseAuth: FirebaseAuth

    @Inject
    protected lateinit var mDatabase: FirebaseDatabase

    init {
        DaggerActivityComponent.create().inject(this)
    }

    protected val loadingLiveDataObserver = Observer<Boolean> { isLoading ->
        if (isLoading) {
            Utils.onStartLoading(this)
        } else {
            Utils.onStopLoading()
        }
    }

    protected val errorLiveDataObserver = Observer<String> { error ->
        if (error != null) {
            Utils.alertError(error, this)
        }
    }

    protected val successLiveDataObserver = Observer<String> { success ->
        if (success != null) {
            Utils.alertSuccess(success, this)
        }
    }
}