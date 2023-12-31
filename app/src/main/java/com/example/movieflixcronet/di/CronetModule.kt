package com.example.movieflixcronet.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.chromium.net.CronetEngine
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object CronetModule {

    @Singleton
    @Provides
    fun provideCronetEngine(@ApplicationContext context: Context): CronetEngine {
        // Customize your CronetEngine creation here
        return CronetEngine.Builder(context)
            .enableHttpCache(CronetEngine.Builder.HTTP_CACHE_IN_MEMORY, 10 * 1024 * 1024).build()

    }

    @Singleton
    @Provides
    fun provideExecutor(): Executor = ThreadPoolExecutor(
        2,
        4,
        60,
        java.util.concurrent.TimeUnit.SECONDS,
        java.util.concurrent.LinkedBlockingQueue()
    )
}