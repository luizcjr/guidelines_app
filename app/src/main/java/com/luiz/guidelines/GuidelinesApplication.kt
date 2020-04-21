package com.luiz.guidelines

import android.app.Application
import android.content.Context
import com.mlykotom.valifi.ValiFi

open class GuidelinesApplication : Application() {
    companion object {
        var context: Context? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        /* Set instance */
        context = applicationContext

        ValiFi.install(this)
    }
}