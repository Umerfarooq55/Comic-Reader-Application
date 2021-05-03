
package com.example.shortcut.net.api

import com.example.shortcut.net.api.model.ComicStrip

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

class ComicApi(retrofit: Retrofit) {

    private val api: Api = retrofit.create(Api::class.java)

    fun getLatest() = api.getLatest()

    fun getForId(comicId: String) = api.getForId(comicId)

    /**
     * @see <a href="https://any-api.com/xkcd_com/xkcd_com/docs/API_Description">Any API definition</a>
     */
    interface Api {

        @GET("/{comicId}/info.0.json")
        fun getForId(@Path("comicId") comicId: String): Single<ComicStrip>

        @GET("/info.0.json")
        fun getLatest(): Single<ComicStrip>
    }
}
