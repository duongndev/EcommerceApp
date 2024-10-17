package com.duongnd.ecommerceapp.di

import com.duongnd.ecommerceapp.data.api.EcommerceApiService
import com.duongnd.ecommerceapp.utils.Constants.Companion.BASE_URL1
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApi(): EcommerceApiService {
        return Retrofit.Builder().baseUrl(BASE_URL1)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(EcommerceApiService::class.java)
    }


}