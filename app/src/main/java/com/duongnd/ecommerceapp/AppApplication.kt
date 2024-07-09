package com.duongnd.ecommerceapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.squareup.picasso.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}