package com.example.apipracticeapp.di

import com.example.apipracticeapp.data.ApiService
import com.example.apipracticeapp.data.GithubAPIRepository
import com.example.apipracticeapp.data.GithubAPIRepositoryImpl
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GithubAPIModule {
    @Provides
    @Singleton
    // Moshiのインスタンスを生成する
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .build()
    }

    @Provides
    @Singleton
    // Retrofitのインスタンスを生成する
    fun provideRetrofit(
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideGitHubService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class GithubAPIRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindWeatherAPIRepository(
        impl: GithubAPIRepositoryImpl
    ): GithubAPIRepository
}