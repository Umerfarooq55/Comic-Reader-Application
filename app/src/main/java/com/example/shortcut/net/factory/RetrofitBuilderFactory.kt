package com.example.shortcut.net.factory

import com.example.shortcut.net.error.RxErrorHandlingCallAdapterFactory
import com.google.gson.Gson

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object RetrofitBuilderFactory {

    fun providesRetrofit(
        baseUrl: String,
        gson: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient)
            .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
    }
}
