package com.duongnd.ecommerceapp

import android.app.Application
import com.duongnd.ecommerceapp.data.repository.GoShipRepository
import com.duongnd.ecommerceapp.data.repository.UsersRepository
import com.duongnd.ecommerceapp.di.AppModule
import com.duongnd.ecommerceapp.utils.SessionManager
import com.duongnd.ecommerceapp.viewmodel.GoShipViewModel
import com.duongnd.ecommerceapp.viewmodel.GoShipViewModelFactory
import com.duongnd.ecommerceapp.viewmodel.UserViewModel
import com.duongnd.ecommerceapp.viewmodel.UserViewModelFactory
import dagger.hilt.android.HiltAndroidApp
import logcat.AndroidLogcatLogger
import logcat.LogPriority

@HiltAndroidApp
class AppApplication : Application() {
    lateinit var goShipViewModel: GoShipViewModel
    lateinit var userViewModel: UserViewModel
    var sessionManager: SessionManager = SessionManager()
    override fun onCreate() {
        super.onCreate()
        goShipViewModel = GoShipViewModelFactory(
            goShipRepository = GoShipRepository(
                goShipService = AppModule.provideApiGoShip(),
                cityDao = AppModule.provideCityDao(
                    ecommerceDatabase = AppModule.provideAppDatabase(
                        this
                    )
                )
            )
        ).create(GoShipViewModel::class.java)

        userViewModel = UserViewModelFactory(
            usersRepository = UsersRepository(
                ecommerceApiService = AppModule.provideApi()
            )
        ).create(UserViewModel::class.java)
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)
        goShipViewModel.getAllCities()
        sessionManager.SessionManager(this)
//        val token = sessionManager.getToken()!!
//        userViewModel.getUserProfile(token)
    }

}