package com.calcifer.redditstop.contracts

import com.calcifer.redditstop.models.Post

interface TopContract {

    interface View : BaseContract.View {
        fun hideMainLoading()
        fun initPostAdapter(posts: ArrayList<Post>)
        fun openChromeTab(url: String)
        fun onPostsInsertion(startPosition: Int, size: Int)
        fun hidePaginationLoading()
        fun showRetryWithError(error: String)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onPostClick(post: Post)
        fun onRetryClick()
        fun loadNextPage()
    }

}
