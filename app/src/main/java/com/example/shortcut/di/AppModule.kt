package com.example.shortcut.di


import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.example.shortcut.BuildConfig
import com.example.shortcut.ui.image.ImageLoader
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

const val NAME_DEBUG = "named.is_debug_enabled"
const val NAME_URL = "key.base.url"

@Module
internal class AppModule {

    @Provides
    @Singleton
    fun providesApplicationContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    fun providesResources(application: Application): Resources {
        return application.resources
    }

    @Provides
    @Named(NAME_DEBUG)
    internal fun providesDebugFlag(): Boolean {
        return BuildConfig.DEBUG
    }

    @Provides
    @Named(NAME_URL)
    internal fun providesBaseUrl(): String {
        return "https://xkcd.com"
    }

    @Provides
    internal fun providesGson(): Gson {
        return GsonBuilder().setPrettyPrinting().create()
    }

    @Provides
    internal fun providesExecutionScheduler(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    internal fun providesImageHlper(context: Context): ImageLoader {
        return ImageLoader(context)
    }
}
