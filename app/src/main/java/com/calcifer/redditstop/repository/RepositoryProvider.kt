package com.calcifer.redditstop.repository

import com.calcifer.redditstop.api.RedditApiService

object RepositoryProvider {
    fun provideTopRepository(): TopRepository {
        return TopRepository(RedditApiService.create())
    }
}