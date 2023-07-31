package com.example.pokeapp.di

import com.example.pokeapp.data.remote.PokeService
import com.example.pokeapp.data.repository.PokeRepository
import com.example.pokeapp.util.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class RemoteModule {
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun providePokeService(retrofit: Retrofit): PokeService {
        return retrofit.create(PokeService::class.java)
    }

    @Provides
    fun providePokeRepository(pokeService: PokeService): PokeRepository {
        return PokeRepository(pokeService)
    }
}