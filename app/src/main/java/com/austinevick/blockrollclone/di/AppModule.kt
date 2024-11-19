package com.austinevick.blockrollclone.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.austinevick.blockrollclone.data.Api
import com.austinevick.blockrollclone.data.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Api.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(2000, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences{
        return application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(retrofit: Retrofit): AuthRepository {
        return retrofit.create(AuthRepository::class.java)
    }

}