package com.duongnd.ecommerceapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.squareup.picasso.BuildConfig
import io.socket.client.IO
import io.socket.client.Socket
import timber.log.Timber
import java.net.URISyntaxException

@HiltAndroidApp
class AppApplication : Application() {

//    lateinit var mSocket: Socket

    override fun onCreate() {
        super.onCreate()
//        try {
//            mSocket = IO.socket("http://192.168.99.52:8080/")
//        }catch (e: URISyntaxException){
//            e.printStackTrace()
//        }

        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}