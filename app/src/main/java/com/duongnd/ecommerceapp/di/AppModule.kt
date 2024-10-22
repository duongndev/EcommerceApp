package com.duongnd.ecommerceapp.di

import android.content.Context
import androidx.room.Room
import com.duongnd.ecommerceapp.BuildConfig
import com.duongnd.ecommerceapp.data.api.EcommerceApiService
import com.duongnd.ecommerceapp.data.api.GoShipService
import com.duongnd.ecommerceapp.data.database.CityDao
import com.duongnd.ecommerceapp.data.database.EcommerceDatabase
import com.duongnd.ecommerceapp.utils.Constants.Companion.BASE_URL1
import com.duongnd.ecommerceapp.utils.Constants.Companion.SANBOX_GOSHIP_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkHttpLoggingInterceptor() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        })
        .build()


    @Singleton
    @Provides
    fun provideApi(): EcommerceApiService {
        return Retrofit.Builder().baseUrl(BASE_URL1)
            .client(provideOkHttpLoggingInterceptor())
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(EcommerceApiService::class.java)
    }


    @Singleton
    @Provides
    fun provideApiGoShip(): GoShipService {
        return Retrofit.Builder().baseUrl(BuildConfig.SANBOX_GOSHIP_URL)
            .client(provideOkHttpLoggingInterceptor())
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(GoShipService::class.java)
    }


    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): EcommerceDatabase {
        return Room.databaseBuilder(
            context,
            EcommerceDatabase::class.java,
            "ecommerce_database"
        ).build()
    }

    @Provides
    fun provideCityDao(ecommerceDatabase: EcommerceDatabase): CityDao {
        return ecommerceDatabase.cityDao()
    }

}