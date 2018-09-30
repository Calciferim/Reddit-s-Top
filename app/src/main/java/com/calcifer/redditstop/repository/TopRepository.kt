package com.calcifer.redditstop.repository

import com.calcifer.redditstop.api.RedditApiService
import com.calcifer.redditstop.models.Top
import io.reactivex.Observable

class TopRepository(private val apiService: RedditApiService) {

    fun getTopPosts(after: String): Observable<Top> {
        return apiService.getTop(after)
    }
}