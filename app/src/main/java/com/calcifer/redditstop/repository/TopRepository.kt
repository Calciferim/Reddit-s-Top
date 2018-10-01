package com.calcifer.redditstop.repository

import com.calcifer.redditstop.api.RedditApiService
import com.calcifer.redditstop.models.Post
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TopRepository(private val apiService: RedditApiService) {

    private var after: String = ""

    fun getNextTopPosts(onGetTopPostsComplete: OnGetTopPostsComplete) {
        apiService.getTop(after)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    after = result.topData.after
                    onGetTopPostsComplete.onGetTopPostsSuccess(result.topData.children)
                }, { error ->
                    onGetTopPostsComplete.onGetTopPostsFailure(error.message!!)
                })
    }

    interface OnGetTopPostsComplete {
        fun onGetTopPostsSuccess(result: ArrayList<Post>)
        fun onGetTopPostsFailure(error: String)
    }
}