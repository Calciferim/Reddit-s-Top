package com.calcifer.redditstop.repository

import com.calcifer.redditstop.api.RedditApiService

object RepositoryProvider {
    fun provideSearchRepository(): TopRepository {
        return TopRepository(RedditApiService.create())
    }
}