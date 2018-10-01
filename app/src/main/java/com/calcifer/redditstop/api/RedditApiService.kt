package com.calcifer.redditstop.api

import com.calcifer.redditstop.models.Top
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable;
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://www.reddit.com/"

interface RedditApiService {

    @GET("top.json")
    fun getTop(@Query("after") after: String): Observable<Top>

    companion object Factory {
        fun create(): RedditApiService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()

            return retrofit.create(RedditApiService::class.java)
        }
    }
}